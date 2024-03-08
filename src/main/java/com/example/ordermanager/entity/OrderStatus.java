package com.example.ordermanager.entity;

public enum OrderStatus {
    WAITING("WAITING"),
    FINISHED("FINISHED");

    public final String value;

    private OrderStatus(String value) {
        this.value = value;
    }
}
