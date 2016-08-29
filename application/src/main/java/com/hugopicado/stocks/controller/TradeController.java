package com.hugopicado.stocks.controller;

import com.hugopicado.stocks.controller.api.TradeDto;
import com.hugopicado.stocks.controller.converter.TradeDtoConverter;
import com.hugopicado.stocks.domain.Trade;
import com.hugopicado.stocks.service.TradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides the operations over trades.
 */
@RestController
public class TradeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);

    private final TradeService tradeService;
    private final TradeDtoConverter tradeDtoConverter;

    @Autowired
    public TradeController(TradeService tradeService, TradeDtoConverter tradeDtoConverter) {
        this.tradeService = tradeService;
        this.tradeDtoConverter = tradeDtoConverter;
    }

    @RequestMapping(value = "/trade", method = RequestMethod.POST)
    public ResponseEntity<String> postTrade(@RequestBody TradeDto tradeDto) {
        LOGGER.debug("received post trade request with data {}", tradeDto);

        try {
            validateTradeDto(tradeDto);
        } catch (Exception e) {
            LOGGER.error("Mandatory field not received", e);
            return ResponseEntity.badRequest().body("mandatory field not received");
        }

        Trade trade = tradeDtoConverter.apply(tradeDto);
        tradeService.recordTrade(trade);
        return ResponseEntity.ok("Trade added with success");
    }

    private void validateTradeDto(TradeDto tradeDto) {
        checkNotNull(tradeDto.getStockSymbol());
        checkNotNull(tradeDto.getIndicator());
    }

}