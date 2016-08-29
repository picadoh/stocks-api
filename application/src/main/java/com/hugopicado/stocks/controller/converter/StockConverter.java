package com.hugopicado.stocks.controller.converter;

import com.hugopicado.stocks.controller.api.StockDto;
import com.hugopicado.stocks.domain.Stock;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Converts a Stock from the internal domain into a Stock DTO.
 */
@Component
public class StockConverter implements Function<Stock, StockDto> {
    @Override
    public StockDto apply(Stock stock) {
        StockDto stockDto = new StockDto();

        stockDto.setSymbol(stock.getSymbol());
        stockDto.setType(stock.getType().name());
        stockDto.setFixedDividend(stock.getFixedDividend());
        stockDto.setLastDividend(stock.getLastDividend());
        stockDto.setParValue(stock.getParValue());
        stockDto.setTickerPrice(stock.getTickerPrice());

        return stockDto;
    }
}
