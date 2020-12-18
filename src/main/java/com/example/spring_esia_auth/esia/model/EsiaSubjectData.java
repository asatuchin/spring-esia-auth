package com.example.spring_esia_auth.esia.model;

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

