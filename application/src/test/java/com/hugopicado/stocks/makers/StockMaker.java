package com.hugopicado.stocks.makers;

import com.hugopicado.stocks.domain.Stock;
import com.hugopicado.stocks.domain.StockType;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import static com.natpryce.makeiteasy.Property.newProperty;

public class StockMaker {
    public static final Property<Stock, String> _symbol = newProperty();
    public static final Property<Stock, StockType> _type = newProperty();
    public static final Property<Stock, Double> _lastDividend = newProperty();
    public static final Property<Stock, Double> _fixedDividend = newProperty();
    public static final Property<Stock, Double> _parValue = newProperty();
    public static final Property<Stock, Double> _tickerPrice = newProperty();

    public static final Instantiator<Stock> _ValidStock = lookup -> new Stock(
            lookup.valueOf(_symbol, "XYZ"),
            lookup.valueOf(_type, StockType.COMMON),
            lookup.valueOf(_lastDividend, 1.0),
            lookup.valueOf(_fixedDividend, 0.01),
            lookup.valueOf(_parValue, 1.0),
            lookup.valueOf(_tickerPrice, 1.0)
    );
}