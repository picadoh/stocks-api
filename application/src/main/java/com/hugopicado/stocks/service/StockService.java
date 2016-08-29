package com.hugopicado.stocks.service;

import com.hugopicado.stocks.domain.Stock;

/**
 * Stock service interface, that provides the operations
 * for calculating dividends, ratios, prices and indexes.
 * <p>
 * Also provides operations for storing and retrieving
 * stocks from the underlying repository.
 */
public interface StockService {

    /**
     * Calculates the dividend yield.
     *
     * @param stockSymbol stock symbol
     * @return dividend yield
     */
    Double calculateDividendYield(String stockSymbol);

    /**
     * Calculates the PE ratio.
     *
     * @param stockSymbol stock symbol
     * @return PE ratio
     */
    Double calculatePERatio(String stockSymbol);

    /**
     * Calculates the stock price based on the trades of past X minutes (configured).
     *
     * @param stockSymbol stock symbol
     * @return stock price
     */
    Double calculateStockPrice(String stockSymbol);

    /**
     * Calculates the GBCE all shares index
     *
     * @return GBCE all shares index
     */
    Double calculateGBCEAllSharesIndex();

    /**
     * Records a stock into the underlying repository.
     *
     * @param stock Stock
     */
    void recordStock(Stock stock);

    /**
     * Retrieves stock details from the underlying repository.
     *
     * @param stockSymbol stock symbol
     * @return Stock details
     */
    Stock retrieveStock(String stockSymbol);
}
