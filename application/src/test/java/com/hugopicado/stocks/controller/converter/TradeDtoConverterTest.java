package com.hugopicado.stocks.controller.converter;

import com.hugopicado.stocks.controller.api.TradeDto;
import com.hugopicado.stocks.domain.Trade;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.hugopicado.stocks.domain.TradeIndicator.BUY;
import static com.hugopicado.stocks.makers.TradeDtoMaker._ValidTradeDto;
import static com.hugopicado.stocks.makers.TradeDtoMaker._tradeIndicator;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class TradeDtoConverterTest {

    private TradeDtoConverter victim;

    @BeforeMethod
    public void setupScenario() {
        victim = new TradeDtoConverter();
    }

    @Test
    public void shouldConvert() {
        TradeDto tradeDto = make(a(_ValidTradeDto));

        Trade trade = victim.apply(tradeDto);

        assertNotNull(trade.getTime());
        assertEquals(trade.getQuantity(), Integer.valueOf(1));
        assertEquals(trade.getPrice(), 1.0);
        assertEquals(trade.getStockSymbol(), "XYZ");
        assertEquals(trade.getIndicator(), BUY);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenConversionFails() {
        TradeDto tradeDto = make(a(_ValidTradeDto, with(_tradeIndicator, "XPTO")));
        victim.apply(tradeDto);
    }

}
