package com.example.das_auth_providers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
        basePackages = "com.example.das_auth_providers",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.example\\.das_auth_providers\\.esia.*")
        }
)
public class DasAuthProvidersApplication {

    public static void main(String[] args) {
        SpringApplication.run(DasAuthProvidersApplication.class, args);
    }
}
