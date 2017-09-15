/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

public class OrderResponse extends Response {
    private final String orderId;
    private final Address depositAddress;

    public OrderResponse(String error) {
        super(error);
        this.orderId = null;
        this.depositAddress = null;
    }

    public OrderResponse(String orderId, Address depositAddress) {
        this.orderId = orderId;
        this.depositAddress = depositAddress;
    }

    public String getOrderId() {
        return orderId;
    }

    public Address getDepositAddress() {
        return depositAddress;
    }
}
