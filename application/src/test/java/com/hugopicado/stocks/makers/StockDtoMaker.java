package com.hugopicado.stocks.makers;

import com.hugopicado.stocks.controller.api.StockDto;
import com.natpryce.makeiteasy.Instantiator;
import com.natpryce.makeiteasy.Property;

import static com.natpryce.makeiteasy.Property.newProperty;

public class StockDtoMaker {
    public static final Property<StockDto, String> _symbol = newProperty();
    public static final Property<StockDto, String> _type = newProperty();
    public static final Property<StockDto, Double> _lastDividend = newProperty();
    public static final Property<StockDto, Double> _fixedDividend = newProperty();
    public static final Property<StockDto, Double> _parValue = newProperty();
    public static final Property<StockDto, Double> _tickerPrice = newProperty();

    public static final Instantiator<StockDto> _ValidStockDto = lookup -> {
        StockDto stockDto = new StockDto();

        stockDto.setSymbol(lookup.valueOf(_symbol, "XYZ"));
        stockDto.setType(lookup.valueOf(_type, "COMMON"));
        stockDto.setLastDividend(lookup.valueOf(_lastDividend, 1.0));
        stockDto.setFixedDividend(lookup.valueOf(_fixedDividend, 0.01));
        stockDto.setParValue(lookup.valueOf(_parValue, 1.0));
        stockDto.setTickerPrice(lookup.valueOf(_tickerPrice, 1.0));

        return stockDto;
    };

}