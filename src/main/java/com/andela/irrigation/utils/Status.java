package com.andela.irrigation.utils;

public enum Status {
    INACTIVE("INACTIVE"),
    DONE("DONE");

    private String value;

    Status(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
