/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.math.BigDecimal;

public class StatusResponse extends Response {
    private final Status exchangeStatus;
    private final BigDecimal fromAmount;
    private final String fromCoin;
    private final String toCoin;
    private final BigDecimal toAmount;
    private final Address refundAddress;
    private final Address toAddress;
    private final Address fromAddress;
    private final long creationTime;
    private final BigDecimal fromExpectedAmount;
    private final BigDecimal toExpectedAmount;
    private final String txURL;
    private final BigDecimal minValue;
    private final BigDecimal maxValue;

    public StatusResponse(String error) {
        super(error);
        this.exchangeStatus = null;
        this.fromAmount = null;
        this.fromCoin = null;
        this.toCoin = null;
        this.toAmount = null;
        this.refundAddress = null;
        this.toAddress = null;
        this.fromAddress = null;
        this.creationTime = 0;
        this.fromExpectedAmount = null;
        this.toExpectedAmount = null;
        this.txURL = null;
        this.minValue = null;
        this.maxValue = null;
    }

    public StatusResponse(Status exchangeStatus, BigDecimal fromAmount, String fromCoin, String toCoin, BigDecimal toAmount, Address refundAddress, Address toAddress, Address fromAddress, long creationTime, BigDecimal fromExpectedAmount, BigDecimal toExpectedAmount, String txURL, BigDecimal minValue, BigDecimal maxValue) {
        this.exchangeStatus = exchangeStatus;
        this.fromAmount = fromAmount;
        this.fromCoin = fromCoin;
        this.toCoin = toCoin;
        this.toAmount = toAmount;
        this.refundAddress = refundAddress;
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.creationTime = creationTime;
        this.fromExpectedAmount = fromExpectedAmount;
        this.toExpectedAmount = toExpectedAmount;
        this.txURL = txURL;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Status getExchangeStatus() {
        return exchangeStatus;
    }

    public BigDecimal getFromAmount() {
        return fromAmount;
    }

    public String getFromCoin() {
        return fromCoin;
    }

    public String getToCoin() {
        return toCoin;
    }

    public BigDecimal getToAmount() {
        return toAmount;
    }

    public Address getRefundAddress() {
        return refundAddress;
    }

    public Address getToAddress() {
        return toAddress;
    }

    public Address getFromAddress() {
        return fromAddress;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public BigDecimal getFromExpectedAmount() {
        return fromExpectedAmount;
    }

    public BigDecimal getToExpectedAmount() {
        return toExpectedAmount;
    }

    public String getTxURL() {
        return txURL;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }
}