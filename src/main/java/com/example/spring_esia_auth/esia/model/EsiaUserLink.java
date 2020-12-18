package com.example.spring_esia_auth.esia.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EsiaUserLink {
    private long userId;
    private String esiaSubjectId;
}
