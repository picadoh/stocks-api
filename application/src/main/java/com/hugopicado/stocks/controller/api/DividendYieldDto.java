package com.hugopicado.stocks.controller.api;

public class DividendYieldDto {

    private String stockSymbol;
    private Double dividend;

    public String getStockSymbol() {
        return stockSymbol;
    }

    public Double getDividend() {
        return dividend;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public void setDividend(Double dividend) {
        this.dividend = dividend;
    }
}
