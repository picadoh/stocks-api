package com.hugopicado.stocks.controller;

import com.hugopicado.stocks.controller.api.*;
import com.hugopicado.stocks.controller.converter.StockConverter;
import com.hugopicado.stocks.controller.converter.StockDtoConverter;
import com.hugopicado.stocks.domain.Stock;
import com.hugopicado.stocks.makers.StockDtoMaker;
import com.hugopicado.stocks.service.StockService;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.hugopicado.stocks.makers.StockDtoMaker._ValidStockDto;
import static com.hugopicado.stocks.makers.StockDtoMaker._type;
import static com.hugopicado.stocks.makers.StockMaker._ValidStock;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;

public class StockControllerTest {

    @Mock
    private StockService stockService;

    @Mock
    private StockDtoConverter stockDtoConverter;

    @Mock
    private StockConverter stockConverter;


    private StockController victim;

    @BeforeMethod
    public void setupScenario() {
        initMocks(this);
        victim = new StockController(stockService, stockDtoConverter, stockConverter);
    }

    @Test
    public void shouldPostStock() {
        StockDto stockDto = make(a(_ValidStockDto));
        Stock stock = make(a(_ValidStock));

        when(stockDtoConverter.apply(stockDto)).thenReturn(stock);
        doNothing().when(stockService).recordStock(stock);

        victim.postStock(stockDto);

        verify(stockDtoConverter).apply(stockDto);
        verify(stockService).recordStock(stock);
    }

    @Test
    public void shouldReturnBadRequestResponseWhenMandatoryFieldsNotPresent() {
        String type = null;
        String symbol = null;
        StockDto stockDto = make(a(_ValidStockDto,
                with(_type, type), with(StockDtoMaker._symbol, symbol)));

        ResponseEntity<String> response = victim.postStock(stockDto);
        assertEquals(response, ResponseEntity.badRequest().body("mandatory field not received"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPostingStockAndServiceThrowsException() {
        StockDto stockDto = make(a(_ValidStockDto));
        Stock stock = make(a(_ValidStock));

        when(stockDtoConverter.apply(stockDto)).thenReturn(stock);
        doThrow(new IllegalArgumentException()).when(stockService).recordStock(any(Stock.class));

        victim.postStock(make(a(_ValidStockDto)));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPostingStockAndConverterThrowsException() {
        when(stockDtoConverter.apply(any(StockDto.class))).thenThrow(new IllegalArgumentException());
        victim.postStock(make(a(_ValidStockDto)));
    }


    @Test
    public void shouldGetStock() {
        Stock stock = make(a(_ValidStock));
        StockDto stockDto = make(a(_ValidStockDto));

        when(stockService.retrieveStock(anyString())).thenReturn(stock);
        when(stockConverter.apply(eq(stock))).thenReturn(stockDto);

        StockDto retrievedStockDto = victim.getStock("FOO");

        assertEquals(retrievedStockDto.getTickerPrice(), stockDto.getTickerPrice());
        assertEquals(retrievedStockDto.getLastDividend(), stockDto.getLastDividend());
        assertEquals(retrievedStockDto.getFixedDividend(), stockDto.getFixedDividend());
        assertEquals(retrievedStockDto.getParValue(), stockDto.getParValue());
        assertEquals(retrievedStockDto.getType(), stockDto.getType());
        assertEquals(retrievedStockDto.getSymbol(), stockDto.getSymbol());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettingStockAndServiceThrowsException() {
        when(stockService.retrieveStock(anyString())).thenThrow(new IllegalArgumentException());
        victim.getStock("FOO");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettingStockAndConverterServiceThrowsException() {
        Stock stock = make(a(_ValidStock));

        when(stockService.retrieveStock(anyString())).thenReturn(stock);
        when(stockConverter.apply(eq(stock))).thenThrow(new IllegalArgumentException());
        victim.getStock("FOO");
    }

    @Test
    public void shouldGetDividend() {
        when(stockService.calculateDividendYield(anyString())).thenReturn(99.);

        DividendYieldDto dividendYieldDto = victim.getDividend("FOO");

        assertEquals(dividendYieldDto.getStockSymbol(), "FOO");
        assertEquals(dividendYieldDto.getDividend(), 99.);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettingDividendAndServiceThrowsException() {
        when(stockService.calculateDividendYield(anyString())).thenThrow(new IllegalArgumentException());
        victim.getDividend("FOO");
    }

    @Test
    public void shouldGetPERatio() {
        when(stockService.calculatePERatio(anyString())).thenReturn(99.);

        PERatioDto peRatioDto = victim.getPERatio("FOO");

        assertEquals(peRatioDto.getStockSymbol(), "FOO");
        assertEquals(peRatioDto.getPeRatio(), 99.);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettingPERatioAndServiceThrowsException() {
        when(stockService.calculatePERatio(anyString())).thenThrow(new IllegalArgumentException());
        victim.getPERatio("FOO");
    }

    @Test
    public void shouldGetStockPrice() {
        when(stockService.calculateStockPrice(anyString())).thenReturn(99.);

        StockPriceDto stockPriceDto = victim.getStockPrice("FOO");

        assertEquals(stockPriceDto.getStockSymbol(), "FOO");
        assertEquals(stockPriceDto.getStockPrice(), 99.);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettingStockPriceAndServiceThrowsException() {
        when(stockService.calculateStockPrice(anyString())).thenThrow(new IllegalArgumentException());
        victim.getStockPrice("FOO");
    }

    @Test
    public void shouldGetGBCEAllSharesIndex() {
        when(stockService.calculateGBCEAllSharesIndex()).thenReturn(99.);

        AllSharesIndexDto allSharesIndexDto = victim.getGBCEAllSharesIndex();

        assertEquals(allSharesIndexDto.getAllSharesIndex(), 99.);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenGettingAllSharesIndexAndServiceThrowsException() {
        when(stockService.calculateGBCEAllSharesIndex()).thenThrow(new IllegalArgumentException());
        victim.getGBCEAllSharesIndex();
    }

}
