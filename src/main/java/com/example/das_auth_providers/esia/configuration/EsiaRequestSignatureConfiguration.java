package com.example.das_auth_providers.esia.configuration;

import com.example.das_auth_providers.esia.exception.EsiaSignatureException;
import com.example.das_auth_providers.esia.service.EsiaSignatureService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsiaRequestSignatureConfiguration {

    @Value("${esia.signature.certificate_path}")
    private String keyPairFilePath;

    @Value("${esia.signature.key_store_password}")
    private String keyStorePassword;

    @Bean
    public EsiaSignatureService esiaSignatureService() throws EsiaSignatureException {
        return new EsiaSignatureService(keyPairFilePath, keyStorePassword);
    }
}
