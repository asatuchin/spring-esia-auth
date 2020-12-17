package com.example.spring_esia_auth.esia.configuration;

import com.example.spring_esia_auth.esia.service.EsiaSignatureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsiaRequestSignatureConfiguration {

    @Value("${esia.signature.algorithm}")
    private String algorithm;

    @Value("${esia.signature.certificate_path}")
    private String keyPairFilePath;

    @Value("${esia.signature.key_store_password}")
    private String keyStorePassword;

    @Value("${esia.signature.key_pair_name}")
    private String keyPairName;

    @Bean
    public EsiaSignatureService esiaSignatureService() {
        return new EsiaSignatureService(algorithm, keyPairFilePath, keyStorePassword, keyPairName);
    }
}
