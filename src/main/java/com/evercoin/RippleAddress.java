package com.evercoin;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class RippleAddress extends Address {

    Integer destinationTag;

    public RippleAddress(String mainAddress, Integer destinationTag) {
        super(mainAddress);
        this.destinationTag = destinationTag;
    }

    public JsonObject getJsonValue() {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder
                .add("mainAddress", mainAddress);
        if (destinationTag != null && destinationTag != 0) {
            builder.add("tagName", "Payment Id");
            builder.add("tagValue", destinationTag.toString());
        }
        JsonObject jsonToAddress = builder.build();
        return jsonToAddress;
    }
}
