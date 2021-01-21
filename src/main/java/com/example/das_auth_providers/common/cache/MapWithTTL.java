package com.example.das_auth_providers.common.cache;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

public class MapWithTTL<K, V> {

    private final HashMap<K, V> map = new HashMap<>();
    private final HashMap<K, Instant> expirationMap = new HashMap<>();

    public void put(final K key, final V value, final Duration expires) {
        map.put(key, value);
        expirationMap.put(key, Instant.now().plus(expires));
    }

    public V get(final K key) {
        Instant expires = expirationMap.get(key);
        V value = null;
        if (expires != null && expires.isAfter(Instant.now())) {
            value = map.get(key);
        }
        map.remove(key);
        expirationMap.remove(key);
        return value;
    }
}
