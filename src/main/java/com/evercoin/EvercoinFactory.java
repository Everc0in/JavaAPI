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
            GET_COINS_SERVICE = URI + version +"/coins/";
            GET_PRICE_SERVICE = URI + version +"/price/";
            CREATE_ORDER_SERVICE = URI + version + "/order/";
            GET_STATUS_SERVICE = URI + version + "/status/";
        }

        public LimitResponse getLimit(final String from, final String to) {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(LIMIT_SERVICE + from + "-" + to);
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
                    final String lFrom = resultObject.getString("from");
                    final String lTo = resultObject.getString("to");
                    final String max = resultObject.getString("max");
                    final String min = resultObject.getString("min");
                    return new LimitResponse(lFrom, lTo, max, min);
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
                        final boolean from = order.getBoolean("from");
                        final boolean to = order.getBoolean("to");
                        final Coin coin = new Coin(name, symbol, from, to);
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
        public PriceResponse getPrice(String fromCoin, String toCoin, BigDecimal fromAmount, BigDecimal toAmount) {
            HttpURLConnection conn = null;
            try {
                String json;
                if (toAmount == null && fromAmount == null) {
                    return new PriceResponse("Amount can not be null");
                } else if (fromAmount != null) {
                    json = Json.createObjectBuilder()
                            .add("fromCoin", fromCoin)
                            .add("toCoin", toCoin)
                            .add("fromAmount", fromAmount.toPlainString())
                            .build()
                            .toString();
                } else {
                    json = Json.createObjectBuilder()
                            .add("fromCoin", fromCoin)
                            .add("toCoin", toCoin)
                            .add("withdrawAmount", toAmount.toPlainString())
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
                        final String responseFrom = order.getString("fromCoin");
                        final String responseTo = order.getString("toCoin");
                        final BigDecimal responseRcvAmount = new BigDecimal(order.getString("fromAmount"));
                        final BigDecimal responseToAmount = new BigDecimal(order.getString("toAmount"));
                        final String signature = order.getString("signature");
                        return new PriceResponse(responseFrom, responseTo, responseRcvAmount, responseToAmount, signature);
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
        public OrderResponse createOrder(PriceResponse priceResponse, Address refundAddress, Address toAddress) {
            OrderResponse response = null;
            HttpURLConnection conn = null;
            final String fromCoin = priceResponse.getFromCoin();
            final String toCoin = priceResponse.getToCoin();
            final BigDecimal fromAmount = priceResponse.getFromAmount();
            final BigDecimal toAmount = priceResponse.getToAmount();
            final String signature = priceResponse.getSignature();
            try {
                String json = Json.createObjectBuilder()
                        .add("fromCoin", fromCoin)
                        .add("toCoin", toCoin)
                        .add("toAddress", toAddress.getJsonValue())
                        .add("refundAddress", refundAddress.getJsonValue())
                        .add("fromAmount", fromAmount.toPlainString())
                        .add("toAmount", toAmount.toPlainString())
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
                        final Address depositAddress = makeAddressFromJson(order, "fromAddress");
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
                    final BigDecimal fromAmount = new BigDecimal(resultObject.get("fromAmount").toString());
                    final String fromCoin = resultObject.getString("fromCoin");
                    final String toCoin = resultObject.getString("toCoin");
                    final BigDecimal toAmount = new BigDecimal(resultObject.get("toAmount").toString());
                    final Address refundAddress = makeAddressFromJson(resultObject, "refundAddress");
                    final Address toAddress = makeAddressFromJson(resultObject, "toAddress");
                    final Address fromAddress = makeAddressFromJson(resultObject, "fromAddress");
                    final long creationTime = Long.parseLong(resultObject.get("creationTime").toString());
                    final BigDecimal fromExpectedAmount = new BigDecimal(resultObject.get("fromExpectedAmount").toString());
                    final BigDecimal toExpectedAmount = new BigDecimal(resultObject.get("toExpectedAmount").toString());
                    final String txURL = getNullableStringValue(resultObject, "txURL");
                    final BigDecimal minValue = new BigDecimal(resultObject.get("minValue").toString());
                    final BigDecimal maxValue = new BigDecimal(resultObject.get("maxValue").toString());
                    return new StatusResponse(exchangeStatus, fromAmount, fromCoin, toCoin, toAmount, refundAddress, toAddress, fromAddress, creationTime, fromExpectedAmount, toExpectedAmount, txURL, minValue, maxValue);
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
