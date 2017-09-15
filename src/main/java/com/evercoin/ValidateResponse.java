/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

public class ValidateResponse extends Response {
    private final boolean isValid;

    public ValidateResponse(String error) {
        super(error);
        this.isValid = false;
    }

    public ValidateResponse(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        return isValid;
    }

    @Override
    public String toString() {
        return "ValidateResponse{" +
                "isValid=" + isValid +
                '}';
    }
}
