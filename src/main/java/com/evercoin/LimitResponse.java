/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

public class LimitResponse extends Response {
    private final String depositCoin;
    private final String destinationCoin;
    private final String maxDeposit;
    private final String minDeposit;

    public LimitResponse(String error) {
        super(error);
        this.depositCoin = null;
        this.destinationCoin = null;
        this.maxDeposit = null;
        this.minDeposit = null;
    }

    public LimitResponse(String depositCoin, String destinationCoin, String maxDeposit, String minDeposit) {
        this.depositCoin = depositCoin;
        this.destinationCoin = destinationCoin;
        this.maxDeposit = maxDeposit;
        this.minDeposit = minDeposit;
    }

    public String getDepositCoin() {
        return depositCoin;
    }

    public String getDestinationCoin() {
        return destinationCoin;
    }

    public String getMaxDeposit() {
        return maxDeposit;
    }

    public String getMinDeposit() {
        return minDeposit;
    }
}
