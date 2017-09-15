/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

public abstract class Response {
    private final String error;
    private final boolean success;

    public Response(String error) {
        this.error = error;
        success = false;
    }

    public Response() {
        this.success = true;
        error = null;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }
}
