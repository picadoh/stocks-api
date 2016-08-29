package com.hugopicado.stocks.controller.api;

public class StockPriceDto {

    private String stockSymbol;
    private Double stockPrice;

    public String getStockSymbol() {
        return stockSymbol;
    }

    public Double getStockPrice() {
        return stockPrice;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public void setStockPrice(Double stockPrice) {
        this.stockPrice = stockPrice;
    }
}
