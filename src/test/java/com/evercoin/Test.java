/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.math.BigDecimal;
import java.util.List;

public class Test {

    public final static Test sharedInstance = new Test();

    public static void main(String[] args) throws InterruptedException {
        Evercoin evercoin = EvercoinFactory.create(new EvercoinApiConfig("Your API Key", "v1"));
        LimitResponse limitResponse = evercoin.getLimit("ETH", "LTC");
        List<CoinResponse> coins = evercoin.getCoins();
        if (!evercoin.validateAddress("ETH", "0x4f78e407b312e6dde8af699ca73b7c15dddfea42").isValid()) {
            return;
        }
        Address refundAddress = new Address("0x4f78e407b312e6dde8af699ca73b7c15dddfea42");
        if (!evercoin.validateAddress("LTC", "mrkEVRsbrLeb7CFE6wcKJv1TajfgJjU6wj").isValid()) {
            return;
        }
        Address withdrawAddress = new Address("mrkEVRsbrLeb7CFE6wcKJv1TajfgJjU6wj");
        PriceResponse priceResponse = evercoin.getPrice("ETH", "LTC", new BigDecimal("1.0"), null);
        if (priceResponse.isSuccess()) {
            OrderResponse orderResponse = evercoin.createOrder(priceResponse, refundAddress, withdrawAddress);
            if (orderResponse.isSuccess()) {
                StatusResponse statusResponse = evercoin.getStatus(orderResponse.getOrderId());
                while (true) {
                    Thread.sleep(2000);
                    if (statusResponse.isSuccess()) {
                        System.out.println(statusResponse.getExchangeStatus().getText());
                        if (statusResponse.getExchangeStatus().getId() == Status.All_Done.getId()) {
                            return;
                        }
                    }
                }
            } else {
                System.err.println(orderResponse.getError());
            }
        }
    }
}
