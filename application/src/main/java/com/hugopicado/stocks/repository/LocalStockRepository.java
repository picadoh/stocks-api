package com.hugopicado.stocks.repository;

import com.hugopicado.stocks.domain.Stock;
import org.springframework.stereotype.Repository;

/**
 * Local repository for stocks.
 *
 * @see Stock
 * @see LocalRepository
 */
@Repository
public class LocalStockRepository extends LocalRepository<Stock> {

    public LocalStockRepository() {
        super();
    }

}
