/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.math.BigDecimal;

public class PriceResponse extends Response {
    private final String fromCoin;
    private final String toCoin;
    private final BigDecimal fromAmount;
    private final BigDecimal toAmount;
    private final String signature;

    public PriceResponse(String error) {
        super(error);
        this.fromCoin = null;
        this.toCoin = null;
        this.fromAmount = null;
        this.toAmount = null;
        this.signature = null;
    }

    public PriceResponse(String fromCoin, String toCoin, BigDecimal fromAmount, BigDecimal toAmount, String signature) {
        this.fromCoin = fromCoin;
        this.toCoin = toCoin;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.signature = signature;
    }

    public String getFromCoin() {
        return fromCoin;
    }

    public String getToCoin() {
        return toCoin;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public String getSignature() {
        return signature;
    }
}
