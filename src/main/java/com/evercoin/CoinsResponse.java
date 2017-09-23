/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.util.ArrayList;
import java.util.List;

public class CoinsResponse extends Response {

    private List<Coin> coinList;

    public CoinsResponse(String error) {
        super(error);
    }

    public CoinsResponse() {
        this.coinList = new ArrayList<>();
    }

    public List<Coin> getCoinList() {
        return coinList;
    }

    public Coin getCoin(String symbol) {
        for (Coin coin : coinList) {
            if (coin.getSymbol().equals(symbol)) {
                return coin;
            }
        }
        return null;
    }
}
