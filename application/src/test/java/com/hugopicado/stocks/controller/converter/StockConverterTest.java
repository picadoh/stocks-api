package com.hugopicado.stocks.controller.converter;

import com.hugopicado.stocks.controller.api.StockDto;
import com.hugopicado.stocks.domain.Stock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.hugopicado.stocks.makers.StockMaker._ValidStock;
import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.testng.Assert.assertEquals;

public class StockConverterTest {

    private StockConverter victim;

    @BeforeMethod
    public void setupScenario() {
        victim = new StockConverter();
    }

    @Test
    public void shouldConvert() {
        Stock stock = make(a(_ValidStock));

        StockDto stockDto = victim.apply(stock);

        assertEquals(stockDto.getSymbol(), "XYZ");
        assertEquals(stockDto.getType(), "COMMON");
        assertEquals(stockDto.getLastDividend(), 1.0);
        assertEquals(stockDto.getFixedDividend(), 0.01);
        assertEquals(stockDto.getParValue(), 1.0);
        assertEquals(stockDto.getTickerPrice(), 1.0);
    }

}
