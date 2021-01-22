package com.example.das_auth_providers.das_emulation.entity;

public enum AuthProviders {

    ESIA("esia"),
    VK("vk");

    private final String name;

    AuthProviders(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}