/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.math.BigDecimal;

public interface Evercoin {
    LimitResponse getLimit(final String from, final String to);

    ValidateResponse validateAddress(final String coin, final String address);

    CoinsResponse getCoins();

    PriceResponse getPrice(String fromCoin, String toCoin, BigDecimal fromAmount, BigDecimal toAmount);

    OrderResponse createOrder(PriceResponse priceResponse, Address refundAddress, Address toAddress);

    StatusResponse getStatus(String orderId);
}
