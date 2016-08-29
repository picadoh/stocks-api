package com.hugopicado.stocks.controller.converter;

import com.hugopicado.stocks.controller.api.StockDto;
import com.hugopicado.stocks.domain.Stock;
import com.hugopicado.stocks.domain.StockType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.hugopicado.stocks.makers.StockDtoMaker._ValidStockDto;
import static com.hugopicado.stocks.makers.StockDtoMaker._type;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.testng.Assert.assertEquals;

public class StockDtoConverterTest {

    private StockDtoConverter victim;

    @BeforeMethod
    public void setupScenario() {
        victim = new StockDtoConverter();
    }

    @Test
    public void shouldConvert() {
        StockDto stockDto = make(a(_ValidStockDto));

        Stock stock = victim.apply(stockDto);

        assertEquals(stock.getSymbol(), "XYZ");
        assertEquals(stock.getType(), StockType.COMMON);
        assertEquals(stock.getLastDividend(), 1.0);
        assertEquals(stock.getFixedDividend(), 0.01);
        assertEquals(stock.getParValue(), 1.0);
        assertEquals(stock.getTickerPrice(), 1.0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailWhenTypeIsInvalid() {
        StockDto stockDto = make(a(_ValidStockDto, with(_type, "INVALID")));
        victim.apply(stockDto);
    }

}
