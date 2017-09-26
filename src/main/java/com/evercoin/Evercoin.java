/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.math.BigDecimal;

public interface Evercoin {
    LimitResponse getLimit(String depositCoin, String destinationCoin);

    ValidateResponse validateAddress( String coin, String address);

    CoinsResponse getCoins();

    PriceResponse getPrice(String depositCoin, String destinationCoin, BigDecimal depositAmount, BigDecimal destinationAmount) ;

    OrderResponse createOrder(PriceResponse priceResponse, Address refundAddress, Address destinationAddress);

    StatusResponse getStatus(String orderId);
}
