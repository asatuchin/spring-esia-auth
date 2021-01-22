package com.example.das_auth_providers.esia.model.api.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EsiaJwtHeader {
    String alg; // GOST3410_2012_512
    String typ; // JWT
    int ver; // 0
    String sbt; // access
}
