package com.hugopicado.stocks.repository;

import com.hugopicado.stocks.domain.Trade;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.function.Predicate;

import static com.hugopicado.stocks.makers.TradeMaker._ValidTrade;
import static com.hugopicado.stocks.makers.TradeMaker._stockSymbol;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class LocalTradeRepositoryTest {

    private LocalTradeRepository victim;

    @BeforeMethod
    public void setupScenario() {
        victim = new LocalTradeRepository();
    }

    @Test
    public void shouldSaveDataToRepository() {
        Trade trade1 = make(a(_ValidTrade, with(_stockSymbol, "FOO")));
        Trade trade2 = make(a(_ValidTrade, with(_stockSymbol, "BAR")));

        victim.save(trade1);
        victim.save(trade2);

        Collection all = victim.findAll();

        assertEquals(all.size(), 2);
        assertTrue(all.contains(trade1));
        assertTrue(all.contains(trade2));
    }

    @Test
    public void shouldFindOneInRepositoryFromSpec() {
        Trade trade1 = make(a(_ValidTrade, with(_stockSymbol, "FOO")));
        Trade trade2 = make(a(_ValidTrade, with(_stockSymbol, "BAR")));

        victim.save(trade1);
        victim.save(trade2);

        Predicate<Trade> spec = mock(Predicate.class);
        when(spec.test(eq(trade1))).thenReturn(false);
        when(spec.test(eq(trade2))).thenReturn(true);

        Trade found = victim.findOne(spec);

        assertNotNull(found);
        assertTrue(found.equals(trade2));
    }

    @Test
    public void shouldFindAllInRepositoryFromSpec() {
        Trade trade1 = make(a(_ValidTrade, with(_stockSymbol, "FOO")));
        Trade trade2 = make(a(_ValidTrade, with(_stockSymbol, "BAR")));
        Trade trade3 = make(a(_ValidTrade, with(_stockSymbol, "XYZ")));

        victim.save(trade1);
        victim.save(trade2);
        victim.save(trade3);

        Predicate<Trade> spec = mock(Predicate.class);
        when(spec.test(eq(trade1))).thenReturn(true);
        when(spec.test(eq(trade2))).thenReturn(false);
        when(spec.test(eq(trade3))).thenReturn(true);

        Collection<Trade> found = victim.findAll(spec);

        assertNotNull(found);
        assertEquals(found.size(), 2);
        assertTrue(found.contains(trade1));
        assertFalse(found.contains(trade2));
        assertTrue(found.contains(trade3));
    }

    @Test
    public void shouldDeleteFromRepository() {
        Trade trade1 = make(a(_ValidTrade, with(_stockSymbol, "FOO")));
        Trade trade2 = make(a(_ValidTrade, with(_stockSymbol, "BAR")));

        victim.save(trade1);
        victim.save(trade2);

        Collection all = victim.findAll();

        assertEquals(all.size(), 2);
        assertTrue(all.contains(trade1));
        assertTrue(all.contains(trade2));

        victim.delete(trade1);

        assertEquals(all.size(), 1);
        assertFalse(all.contains(trade1));
        assertTrue(all.contains(trade2));
    }

}
