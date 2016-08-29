package com.hugopicado.stocks.domain;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.hugopicado.stocks.makers.TradeMaker.*;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.testng.Assert.assertEquals;

public class TradeTest {

    private Trade victim;

    @BeforeMethod
    public void setupScenario() {
        victim = make(a(_ValidTrade,
                with(_stockSymbol, "XYZ"),
                with(_price, 1.0),
                with(_quantity, 2),
                with(_time, 123L),
                with(_tradeIndicator, TradeIndicator.BUY)
        ));
    }

    @Test
    public void shouldGetState() {
        assertEquals(victim.getStockSymbol(), "XYZ");
        assertEquals(victim.getPrice(), 1.0);
        assertEquals(victim.getQuantity(), Integer.valueOf(2));
        assertEquals(victim.getTime(), Long.valueOf(123));
        assertEquals(victim.getIndicator(), TradeIndicator.BUY);
    }

}
