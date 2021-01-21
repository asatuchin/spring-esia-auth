package com.example.das_auth_providers.vk.service;

import com.example.das_auth_providers.common.cache.MapWithTTL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class VKRequestValidationService {

    private final MapWithTTL<String, String> requests = new MapWithTTL<>();
    private final BCryptPasswordEncoder encoder;

    @Value("${vk.security.request.ttl:15S}")
    private Duration requestTTL;

    @Value("${vk.security.request.secret:secret_value}")
    private String requestSecretSalt;

    public VKRequestValidationService() {
        this.encoder = new BCryptPasswordEncoder();
    }

    public String getRequestHash(final String id) {
        String requestSecret = UUID.randomUUID().toString();
        requests.put(id, requestSecret, requestTTL);
        return encoder.encode(requestSecret + requestSecretSalt);
    }

    public boolean isValid(final String id, final String hash) {
        String requestSecret = requests.get(id);
        if (requestSecret == null) {
            return false;
        }
        return encoder.matches(requestSecret + requestSecretSalt, hash);
    }
}
