/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.math.BigDecimal;

public class StatusResponse extends Response {
    private final Status exchangeStatus;
    private final BigDecimal depositAmount;
    private final String depositCoin;
    private final String destinationCoin;
    private final BigDecimal destinationAmount;
    private final Address refundAddress;
    private final Address destinationAddress;
    private final Address depositAddress;
    private final long creationTime;
    private final BigDecimal depositExpectedAmount;
    private final BigDecimal destinationExpectedAmount;
    private final String txURL;
    private final BigDecimal minDeposit;
    private final BigDecimal maxDeposit;

    public StatusResponse(String error) {
        super(error);
        this.exchangeStatus = null;
        this.depositAmount = null;
        this.depositCoin = null;
        this.destinationCoin = null;
        this.destinationAmount = null;
        this.refundAddress = null;
        this.destinationAddress = null;
        this.depositAddress = null;
        this.creationTime = 0;
        this.depositExpectedAmount = null;
        this.destinationExpectedAmount = null;
        this.txURL = null;
        this.minDeposit = null;
        this.maxDeposit = null;
    }

    public StatusResponse(Status exchangeStatus, BigDecimal depositAmount, String depositCoin, String destinationCoin, BigDecimal destinationAmount, Address refundAddress, Address destinationAddress, Address depositAddress, long creationTime, BigDecimal depositExpectedAmount, BigDecimal destinationExpectedAmount, String txURL, BigDecimal minDeposit, BigDecimal maxDeposit) {
        this.exchangeStatus = exchangeStatus;
        this.depositAmount = depositAmount;
        this.depositCoin = depositCoin;
        this.destinationCoin = destinationCoin;
        this.destinationAmount = destinationAmount;
        this.refundAddress = refundAddress;
        this.destinationAddress = destinationAddress;
        this.depositAddress = depositAddress;
        this.creationTime = creationTime;
        this.depositExpectedAmount = depositExpectedAmount;
        this.destinationExpectedAmount = destinationExpectedAmount;
        this.txURL = txURL;
        this.minDeposit = minDeposit;
        this.maxDeposit = maxDeposit;
    }

    public Status getExchangeStatus() {
        return exchangeStatus;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public String getDepositCoin() {
        return depositCoin;
    }

    public String getDestinationCoin() {
        return destinationCoin;
    }

    public BigDecimal getDestinationAmount() {
        return destinationAmount;
    }

    public Address getRefundAddress() {
        return refundAddress;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public Address getDepositAddress() {
        return depositAddress;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public BigDecimal getDepositExpectedAmount() {
        return depositExpectedAmount;
    }

    public BigDecimal getDestinationExpectedAmount() {
        return destinationExpectedAmount;
    }

    public String getTxURL() {
        return txURL;
    }

    public BigDecimal getMinDeposit() {
        return minDeposit;
    }

    public BigDecimal getMaxDeposit() {
        return maxDeposit;
    }
}