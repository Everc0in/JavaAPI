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
import java.util.ArrayList;
import java.util.List;

public class EvercoinFactory {
    private final static String URI;
    private final static String LIMIT_SERVICE;
    private final static String VALIDATE_ADDRESS_SERVICE;
    private final static String CANCEL_SERVICE;
    private final static String GET_COINS_SERVICE;
    private final static String GET_PRICE_SERVICE;
    private final static String CREATE_ORDER_SERVICE;
    private final static String GET_STATUS_SERVICE;
    private static Evercoin evercoin;

    static {
        final String endpoint = System.getProperty("evercoin.api.endpoint");
        URI = endpoint != null ? endpoint : "https://api.evercoin.com/";
        LIMIT_SERVICE = URI + "limit/";
        VALIDATE_ADDRESS_SERVICE = URI + "validate/";
        CANCEL_SERVICE = URI + "cancel/";
        GET_COINS_SERVICE = URI + "coins/";
        GET_PRICE_SERVICE = URI + "price/";
        CREATE_ORDER_SERVICE = URI + "order/";
        GET_STATUS_SERVICE = URI + "status/";
    }

    public synchronized static Evercoin create(EvercoinApiConfig config) {
        if (evercoin == null) {
            evercoin = new EvercoinImpl(config.getApiKey(), config.getVersion());
        }
        return evercoin;
    }

    static class EvercoinImpl implements Evercoin {
        String version;
        String apiKey;

        EvercoinImpl(String apiKey, String version) {
            this.apiKey = apiKey;
            this.version = version;
        }

        public LimitResponse getLimit(final String from, final String to) {
            String url = LIMIT_SERVICE + from + "-" + to;
            try (InputStream is = new URL(url).openStream();
                 JsonReader rdr = Json.createReader(is)) {
                JsonObject obj = rdr.readObject();
                String error = getNullableStringValue(obj, "error");
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
            }
            return new LimitResponse("Exception");
        }

        public ValidateResponse validateAddress(final String coin, final String address) {
            String url = VALIDATE_ADDRESS_SERVICE + coin + "/" + address;
            try (InputStream is = new URL(url).openStream();
                 JsonReader rdr = Json.createReader(is)) {
                JsonObject obj = rdr.readObject();
                String error = getNullableStringValue(obj, "error");
                if (error == null) {
                    final String isValid = obj.getString("result");
                    if (isValid.equals("true")) {
                        return new ValidateResponse(true);
                    } else {
                        return new ValidateResponse(false);
                    }
                } else {
                    return new ValidateResponse(error);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new ValidateResponse("Exception");
        }

        @Override
        public CancelResponse cancelOrder(String orderId) {
            HttpURLConnection conn = null;
            try {
                String json = Json.createObjectBuilder()
                        .add("orderId", orderId)
                        .build()
                        .toString();
                URL url = new URL(CANCEL_SERVICE);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
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
                String error = getNullableStringValue(obj, "error");
                if (error == null) {
                    return new CancelResponse();
                } else {
                    return new CancelResponse(error);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    conn.disconnect();
            }
            return new CancelResponse("Exception");
        }

        @Override
        public List<CoinResponse> getCoins() {
            List<CoinResponse> list = new ArrayList<>();
            try (InputStream is = new URL(GET_COINS_SERVICE).openStream();
                 JsonReader rdr = Json.createReader(is)) {
                JsonObject obj = rdr.readObject();
                JsonArray results = obj.getJsonArray("result");
                int len = results.size();
                for (int i = 0; i < len; i++) {
                    JsonObject order = results.getJsonObject(i);
                    final String name = order.getString("name");
                    final String symbol = order.getString("symbol");
                    final boolean from = order.getBoolean("from");
                    final boolean to = order.getBoolean("to");
                    final CoinResponse response = new CoinResponse(name, symbol, from, to);
                    list.add(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
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
                String error = getNullableStringValue(obj, "error");
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
                String error = getNullableStringValue(obj, "error");
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
            String url = GET_STATUS_SERVICE + orderId;
            try (InputStream is = new URL(url).openStream();
                 JsonReader rdr = Json.createReader(is)) {
                JsonObject obj = rdr.readObject();
                String error = getNullableStringValue(obj, "error");
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
            }
            return new StatusResponse("Exception");
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
            String tagName = getNullableStringValue(addressObject, "tagName");
            String tagValue = getNullableStringValue(addressObject, "tagValue");
            if (tagName.equals("Payment Id") && tagValue != null) {
                return new MoneroAddress(mainAddress, tagValue);
            } else if (tagName.equals("DestinationTag") && tagValue != null) {
                return new RippleAddress(mainAddress, new Integer(tagValue));
            }
            return new Address(mainAddress);
        }
    }
}
