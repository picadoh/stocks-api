package com.hugopicado.stocks.controller.converter;

import com.hugopicado.stocks.controller.api.StockDto;
import com.hugopicado.stocks.domain.Stock;
import com.hugopicado.stocks.domain.StockType;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Converts a Stock DTO into a Stock from the internal domain.
 */
@Component
public class StockDtoConverter implements Function<StockDto, Stock> {
    @Override
    public Stock apply(StockDto stockDto) {
        String symbol = stockDto.getSymbol();
        StockType type = StockType.valueOf(stockDto.getType());
        double fixedDividend = stockDto.getFixedDividend();
        double lastDividend = stockDto.getLastDividend();
        double parValue = stockDto.getParValue();
        double tickerPrice = stockDto.getTickerPrice();

        return new Stock(symbol, type, lastDividend, fixedDividend, parValue, tickerPrice);
    }
}
