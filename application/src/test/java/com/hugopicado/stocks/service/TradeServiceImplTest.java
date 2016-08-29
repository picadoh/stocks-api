package com.hugopicado.stocks.service;

import com.hugopicado.stocks.domain.Trade;
import com.hugopicado.stocks.repository.GenericRepository;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.hugopicado.stocks.makers.TradeMaker._ValidTrade;
import static com.hugopicado.stocks.makers.TradeMaker._stockSymbol;
import static com.natpryce.makeiteasy.MakeItEasy.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TradeServiceImplTest {

    @Mock
    private GenericRepository<Trade> tradeRepo;

    private TradeServiceImpl victim;

    @BeforeMethod
    public void setupScenario() {
        initMocks(this);
        victim = new TradeServiceImpl(tradeRepo);
    }

    @Test
    public void shouldRecordStock() {
        Trade trade = make(a(_ValidTrade, with(_stockSymbol, "FOO")));
        victim.recordTrade(trade);
        verify(tradeRepo).save(eq(trade));
    }

}
