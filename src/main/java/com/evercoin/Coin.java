package com.evercoin;

public class Coin {
    private final String name;
    private final String symbol;
    private final boolean fromAvailable;
    private final boolean toAvailable;

    Coin(String name, String symbol, boolean fromAvailable, boolean toAvailable) {
        this.name = name;
        this.symbol = symbol;
        this.fromAvailable = fromAvailable;
        this.toAvailable = toAvailable;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isFromAvailable() {
        return fromAvailable;
    }

    public boolean isToAvailable() {
        return toAvailable;
    }
}
