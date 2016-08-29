package com.hugopicado.stocks.spec;

import com.hugopicado.stocks.domain.Stock;

import java.util.function.Predicate;

/**
 * Factory for trade specifications.
 */
public class StockSpecification {

    public static Predicate<Stock> withSymbol(String symbol) {
        return stock -> stock.getSymbol().equals(symbol);
    }

}
