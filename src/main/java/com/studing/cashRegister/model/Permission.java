package com.studing.cashRegister.model;

public enum Permission {
    ADD_GOODS("add goods"),
    CREATE_ORDER("create order"),
    UPDATE_ORDER("update order"),
    CLOSE_ORDER("close order"),
    CANCEL_ORDER("cancel order"),
    CREATE_REPORT("create report");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
