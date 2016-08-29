package com.hugopicado.stocks;


import com.hugopicado.stocks.controller.api.*;
import com.hugopicado.stocks.domain.Stock;
import com.hugopicado.stocks.domain.StockType;
import com.hugopicado.stocks.domain.Trade;
import com.hugopicado.stocks.repository.LocalStockRepository;
import com.hugopicado.stocks.repository.LocalTradeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.hugopicado.stocks.spec.StockSpecification.withSymbol;
import static com.hugopicado.stocks.spec.TradeSpecification.forStock;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StocksApplicationTest {
    private static final double PRECISION = 10E5;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LocalStockRepository stockRepository;

    @Autowired
    private LocalTradeRepository tradeRepository;

    @Before
    public void cleanup() {
        stockRepository.clear();

        postStock("XYZ", "COMMON", 2.0, 0.02, 3.0, 4.0);
        postTrade("XYZ", 5.0, 2);
        postTrade("XYZ", 5.5, 6);

        postStock("FOO", "PREFERRED", 2.0, 0.02, 4.0, 6.0);
        postTrade("FOO", 10.0, 1);
        postTrade("FOO", 5.5, 3);
    }

    @Test
    public void shouldGetBadRequestWhenPostingInvalidStock() {
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/stock", new StockDto(), String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldPostStock() {
        Stock stock = stockRepository.findOne(withSymbol("XYZ"));
        assertNotNull(stock);
        assertEquals("XYZ", stock.getSymbol());
        assertEquals(StockType.COMMON, stock.getType());
        assertEquals(Double.valueOf(2.0), stock.getLastDividend());
        assertEquals(Double.valueOf(0.02), stock.getFixedDividend());
        assertEquals(Double.valueOf(3.0), stock.getParValue());
        assertEquals(Double.valueOf(4.0), stock.getTickerPrice());
        assertEquals(Double.valueOf(0.5), stock.getDividendYield());
        assertEquals(Double.valueOf(8.0), stock.getPERatio());
    }

    @Test
    public void shouldPostTrade() {
        Collection<Trade> trades = tradeRepository.findAll(forStock("XYZ"));

        List<Double> prices = trades.stream().map(Trade::getPrice).collect(Collectors.toList());
        assertTrue(prices.contains(5.0));
        assertTrue(prices.contains(5.5));
    }

    @Test
    public void shouldGetDividend() {
        ResponseEntity<DividendYieldDto> responseEntity =
                restTemplate.getForEntity("/stocks/dividend?symbol=XYZ", DividendYieldDto.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(Double.valueOf(0.5), responseEntity.getBody().getDividend());
        assertEquals("XYZ", responseEntity.getBody().getStockSymbol());
    }

    @Test
    public void shouldGetPERatio() {
        ResponseEntity<PERatioDto> responseEntity =
                restTemplate.getForEntity("/stocks/peratio?symbol=XYZ", PERatioDto.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(Double.valueOf(8.0), responseEntity.getBody().getPeRatio());
        assertEquals("XYZ", responseEntity.getBody().getStockSymbol());
    }

    @Test
    public void shouldGetStockPrice() {
        ResponseEntity<StockPriceDto> responseEntity =
                restTemplate.getForEntity("/stocks/price?symbol=XYZ", StockPriceDto.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(Double.valueOf(5.375), responseEntity.getBody().getStockPrice());
        assertEquals("XYZ", responseEntity.getBody().getStockSymbol());
    }

    @Test
    public void shouldGetGBCEAllSharesIndex() {
        ResponseEntity<AllSharesIndexDto> responseEntity =
                restTemplate.getForEntity("/stocks/gbceasi", AllSharesIndexDto.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Double index = responseEntity.getBody().getAllSharesIndex();

        // we may test this differently depending on the required precision
        assertEquals(Double.valueOf(5.967359), Double.valueOf(Math.round(index * PRECISION) / PRECISION));
    }

    private void postStock(String symbol, String type, double lastDividend,
                           double fixedDividend, double parValue, double tickerPrice) {
        StockDto stockDto = new StockDto();
        stockDto.setSymbol(symbol);
        stockDto.setType(type);
        stockDto.setLastDividend(lastDividend);
        stockDto.setFixedDividend(fixedDividend);
        stockDto.setParValue(parValue);
        stockDto.setTickerPrice(tickerPrice);

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/stock", stockDto, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    private void postTrade(String symbol, double price, int quantity) {
        TradeDto tradeDto = new TradeDto();
        tradeDto.setStockSymbol(symbol);
        tradeDto.setPrice(price);
        tradeDto.setQuantity(quantity);
        tradeDto.setIndicator("SELL");

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity("/trade", tradeDto, String.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }
}