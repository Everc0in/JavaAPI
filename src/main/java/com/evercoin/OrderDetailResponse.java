/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.math.BigDecimal;

public class OrderDetailResponse extends OrderResponse {
    String depositTxId;
    String depositCoin;
    BigDecimal depositAmount;
    String withdrawTxId;
    Address withdrawAddress;
    String withdrawCoin;
    BigDecimal withdrawAmount;
    TxStatus status;

    public OrderDetailResponse(String error) {
        super(error);
    }

    public OrderDetailResponse(String orderId, String depositTxId, Address depositAddress, String depositCoin, BigDecimal depositAmount, String withdrawTxId, Address withdrawAddress, String withdrawCoin, BigDecimal withdrawAmount, TxStatus status) {
        super(orderId, depositAddress);
        this.depositTxId = depositTxId;
        this.depositCoin = depositCoin;
        this.depositAmount = depositAmount;
        this.withdrawTxId = withdrawTxId;
        this.withdrawAddress = withdrawAddress;
        this.withdrawCoin = withdrawCoin;
        this.withdrawAmount = withdrawAmount;
        this.status = status;
    }
}
