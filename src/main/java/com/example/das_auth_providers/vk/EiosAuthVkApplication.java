package com.example.das_auth_providers.vk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(
        scanBasePackages = {"com.example.eios_auth_vk"}
)
public class EiosAuthVkApplication implements WebMvcConfigurer {

    public static void main(final String[] args) {
        SpringApplication.run(EiosAuthVkApplication.class, args);
    }
}
