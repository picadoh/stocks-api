package com.hugopicado.stocks.makers;

import com.hugopicado.stocks.controller.api.TradeDto;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import static com.natpryce.makeiteasy.Property.newProperty;

public class TradeDtoMaker {
    public static final Property<TradeDto, Double> _price = newProperty();
    public static final Property<TradeDto, Integer> _quantity = newProperty();
    public static final Property<TradeDto, String> _stockSymbol = newProperty();
    public static final Property<TradeDto, String> _tradeIndicator = newProperty();

    public static final Instantiator<TradeDto> _ValidTradeDto = lookup -> {
        TradeDto tradeDto = new TradeDto();
        tradeDto.setStockSymbol(lookup.valueOf(_stockSymbol, "XYZ"));
        tradeDto.setPrice(lookup.valueOf(_price, 1.0));
        tradeDto.setQuantity(lookup.valueOf(_quantity, 1));
        tradeDto.setIndicator(lookup.valueOf(_tradeIndicator, "BUY"));
        return tradeDto;
    };
}