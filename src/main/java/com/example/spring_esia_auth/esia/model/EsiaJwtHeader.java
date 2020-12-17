package com.example.spring_esia_auth.esia.model;

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
