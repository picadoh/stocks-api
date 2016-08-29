package com.hugopicado.stocks.repository;

import com.hugopicado.stocks.domain.Stock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.function.Predicate;

import static com.hugopicado.stocks.makers.StockMaker._ValidStock;
import static com.hugopicado.stocks.makers.StockMaker._symbol;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class LocalStockRepositoryTest {

    private LocalStockRepository victim;

    @BeforeMethod
    public void setupScenario() {
        victim = new LocalStockRepository();
    }

    @Test
    public void shouldSaveDataToRepository() {
        Stock stock1 = make(a(_ValidStock, with(_symbol, "FOO")));
        Stock stock2 = make(a(_ValidStock, with(_symbol, "BAR")));

        victim.save(stock1);
        victim.save(stock2);

        Collection all = victim.findAll();

        assertEquals(all.size(), 2);
        assertTrue(all.contains(stock1));
        assertTrue(all.contains(stock2));
    }

    @Test
    public void shouldFindOneInRepositoryFromSpec() {
        Stock stock1 = make(a(_ValidStock, with(_symbol, "FOO")));
        Stock stock2 = make(a(_ValidStock, with(_symbol, "BAR")));

        victim.save(stock1);
        victim.save(stock2);

        Predicate<Stock> stockSpecification = mock(Predicate.class);
        when(stockSpecification.test(eq(stock1))).thenReturn(false);
        when(stockSpecification.test(eq(stock2))).thenReturn(true);

        Stock found = victim.findOne(stockSpecification);

        assertNotNull(found);
        assertTrue(found.equals(stock2));
    }

    @Test
    public void shouldFindAllInRepositoryFromSpec() {
        Stock stock1 = make(a(_ValidStock, with(_symbol, "FOO")));
        Stock stock2 = make(a(_ValidStock, with(_symbol, "BAR")));
        Stock stock3 = make(a(_ValidStock, with(_symbol, "XYZ")));

        victim.save(stock1);
        victim.save(stock2);
        victim.save(stock3);

        Predicate<Stock> spec = mock(Predicate.class);
        when(spec.test(eq(stock1))).thenReturn(true);
        when(spec.test(eq(stock2))).thenReturn(false);
        when(spec.test(eq(stock3))).thenReturn(true);

        Collection<Stock> found = victim.findAll(spec);

        assertNotNull(found);
        assertEquals(found.size(), 2);
        assertTrue(found.contains(stock1));
        assertFalse(found.contains(stock2));
        assertTrue(found.contains(stock3));
    }

    @Test
    public void shouldDeleteFromRepository() {
        Stock stock1 = make(a(_ValidStock, with(_symbol, "FOO")));
        Stock stock2 = make(a(_ValidStock, with(_symbol, "BAR")));

        victim.save(stock1);
        victim.save(stock2);

        Collection all = victim.findAll();

        assertEquals(all.size(), 2);
        assertTrue(all.contains(stock1));
        assertTrue(all.contains(stock2));

        victim.delete(stock1);

        assertEquals(all.size(), 1);
        assertFalse(all.contains(stock1));
        assertTrue(all.contains(stock2));
    }

}
