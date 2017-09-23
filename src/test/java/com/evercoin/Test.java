/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import java.math.BigDecimal;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("evercoin.api.endpoint", "https://test.evercoin.com/");
        final String API_KEY = "7f878074f09f4f673554f30be51b6a0d";
        final String version = "v1";
        final String from = "LTC";
        final String to = "ETH";
        final String fromAddress = "mwCwTceJvYV27KXBc3NJZys6CjsgsoeHmf";
        final String toAddress = "0x4f78e407b312e6dde8af699ca73b7c15dddfea42";
        final String fromAmount = "1.0";
        Evercoin evercoin = EvercoinFactory.create(new EvercoinApiConfig(API_KEY, version));
        CoinsResponse coins = evercoin.getCoins();
        Coin fromCoin = coins.getCoin(from);
        Coin toCoin = coins.getCoin(to);
        if (fromCoin != null && !fromCoin.isFromAvailable()) {
            //Exchanging from BTC is currently available.
            return;
        }
        if (toCoin != null && !toCoin.isToAvailable()) {
            //Exchanging to ETH is currently available.
            return;
        }
        if (!evercoin.validateAddress(from, fromAddress).isValid()) {
            //Your BTC address is not valid.
            return;
        }
        Address refundAddress = new Address(fromAddress);
        if (!evercoin.validateAddress(to, toAddress).isValid()) {
            //Your ETH address is not valid.
            return;
        }
        Address destinationAddress = new Address(toAddress);
        PriceResponse priceResponse = evercoin.getPrice(from, to, new BigDecimal(fromAmount), null);
        if (priceResponse.isSuccess()) {
            OrderResponse orderResponse = evercoin.createOrder(priceResponse, refundAddress, destinationAddress);
            if (orderResponse.isSuccess()) {
                System.out.println("You should deposit to this address: " + orderResponse.getDepositAddress().getMainAddress());
                while (true) {
                    Thread.sleep(10000);
                    StatusResponse statusResponse = evercoin.getStatus(orderResponse.getOrderId());
                    if (statusResponse.isSuccess()) {
                        if (statusResponse.getExchangeStatus().getId() == Status.Awaiting_Deposit.getId()) {
                            System.out.println("Send 1 BTC to the address");
                        } else if (statusResponse.getExchangeStatus().getId() == Status.Awaiting_Confirm.getId()) {
                            System.out.println("Looks like you sent BTC. Waiting for confirmation on the blockchain.");
                        } else if (statusResponse.getExchangeStatus().getId() == Status.Awaiting_Exchange.getId()) {
                            System.out.println("Your ETH is on the way.");
                        } else if (statusResponse.getExchangeStatus().getId() == Status.All_Done.getId()) {
                            System.out.println("Success! Enjoy your ETH");
                            return;
                        }
                    }
                }
            } else {
                System.err.println("There is an error in order creation: " + orderResponse.getError());
            }
        }
    }
}
