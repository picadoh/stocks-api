package com.hugopicado.stocks.controller;

import com.hugopicado.stocks.controller.api.*;
import com.hugopicado.stocks.controller.converter.StockConverter;
import com.hugopicado.stocks.controller.converter.StockDtoConverter;
import com.hugopicado.stocks.domain.Stock;
import com.hugopicado.stocks.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides the operations over stocks.
 */
@RestController
public class StockController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    private final StockService stockService;
    private final StockDtoConverter stockDtoConverter;
    private final StockConverter stockConverter;

    @Autowired
    public StockController(StockService stockService, StockDtoConverter stockDtoConverter, StockConverter stockConverter) {
        this.stockService = stockService;
        this.stockDtoConverter = stockDtoConverter;
        this.stockConverter = stockConverter;
    }

    @RequestMapping(value = "/stock", method = RequestMethod.POST)
    public ResponseEntity<String> postStock(@RequestBody StockDto stockDto) {
        LOGGER.debug("received post stock request with data {}", stockDto);

        try {
            validateStockDto(stockDto);
        } catch (Exception e) {
            LOGGER.error("Mandatory field not received", e);
            return ResponseEntity.badRequest().body("mandatory field not received");
        }

        Stock stock = stockDtoConverter.apply(stockDto);
        stockService.recordStock(stock);
        return ResponseEntity.ok("Stock added with success");
    }

    private void validateStockDto(StockDto stockDto) {
        checkNotNull(stockDto.getSymbol());
        checkNotNull(stockDto.getType());
    }

    @RequestMapping(value = "/stocks", method = RequestMethod.GET)
    public StockDto getStock(@RequestParam String symbol) {
        LOGGER.debug("received getstock request for symbol {}", symbol);

        Stock stock = stockService.retrieveStock(symbol);
        return stockConverter.apply(stock);
    }

    @RequestMapping(value = "/stocks/dividend", method = RequestMethod.GET)
    public DividendYieldDto getDividend(@RequestParam String symbol) {
        LOGGER.debug("received get dividend request for symbol {}", symbol);

        Double dividendYield = stockService.calculateDividendYield(symbol);
        DividendYieldDto dto = new DividendYieldDto();
        dto.setStockSymbol(symbol);
        dto.setDividend(dividendYield);
        return dto;
    }

    @RequestMapping(value = "/stocks/peratio", method = RequestMethod.GET)
    public PERatioDto getPERatio(@RequestParam String symbol) {
        LOGGER.debug("received get PE ratio request for symbol {}", symbol);

        Double peRatio = stockService.calculatePERatio(symbol);
        PERatioDto dto = new PERatioDto();
        dto.setStockSymbol(symbol);
        dto.setPeRatio(peRatio);
        return dto;
    }

    @RequestMapping(value = "/stocks/price", method = RequestMethod.GET)
    public StockPriceDto getStockPrice(@RequestParam String symbol) {
        LOGGER.debug("received get stock price request for symbol {}", symbol);

        Double stockPrice = stockService.calculateStockPrice(symbol);
        StockPriceDto dto = new StockPriceDto();
        dto.setStockSymbol(symbol);
        dto.setStockPrice(stockPrice);
        return dto;
    }

    @RequestMapping(value = "/stocks/gbceasi", method = RequestMethod.GET)
    public AllSharesIndexDto getGBCEAllSharesIndex() {
        LOGGER.debug("received get GBCE all shares index request");

        Double allSharesIndex = stockService.calculateGBCEAllSharesIndex();
        AllSharesIndexDto dto = new AllSharesIndexDto();
        dto.setAllSharesIndex(allSharesIndex);
        return dto;
    }
}