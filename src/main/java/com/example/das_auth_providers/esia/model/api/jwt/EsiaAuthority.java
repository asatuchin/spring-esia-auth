package com.example.das_auth_providers.esia.model.api.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EsiaAuthority {
    String id;
    String expired;
}
