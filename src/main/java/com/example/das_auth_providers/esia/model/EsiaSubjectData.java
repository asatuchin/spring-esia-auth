package com.example.das_auth_providers.esia.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class EsiaSubjectData {
    String firstName;
    String lastName;
    String birthDate;
    String passport;
}

