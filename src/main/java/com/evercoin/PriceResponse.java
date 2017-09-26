/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.math.BigDecimal;

public class PriceResponse extends Response {
    private final String depositCoin;
    private final String destinationCoin;
    private final BigDecimal depositAmount;
    private final BigDecimal destinationAmount;
    private final String signature;

    public PriceResponse(String error) {
        super(error);
        this.depositCoin = null;
        this.destinationCoin = null;
        this.depositAmount = null;
        this.destinationAmount = null;
        this.signature = null;
    }

    public PriceResponse(String depositCoin, String destinationCoin, BigDecimal depositAmount, BigDecimal destinationAmount, String signature) {
        this.depositCoin = depositCoin;
        this.destinationCoin = destinationCoin;
        this.depositAmount = depositAmount;
        this.destinationAmount = destinationAmount;
        this.signature = signature;
    }

    public String getDepositCoin() {
        return depositCoin;
    }

    public String getDestinationCoin() {
        return destinationCoin;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public BigDecimal getDestinationAmount() {
        return destinationAmount;
    }

    public String getSignature() {
        return signature;
    }
}
