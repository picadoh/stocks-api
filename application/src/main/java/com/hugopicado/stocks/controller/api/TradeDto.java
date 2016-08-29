package com.hugopicado.stocks.controller.api;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class TradeDto {

    private double price;
    private int quantity;
    private String stockSymbol;
    private String indicator;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("price", price)
                .add("quantity", quantity)
                .add("stockSymbol", stockSymbol)
                .add("indicator", indicator)
                .toString();
    }
}
