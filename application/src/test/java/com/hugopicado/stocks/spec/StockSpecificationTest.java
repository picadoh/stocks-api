package com.hugopicado.stocks.spec;

import com.hugopicado.stocks.domain.Stock;
import org.testng.annotations.Test;

import static com.hugopicado.stocks.makers.StockMaker._ValidStock;
import static com.hugopicado.stocks.makers.StockMaker._symbol;
import static com.hugopicado.stocks.spec.StockSpecification.withSymbol;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class StockSpecificationTest {

    @Test
    public void shouldValidateSymbol() {
        Stock stock1 = make(a(_ValidStock, with(_symbol, "FOO")));
        Stock stock2 = make(a(_ValidStock, with(_symbol, "BAR")));

        assertTrue(withSymbol("FOO").test(stock1));
        assertFalse(withSymbol("FOO").test(stock2));
    }

}
