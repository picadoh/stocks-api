package com.hugopicado.stocks.controller.api;

import com.google.common.base.MoreObjects;

public class StockDto {

    private String symbol;
    private String type;
    private double lastDividend;
    private double fixedDividend;
    private double parValue;
    private double tickerPrice;

    public String getSymbol() {
        return symbol;
    }

    public String getType() {
        return type;
    }

    public double getLastDividend() {
        return lastDividend;
    }

    public double getFixedDividend() {
        return fixedDividend;
    }

    public double getParValue() {
        return parValue;
    }

    public double getTickerPrice() {
        return tickerPrice;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLastDividend(double lastDividend) {
        this.lastDividend = lastDividend;
    }

    public void setFixedDividend(double fixedDividend) {
        this.fixedDividend = fixedDividend;
    }

    public void setParValue(double parValue) {
        this.parValue = parValue;
    }

    public void setTickerPrice(double tickerPrice) {
        this.tickerPrice = tickerPrice;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("symbol", symbol)
                .add("type", type)
                .add("lastDividend", lastDividend)
                .add("fixedDividend", fixedDividend)
                .add("parValue", parValue)
                .add("tickerPrice", tickerPrice)
                .toString();
    }
}
