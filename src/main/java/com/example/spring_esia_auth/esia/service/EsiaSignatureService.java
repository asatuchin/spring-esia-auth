package com.example.spring_esia_auth.esia.service;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;

public class EsiaSignatureService {

    private final PrivateKey privateKey;

    public EsiaSignatureService(
            final String algorithm,
            final String keyPairFilePath,
            final String storePass,
            final String keyPairName
    ) {
        try {
            KeyStore keyStore = KeyStore.getInstance(algorithm); // PKCS7
            keyStore.load(new FileInputStream(keyPairFilePath), storePass.toCharArray()); // "sender_keystore.p12", "pass"
            privateKey = (PrivateKey) keyStore.getKey(keyPairName, storePass.toCharArray()); // "senderKeyPair"
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String sign(final String message) {
        return message;
    }
}
