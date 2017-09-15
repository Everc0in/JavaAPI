package com.evercoin;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class MoneroAddress extends Address {

    String paymentId;

    public MoneroAddress(String mainAddress, String paymentId) {
        super(mainAddress);
        this.paymentId = paymentId;
    }

    public JsonObject getJsonValue() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder
                .add("mainAddress", mainAddress);
        if (paymentId != null && !paymentId.equals("")) {
            builder.add("tagName", "Payment Id");
            builder.add("tagValue", paymentId);
        }
        JsonObject jsonToAddress = builder.build();
        return jsonToAddress;
    }
}
