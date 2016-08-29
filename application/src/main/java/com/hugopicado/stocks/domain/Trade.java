package com.hugopicado.stocks.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Trade {

    private final long time;
    private final double price;
    private final int quantity;
    private final String stockSymbol;
    private final TradeIndicator indicator;

    public Trade(long time, double price, int quantity, String stockSymbol, TradeIndicator indicator) {
        this.time = time;
        this.price = price;
        this.quantity = quantity;
        this.stockSymbol = stockSymbol;
        this.indicator = indicator;
    }

    public Long getTime() {
        return time;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public TradeIndicator getIndicator() {
        return indicator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Trade trade = (Trade) o;
        return time == trade.time &&
                Double.compare(trade.price, price) == 0 &&
                quantity == trade.quantity &&
                Objects.equal(stockSymbol, trade.stockSymbol) &&
                Objects.equal(indicator, trade.indicator);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(time, price, quantity, stockSymbol, indicator);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("time", time)
                .add("price", price)
                .add("quantity", quantity)
                .add("stockSymbol", stockSymbol)
                .add("indicator", indicator)
                .toString();
    }
}
