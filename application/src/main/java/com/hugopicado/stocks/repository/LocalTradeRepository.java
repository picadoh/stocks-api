package com.hugopicado.stocks.repository;

import com.hugopicado.stocks.domain.Trade;
import org.springframework.stereotype.Repository;

/**
 * Local repository for trades.
 *
 * @see Trade
 * @see LocalRepository
 */
@Repository
public class LocalTradeRepository extends LocalRepository<Trade> {

    public LocalTradeRepository() {
        super();
    }

}
