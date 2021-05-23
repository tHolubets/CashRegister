package com.studing.cashRegister.model;

import java.io.Serializable;
import java.util.Set;

public enum UserRole implements Serializable {
    COMMODITY_EXPERT(Set.of(Permission.ADD_GOODS)),
    CASHIER(Set.of(Permission.CREATE_ORDER, Permission.UPDATE_ORDER, Permission.CLOSE_ORDER)),
    SENIOR_CASHIER(Set.of(Permission.CANCEL_ORDER, Permission.CREATE_REPORT));

    private final Set<Permission> permissions;

    UserRole(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(Permission permission){
        return permissions.contains(permission);
    }
}
