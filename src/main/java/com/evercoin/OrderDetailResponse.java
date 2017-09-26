/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.math.BigDecimal;

public class OrderDetailResponse extends OrderResponse {
    String depositTxId;
    String depositCoin;
    BigDecimal depositAmount;
    String destinationTxId;
    Address destinationAddress;
    String destinationCoin;
    BigDecimal destinationAmount;
    TxStatus status;

    public OrderDetailResponse(String error) {
        super(error);
    }

    public OrderDetailResponse(String orderId, Address depositAddress, String depositTxId, String depositCoin, BigDecimal depositAmount, String destinationTxId, Address destinationAddress, String destinationCoin, BigDecimal destinationAmount, TxStatus status) {
        super(orderId, depositAddress);
        this.depositTxId = depositTxId;
        this.depositCoin = depositCoin;
        this.depositAmount = depositAmount;
        this.destinationTxId = destinationTxId;
        this.destinationAddress = destinationAddress;
        this.destinationCoin = destinationCoin;
        this.destinationAmount = destinationAmount;
        this.status = status;
    }
}
