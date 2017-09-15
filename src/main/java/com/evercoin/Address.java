/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Address {

    String mainAddress;

    public Address(String mainAddress) {
        this.mainAddress = mainAddress;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public JsonObject getJsonValue() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder
                .add("mainAddress", mainAddress);
        JsonObject jsonToAddress = builder.build();
        return jsonToAddress;
    }
}

