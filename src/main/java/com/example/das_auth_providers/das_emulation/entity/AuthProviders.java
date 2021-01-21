package com.example.das_auth_providers.das_emulation.entity;

public enum AuthProviders {

    VK("vk");

    private final String name;

    AuthProviders(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}