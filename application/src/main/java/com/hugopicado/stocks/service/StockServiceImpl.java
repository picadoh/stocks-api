package com.hugopicado.stocks.service;

import com.google.common.annotations.VisibleForTesting;
import com.hugopicado.stocks.domain.Stock;
import com.hugopicado.stocks.domain.Trade;
import com.hugopicado.stocks.repository.GenericRepository;
import org.apache.commons.math3.stat.StatUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.hugopicado.stocks.spec.StockSpecification.withSymbol;
import static com.hugopicado.stocks.spec.TradeSpecification.forStock;
import static com.hugopicado.stocks.spec.TradeSpecification.within;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Service
public class StockServiceImpl implements StockService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);

    private final GenericRepository<Stock> stockRepository;
    private final GenericRepository<Trade> tradeRepository;

    @Value("${stocks.stockprice.tradeTimeRangeMinutes}")
    private int stockPriceRangeMins;

    @Autowired
    public StockServiceImpl(GenericRepository<Stock> stockRepository, GenericRepository<Trade> tradeRepository) {
        this.stockRepository = stockRepository;
        this.tradeRepository = tradeRepository;
    }

    @Override
    public Double calculateDividendYield(String stockSymbol) {
        LOGGER.trace("Calculating dividend yield for stock with symbol {}", stockSymbol);

        Stock stock = stockRepository.findOne(withSymbol(stockSymbol));
        return validateStock(stockSymbol, stock).getDividendYield();
    }

    @Override
    public Double calculatePERatio(String stockSymbol) {
        Stock stock = stockRepository.findOne(withSymbol(stockSymbol));
        return validateStock(stockSymbol, stock).getPERatio();
    }

    @Override
    public Double calculateStockPrice(String stockSymbol) {
        return calculateStockPriceFromPastMinutes(stockSymbol, stockPriceRangeMins);
    }

    @Override
    public Double calculateGBCEAllSharesIndex() {
        // retrieve all stocks
        Collection<Stock> stocks = stockRepository.findAll();

        if (isEmpty(stocks)) {
            LOGGER.warn("Could not find stocks");
            throw new ServiceException("no stock found");
        }

        // retrieve an array of prices (taking advantage of commons math3 lib)
        double[] prices = stocks.stream()
                .map(s -> calculateStockPriceFromPastMinutes(s.getSymbol(), 0))
                .mapToDouble(Double::doubleValue)
                .toArray();

        // calculate all share index
        return StatUtils.geometricMean(prices);
    }

    @Override
    public void recordStock(Stock stock) {
        stockRepository.save(stock);
    }

    @Override
    public Stock retrieveStock(String stockSymbol) {
        Stock stock = stockRepository.findOne(withSymbol(stockSymbol));

        // return stock details if it exists
        return validateStock(stockSymbol, stock);
    }

    private Stock validateStock(String stockSymbol, Stock stock) {
        if (stock != null) {
            // return PE ratio if stock exists
            LOGGER.debug("Found stock {}", stock);
            return stock;
        } else {
            LOGGER.warn("Could not find any stock for symbol {}", stockSymbol);
            throw new ServiceException("stock not found");
        }
    }

    @VisibleForTesting
    Double calculateStockPriceFromPastMinutes(String stockSymbol, int offsetMinutes) {
        // retrieve trades for a specific stock symbol from the past minutes
        DateTime now = new DateTime(DateTimeZone.UTC);

        Collection<Trade> trades = tradeRepository.findAll(
                forStock(stockSymbol).and(within(now.minusMinutes(offsetMinutes), now))
        );

        if (isEmpty(trades)) {
            LOGGER.warn("Could not find any stock for symbol {} within last {} minutes", stockSymbol, offsetMinutes);
            throw new ServiceException("trades not found");
        }

        LOGGER.debug("Found trades {}", trades);

        // sum the product tradePrice*quantity
        double productSum = trades.stream()
                .map(t -> t.getPrice() * t.getQuantity())
                .mapToDouble(Double::doubleValue)
                .sum();

        // sum the quantity
        int quantitySum = trades.stream()
                .map(Trade::getQuantity)
                .mapToInt(Integer::intValue)
                .sum();

        // calculate stock price for from past minutes if quantity sum greater than zero
        if (quantitySum == 0) {
            LOGGER.warn("Quantity sum is zero for stock symbol {} within last {} minutes", stockSymbol, offsetMinutes);
            throw new ServiceException("quantity sum is zero");
        }

        return productSum / quantitySum;
    }

}
