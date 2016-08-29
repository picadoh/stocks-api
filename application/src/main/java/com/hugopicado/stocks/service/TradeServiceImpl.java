package com.hugopicado.stocks.service;

import com.hugopicado.stocks.domain.Trade;
import com.hugopicado.stocks.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeServiceImpl implements TradeService {
    private final GenericRepository<Trade> tradeRepository;

    @Autowired
    public TradeServiceImpl(GenericRepository<Trade> tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Override
    public void recordTrade(Trade trade) {
        tradeRepository.save(trade);
    }
}
