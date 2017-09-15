/*
 * Copyright (c) 2016-2017, Evercoin. All Rights Reserved.
 */
package com.evercoin;

public enum Status {
    Awaiting_Deposit(1, "Awaiting_Deposit"),
    Awaiting_Confirm(2, "Awaiting_Confirm"),
    Awaiting_Exchange(3, "Awaiting_Exchange"),
    Awaiting_Refund(4, "Awaiting_Refund"),
    All_Done(5, "All_Done"),
    Refund_Done(6, "Refund_Done"),
    Minimum_Cancel(7, "Minimum_Cancel"),
    Send_Money_Error(8, "Send_Money_Error"),
    Expire_Exchange(9, "Expire_Exchange"),
    Cancel(10, "Cancel");
    private final String text;
    private final int id;

    private Status(final int id, final String text) {
        this.text = text;
        this.id = id;
    }

    public static Status get(int id) {
        for (Status status : values()) {
            if (status.getId() == id)
                return status;
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}