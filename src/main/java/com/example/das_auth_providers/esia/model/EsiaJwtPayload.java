package com.example.das_auth_providers.esia.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EsiaJwtPayload {
    long nbf;
    @JsonProperty("urn:esia:authority")
    EsiaAuthority authority;
    String scope;
    String iss;
    @JsonProperty("urn:esia:sid")
    String sid;
    @JsonProperty("urn:esia:sbj_id")
    String sbjId;
    long exp;
    long iat;
    @JsonProperty("client_id")
    String clientId;
}
