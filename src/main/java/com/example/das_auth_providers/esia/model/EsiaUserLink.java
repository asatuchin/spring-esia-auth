package com.example.das_auth_providers.esia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EsiaUserLink {
    private long userId;
    private String esiaSubjectId;
}
