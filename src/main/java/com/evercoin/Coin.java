package com.evercoin;

public class Coin {
    private final String name;
    private final String symbol;
    private final boolean from;
    private final boolean to;

    Coin(String name, String symbol, boolean from, boolean to) {
        this.name = name;
        this.symbol = symbol;
        this.from = from;
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isFrom() {
        return from;
    }

    public boolean isTo() {
        return to;
    }
}
