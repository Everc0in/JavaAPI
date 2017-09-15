/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

public class CoinResponse extends Response {
    private final String name;
    private final String symbol;
    private final boolean from;
    private final boolean to;

    public CoinResponse(String error) {
        super(error);
        this.name = null;
        this.symbol = null;
        this.from = false;
        this.to = false;
    }

    public CoinResponse(String name, String symbol, boolean from, boolean to) {
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
