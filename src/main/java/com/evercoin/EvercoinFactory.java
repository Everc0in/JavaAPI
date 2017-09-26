/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import javax.json.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;

public class EvercoinFactory {
    private final static String URI;
    private static Evercoin evercoin;
    private static final String EVERCOIN_API_KEY = "EVERCOIN-API-KEY";

    static {
        final String endpoint = System.getProperty("evercoin.api.endpoint");
        URI = endpoint != null ? endpoint : "https://api.evercoin.com/";
    }

    public synchronized static Evercoin create(EvercoinApiConfig config) {
        if (evercoin == null) {
            evercoin = new EvercoinImpl(config.getApiKey(), config.getVersion());
        }
        return evercoin;
    }

    static class EvercoinImpl implements Evercoin {

        private final String LIMIT_SERVICE;
        private final String VALIDATE_ADDRESS_SERVICE;
        private final String GET_COINS_SERVICE;
        private final String GET_PRICE_SERVICE;
        private final String CREATE_ORDER_SERVICE;
        private final String GET_STATUS_SERVICE;
        String version;
        String apiKey;

        EvercoinImpl(String apiKey, String version) {
            this.apiKey = apiKey;
            this.version = version;
            LIMIT_SERVICE = URI + version + "/limit/";
            VALIDATE_ADDRESS_SERVICE = URI + version + "/validate/";
            GET_COINS_SERVICE = URI + version + "/coins/";
            GET_PRICE_SERVICE = URI + version + "/price/";
            CREATE_ORDER_SERVICE = URI + version + "/order/";
            GET_STATUS_SERVICE = URI + version + "/status/";
        }

        public LimitResponse getLimit(String depositCoin, String destinationCoin) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(LIMIT_SERVICE + depositCoin + "-" + destinationCoin);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty(EVERCOIN_API_KEY, apiKey);
                conn.setRequestMethod("GET");
                InputStream is = new BufferedInputStream(conn.getInputStream());
                JsonReader rdr = Json.createReader(is);
                JsonObject obj = rdr.readObject();
                String error = getNullableObjectValue(obj, "error");
                if (error == null) {
                    JsonObject resultObject = obj.getJsonObject("result");
                    final String lDeposit = resultObject.getString("depositCoin");
                    final String lDestination = resultObject.getString("destinationCoin");
                    final String maxDeposit = resultObject.getString("maxDeposit");
                    final String minDeposit = resultObject.getString("minDeposit");
                    return new LimitResponse(lDeposit, lDestination, maxDeposit, minDeposit);
                } else {
                    return new LimitResponse(error);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return new LimitResponse("Exception");
        }

        public ValidateResponse validateAddress(final String coin, final String address) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(VALIDATE_ADDRESS_SERVICE + coin + "/" + address);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty(EVERCOIN_API_KEY, apiKey);
                conn.setRequestMethod("GET");
                InputStream is = new BufferedInputStream(conn.getInputStream());
                JsonReader rdr = Json.createReader(is);
                JsonObject obj = rdr.readObject();
                String error = getNullableObjectValue(obj, "error");
                if (error == null) {
                    JsonObject resultObject = obj.getJsonObject("result");
                    final Boolean isValid = resultObject.getBoolean("isValid");
                    return new ValidateResponse(isValid);
                } else {
                    return new ValidateResponse(error);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return new ValidateResponse("Exception");
        }

        @Override
        public CoinsResponse getCoins() {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(GET_COINS_SERVICE);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty(EVERCOIN_API_KEY, apiKey);
                conn.setRequestMethod("GET");
                InputStream is = new BufferedInputStream(conn.getInputStream());
                JsonReader rdr = Json.createReader(is);
                JsonObject obj = rdr.readObject();
                String error = getNullableObjectValue(obj, "error");
                if (error == null) {
                    CoinsResponse response = new CoinsResponse();
                    JsonArray results = obj.getJsonArray("result");
                    int len = results.size();
                    for (int i = 0; i < len; i++) {
                        JsonObject order = results.getJsonObject(i);
                        final String name = order.getString("name");
                        final String symbol = order.getString("symbol");
                        final boolean fromAvailable = order.getBoolean("fromAvailable");
                        final boolean toAvailable = order.getBoolean("toAvailable");
                        final Coin coin = new Coin(name, symbol, fromAvailable, toAvailable);
                        response.getCoinList().add(coin);
                    }
                    return response;
                } else {
                    return new CoinsResponse(error);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return new CoinsResponse("Exception");
        }

        @Override
        public PriceResponse getPrice(String depositCoin, String destinationCoin, BigDecimal depositAmount, BigDecimal destinationAmount) {
            HttpURLConnection conn = null;
            try {
                String json;
                if (destinationAmount == null && depositAmount == null) {
                    return new PriceResponse("Amount can not be null");
                } else if (depositAmount != null) {
                    json = Json.createObjectBuilder()
                            .add("depositCoin", depositCoin)
                            .add("destinationCoin", destinationCoin)
                            .add("depositAmount", depositAmount.toPlainString())
                            .build()
                            .toString();
                } else {
                    json = Json.createObjectBuilder()
                            .add("depositCoin", depositCoin)
                            .add("destinationCoin", destinationCoin)
                            .add("destinationAmount", destinationAmount.toPlainString())
                            .build()
                            .toString();
                }
                URL url = new URL(GET_PRICE_SERVICE);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty(EVERCOIN_API_KEY, apiKey);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.flush();
                os.close();
                InputStream is = new BufferedInputStream(conn.getInputStream());
                JsonReader rdr = Json.createReader(is);
                JsonObject obj = rdr.readObject();
                String error = getNullableObjectValue(obj, "error");
                if (error == null) {
                    JsonValue result = obj.get("result");
                    if (!result.toString().equals("null")) {
                        JsonObject order = obj.getJsonObject("result");
                        final String responseDeposit = order.getString("depositCoin");
                        final String responseDestination = order.getString("destinationCoin");
                        final BigDecimal responseRcvAmount = new BigDecimal(order.getString("depositAmount"));
                        final BigDecimal responseToAmount = new BigDecimal(order.getString("destinationAmount"));
                        final String signature = order.getString("signature");
                        return new PriceResponse(responseDeposit, responseDestination, responseRcvAmount, responseToAmount, signature);
                    } else {
                        return new PriceResponse("No Order");
                    }
                } else {
                    return new PriceResponse(error);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return new PriceResponse("Exception");
        }

        @Override
        public OrderResponse createOrder(PriceResponse priceResponse, Address refundAddress, Address destinationAddress) {
            OrderResponse response = null;
            HttpURLConnection conn = null;
            final String depositCoin = priceResponse.getDepositCoin();
            final String destinationCoin = priceResponse.getDestinationCoin();
            final BigDecimal depositAmount = priceResponse.getDepositAmount();
            final BigDecimal destinationAmount = priceResponse.getDestinationAmount();
            final String signature = priceResponse.getSignature();
            try {
                String json = Json.createObjectBuilder()
                        .add("depositCoin", depositCoin)
                        .add("destinationCoin", destinationCoin)
                        .add("destinationAddress", destinationAddress.getJsonValue())
                        .add("refundAddress", refundAddress.getJsonValue())
                        .add("depositAmount", depositAmount.toPlainString())
                        .add("destinationAmount", destinationAmount.toPlainString())
                        .add("signature", signature)
                        .build()
                        .toString();
                URL url = new URL(CREATE_ORDER_SERVICE);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty(EVERCOIN_API_KEY, apiKey);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes("UTF-8"));
                os.flush();
                os.close();
                InputStream is = new BufferedInputStream(conn.getInputStream());
                JsonReader rdr = Json.createReader(is);
                JsonObject obj = rdr.readObject();
                String error = getNullableObjectValue(obj, "error");
                if (error == null) {
                    JsonValue result = obj.get("result");
                    if (!result.toString().equals("null")) {
                        JsonObject order = obj.getJsonObject("result");
                        final String orderId = order.getString("orderId");
                        final Address depositAddress = makeAddressFromJson(order, "depositAddress");
                        return new OrderResponse(orderId, depositAddress);
                    } else {
                        return new OrderResponse("Error");
                    }
                } else {
                    return new OrderResponse(error);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return new OrderResponse("Exception");
        }

        public StatusResponse getStatus(final String orderId) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(GET_STATUS_SERVICE + orderId);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty(EVERCOIN_API_KEY, apiKey);
                conn.setRequestMethod("GET");
                InputStream is = new BufferedInputStream(conn.getInputStream());
                JsonReader rdr = Json.createReader(is);
                JsonObject obj = rdr.readObject();
                String error = getNullableObjectValue(obj, "error");
                if (error == null) {
                    JsonObject resultObject = obj.getJsonObject("result");
                    final Status exchangeStatus = Status.get(resultObject.getInt("exchangeStatus"));
                    final BigDecimal depositAmount = new BigDecimal(resultObject.get("depositAmount").toString());
                    final String depositCoin = resultObject.getString("depositCoin");
                    final String destinationCoin = resultObject.getString("destinationCoin");
                    final BigDecimal destinationAmount = new BigDecimal(resultObject.get("destinationAmount").toString());
                    final Address refundAddress = makeAddressFromJson(resultObject, "refundAddress");
                    final Address destinationAddress = makeAddressFromJson(resultObject, "destinationAddress");
                    final Address depositAddress = makeAddressFromJson(resultObject, "depositAddress");
                    final long creationTime = Long.parseLong(resultObject.get("creationTime").toString());
                    final BigDecimal depositExpectedAmount = new BigDecimal(resultObject.get("depositExpectedAmount").toString());
                    final BigDecimal destinationExpectedAmount = new BigDecimal(resultObject.get("destinationExpectedAmount").toString());
                    final String txURL = getNullableStringValue(resultObject, "txURL");
                    final BigDecimal minDeposit = new BigDecimal(resultObject.get("minDeposit").toString());
                    final BigDecimal maxDeposit = new BigDecimal(resultObject.get("maxDeposit").toString());
                    return new StatusResponse(exchangeStatus, depositAmount, depositCoin, destinationCoin, destinationAmount, refundAddress, destinationAddress, depositAddress, creationTime, depositExpectedAmount, destinationExpectedAmount, txURL, minDeposit, maxDeposit);
                } else {
                    return new StatusResponse(error);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return new StatusResponse("Exception");
        }

        private String getNullableObjectValue(JsonObject resultObject, String tag) {
            String value = null;
            if (!resultObject.get(tag).toString().equals("null")) {
                value = resultObject.getJsonObject(tag).getString("message");
            }
            return value;
        }

        private String getNullableStringValue(JsonObject resultObject, String tag) {
            String value = null;
            if (!resultObject.get(tag).toString().equals("null")) {
                value = resultObject.getString(tag);
            }
            return value;
        }

        private Address makeAddressFromJson(JsonObject jsonObject, String tag) {
            final JsonObject addressObject = jsonObject.getJsonObject(tag);
            final String mainAddress = addressObject.getString("mainAddress");
            String tagName = getNullableObjectValue(addressObject, "tagName");
            String tagValue = getNullableObjectValue(addressObject, "tagValue");
            if (tagValue != null && tagName.equals("Payment Id")) {
                return new MoneroAddress(mainAddress, tagValue);
            } else if (tagValue != null && tagName.equals("DestinationTag")) {
                return new RippleAddress(mainAddress, new Integer(tagValue));
            }
            return new Address(mainAddress);
        }
    }
}
