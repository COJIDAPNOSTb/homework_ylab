package org.example.app.backend.model;

public enum Role {
    ADMIN,CUSTOMER,USER;
    @Override
    public String toString() {
        return name();
    }
}
