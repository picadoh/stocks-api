package com.hugopicado.stocks.spec;

import com.hugopicado.stocks.domain.Trade;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.testng.annotations.Test;

import static com.hugopicado.stocks.makers.TradeMaker.*;
import static com.hugopicado.stocks.spec.TradeSpecification.forStock;
import static com.hugopicado.stocks.spec.TradeSpecification.within;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TradeSpecificationTest {

    @Test
    public void shouldValidateStockSymbol() {
        Trade trade1 = make(a(_ValidTrade, with(_stockSymbol, "FOO")));
        Trade trade2 = make(a(_ValidTrade, with(_stockSymbol, "BAR")));

        assertTrue(forStock("FOO").test(trade1));
        assertFalse(forStock("FOO").test(trade2));
    }

    @Test
    public void shouldValidateDateInterval() {
        DateTime now = new DateTime(DateTimeZone.UTC);

        Trade trade1 = make(a(_ValidTrade, with(_stockSymbol, "FOO"), with(_time, now.minusMinutes(1).getMillis())));
        Trade trade2 = make(a(_ValidTrade, with(_stockSymbol, "BAR"), with(_time, now.minusMinutes(15).getMillis())));

        assertTrue(within(now.minusMinutes(10), now).test(trade1));
        assertFalse(within(now.minusMinutes(10), now).test(trade2));
        assertTrue(within(now.minusMinutes(20), now).test(trade2));
    }

    @Test
    public void shouldReturnTrueIfDateIntervalIsZero() {
        DateTime now = new DateTime(DateTimeZone.UTC);

        Trade trade1 = make(a(_ValidTrade, with(_stockSymbol, "FOO"), with(_time, now.minusMinutes(1).getMillis())));

        assertTrue(within(now.minusMinutes(0), now).test(trade1));
    }

}
