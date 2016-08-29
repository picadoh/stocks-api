package com.hugopicado.stocks.makers;

import com.hugopicado.stocks.domain.Trade;
import com.hugopicado.stocks.domain.TradeIndicator;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;
import org.joda.time.DateTime;

import static com.natpryce.makeiteasy.Property.newProperty;

public class TradeMaker {
    public static final Property<Trade, Long> _time = newProperty();
    public static final Property<Trade, Double> _price = newProperty();
    public static final Property<Trade, Integer> _quantity = newProperty();
    public static final Property<Trade, String> _stockSymbol = newProperty();
    public static final Property<Trade, TradeIndicator> _tradeIndicator = newProperty();

    public static final Instantiator<Trade> _ValidTrade = lookup -> new Trade(
            lookup.valueOf(_time, new DateTime().getMillis()),
            lookup.valueOf(_price, 1.0),
            lookup.valueOf(_quantity, 1),
            lookup.valueOf(_stockSymbol, "XYZ"),
            lookup.valueOf(_tradeIndicator, TradeIndicator.BUY));
}