package com.evercoin;

public enum TxStatus {
    NO_DEPOSITS("no_deposits"),
    RECEIVED("received"),
    COMPLETED("completed"),
    RETURNED("returned"),
    FAILED("failed");

    private String status;

    TxStatus(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }

    public static TxStatus get(String status) {
        for (TxStatus txStatus : values()) {
            if (txStatus.status().equals(status))
                return txStatus;
        }
        return null;
    }
}