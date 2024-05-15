package com.general.entity;

public class DataItem {
    private final int id;
    private final String value;

    public DataItem(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return value;
    }
}
