/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

public class LimitResponse extends Response {
    private final String from;
    private final String to;
    private final String max;
    private final String min;

    public LimitResponse(String error) {
        super(error);
        this.from = null;
        this.to = null;
        this.min = null;
        this.max = null;
    }

    public LimitResponse(String from, String to, String min, String max) {
        this.from = from;
        this.to = to;
        this.min = min;
        this.max = max;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMax() {
        return max;
    }

    public String getMin() {
        return min;
    }

    @Override
    public String toString() {
        return "LimitResponse{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", max='" + max + '\'' +
                ", min='" + min + '\'' +
                '}';
    }
}
