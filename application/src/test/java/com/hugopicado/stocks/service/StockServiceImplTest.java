package com.hugopicado.stocks.service;

import com.hugopicado.stocks.domain.Stock;
import com.hugopicado.stocks.domain.Trade;
import com.hugopicado.stocks.repository.GenericRepository;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.DecimalFormat;
import java.util.function.Predicate;

import static com.google.common.collect.Sets.newHashSet;
import static com.hugopicado.stocks.makers.StockMaker._ValidStock;
import static com.hugopicado.stocks.makers.StockMaker._symbol;
import static com.hugopicado.stocks.makers.TradeMaker.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

public class StockServiceImplTest {

    @Mock
    private GenericRepository<Stock> stockRepo;

    @Mock
    private GenericRepository<Trade> tradeRepo;

    private StockServiceImpl victim;

    @BeforeMethod
    public void setupScenario() {
        initMocks(this);
        victim = new StockServiceImpl(stockRepo, tradeRepo);
    }

    @Test
    public void shouldCalculateDividend() {
        Stock stock = mock(Stock.class);
        when(stock.getDividendYield()).thenReturn(99.);

        when(stockRepo.findOne(any(Predicate.class))).thenReturn(stock);

        Double dividend = victim.calculateDividendYield("FOO");

        assertEquals(dividend, 99.);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void shouldThrowExceptionWhenCalculatingDividendAndStockDoesNotExist() {
        when(stockRepo.findOne(any(Predicate.class))).thenReturn(null);
        victim.calculateDividendYield("FOO");
    }

    @Test
    public void shouldCalculatePERatio() {
        Stock stock = mock(Stock.class);
        when(stock.getPERatio()).thenReturn(0.99);

        when(stockRepo.findOne(any(Predicate.class))).thenReturn(stock);

        Double peRatio = victim.calculatePERatio("FOO");

        assertEquals(peRatio, 0.99);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void shouldThrowExceptionWhenCalculatingPERatioAndStockDoesNotExist() {
        when(stockRepo.findOne(any(Predicate.class))).thenReturn(null);
        victim.calculatePERatio("FOO");
    }

    @Test
    public void shouldCalculateStockPrice() {
        StockServiceImpl spied = spy(victim);
        doReturn(99.).when(spied).calculateStockPriceFromPastMinutes(eq("FOO"), anyInt());

        Double price = spied.calculateStockPrice("FOO");
        assertEquals(price, 99.);
    }

    @Test
    public void shouldCalculateStockPriceFromPastMinutes() {
        Trade trade1 = make(a(_ValidTrade, with(_price, 10.0), with(_quantity, 1)));
        Trade trade2 = make(a(_ValidTrade, with(_price, 20.0), with(_quantity, 2)));
        Trade trade3 = make(a(_ValidTrade, with(_price, 30.0), with(_quantity, 5)));

        // price = (10*1 + 20*2 + 30*5) / (1+2+5) = 140 / 7 = 25

        when(tradeRepo.findAll(any(Predicate.class))).thenReturn(
                newHashSet(trade1, trade2, trade3));

        Double price = victim.calculateStockPriceFromPastMinutes("FOO", 15);
        assertEquals(price, 25.);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void shouldThrowExceptionWhenCalculatingStockPriceAndQuantitySumIsZero() {
        Trade trade1 = make(a(_ValidTrade, with(_price, 10.0), with(_quantity, 0)));
        Trade trade2 = make(a(_ValidTrade, with(_price, 20.0), with(_quantity, 0)));
        Trade trade3 = make(a(_ValidTrade, with(_price, 30.0), with(_quantity, 0)));

        // price = (10*0 + 20*0 + 30*0) / (0+0+0) = 140 / 0 = NaN

        when(tradeRepo.findAll(any(Predicate.class))).thenReturn(
                newHashSet(trade1, trade2, trade3));

        victim.calculateStockPriceFromPastMinutes("FOO", 15);
    }

    @Test(expectedExceptions = ServiceException.class)
    public void shouldThrowExceptionWhenCalculatingStockPriceAndNoTradesFound() {
        when(tradeRepo.findAll(any(Predicate.class))).thenReturn(
                newHashSet());
        victim.calculateStockPriceFromPastMinutes("FOO", 15);
    }

    @Test
    public void shouldCalculateGBCEAllSharesIndex() {
        StockServiceImpl spied = spy(victim);
        doReturn(3.).when(spied).calculateStockPriceFromPastMinutes(eq("FOO"), anyInt());
        doReturn(9.).when(spied).calculateStockPriceFromPastMinutes(eq("BAR"), anyInt());
        doReturn(27.).when(spied).calculateStockPriceFromPastMinutes(eq("XYZ"), anyInt());

        Stock stock1 = make(a(_ValidStock, with(_symbol, "FOO")));
        Stock stock2 = make(a(_ValidStock, with(_symbol, "BAR")));
        Stock stock3 = make(a(_ValidStock, with(_symbol, "XYZ")));
        when(stockRepo.findAll()).thenReturn(newHashSet(stock1, stock2, stock3));

        // geometric mean = nsqrt(3, 3*9*27) = 9

        Double gbceAsi = spied.calculateGBCEAllSharesIndex();

        DecimalFormat df = new DecimalFormat("0.00000");
        assertEquals(df.format(gbceAsi), "9.00000");
    }

    @Test(expectedExceptions = ServiceException.class)
    public void shouldThrowExceptionWhenCalculatingGBCEAsiAndNoStocksFound() {
        when(stockRepo.findAll()).thenReturn(newHashSet());
        victim.calculateGBCEAllSharesIndex();
    }

    @Test(expectedExceptions = ServiceException.class)
    public void shouldThrowExceptionWhenCalculatingGBCEAsiAndCannotComputePrice() {
        StockServiceImpl spied = spy(victim);
        doReturn(3.).when(spied).calculateStockPriceFromPastMinutes(eq("FOO"), anyInt());
        doThrow(new ServiceException("x")).when(spied).calculateStockPriceFromPastMinutes(eq("BAR"), anyInt());
        doReturn(27.).when(spied).calculateStockPriceFromPastMinutes(eq("XYZ"), anyInt());

        Stock stock1 = make(a(_ValidStock, with(_symbol, "FOO")));
        Stock stock2 = make(a(_ValidStock, with(_symbol, "BAR")));
        Stock stock3 = make(a(_ValidStock, with(_symbol, "XYZ")));
        when(stockRepo.findAll()).thenReturn(newHashSet(stock1, stock2, stock3));

        victim.calculateGBCEAllSharesIndex();
    }

    @Test
    public void shouldRecordStock() {
        Stock stock = make(a(_ValidStock, with(_symbol, "FOO")));
        victim.recordStock(stock);
        verify(stockRepo).save(eq(stock));
    }

}
