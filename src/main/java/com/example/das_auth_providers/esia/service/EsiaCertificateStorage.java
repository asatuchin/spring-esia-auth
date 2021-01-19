package com.example.das_auth_providers.esia.service;

import org.springframework.stereotype.Component;

import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

@Component
public class EsiaCertificateStorage {

    private final Map<String, X509Certificate> map = new HashMap<>();

    public void put(final String algorithm, final X509Certificate cert) {
        this.map.put(algorithm, cert);
    }

    public X509Certificate get(final String algorithm) {
        return this.map.get(algorithm);
    }
}
