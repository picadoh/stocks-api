package com.hugopicado.stocks.domain;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.hugopicado.stocks.makers.StockMaker.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertEquals;

public class StockTest {

    private Stock victim;

    @BeforeMethod
    public void setupScenario() {
        victim = make(a(_ValidStock,
                with(_symbol, "XYZ"),
                with(_type, StockType.COMMON),
                with(_lastDividend, 1.0),
                with(_fixedDividend, 0.02),
                with(_parValue, 3.0),
                with(_tickerPrice, 4.0)
        ));
    }

    @Test
    public void shouldGetState() {
        assertEquals(victim.getSymbol(), "XYZ");
        assertEquals(victim.getLastDividend(), 1.0);
        assertEquals(victim.getFixedDividend(), 0.02);
        assertEquals(victim.getParValue(), 3.0);
        assertEquals(victim.getTickerPrice(), 4.0);
    }

    @Test
    public void shouldCalculateCommonDividendYield() {
        assertEquals(victim.getDividendYield(), 1.0 / 4.0);
    }

    @Test
    public void shouldCalculatePreferredDividendYield() {
        Stock victim = make(a(_ValidStock,
                with(_symbol, "XYZ"),
                with(_type, StockType.PREFERRED),
                with(_lastDividend, 1.0),
                with(_fixedDividend, 0.02),
                with(_parValue, 3.0),
                with(_tickerPrice, 4.0)
        ));

        assertEquals(victim.getDividendYield(), 0.02 / 4.0);
    }

    @Test
    public void shouldCalculatePERatio() {

        Stock spied = spy(victim);
        doReturn(10.0).when(spied).getDividendYield();

        assertEquals(spied.getPERatio(), 4.0 / 10.0);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailWithNullSymbol() {
        make(a(_ValidStock,
                with(_symbol, (String) null)
        ));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailWithEmptySymbol() {
        make(a(_ValidStock,
                with(_symbol, "")
        ));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailWithNullType() {
        make(a(_ValidStock,
                with(_type, (StockType) null)
        ));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailWithNegativeLastDividend() {
        make(a(_ValidStock,
                with(_lastDividend, -1.0)
        ));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailWithNegativeTickerPrice() {
        make(a(_ValidStock,
                with(_tickerPrice, -1.0)
        ));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailWithNegativeFixedDividend() {
        make(a(_ValidStock,
                with(_fixedDividend, -1.0)
        ));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailWithFixedDividendGreaterThanUnit() {
        make(a(_ValidStock,
                with(_fixedDividend, 1.1)
        ));
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldFailWithNegativeParValue() {
        make(a(_ValidStock,
                with(_parValue, -1.0)
        ));
    }

}
