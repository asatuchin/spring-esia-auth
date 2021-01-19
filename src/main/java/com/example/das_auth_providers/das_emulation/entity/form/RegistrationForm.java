package com.example.das_auth_providers.das_emulation.entity.form;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class RegistrationForm {

    String email;
    String studentID;
    String password;

    @JsonPOJOBuilder
    public static class RegistrationFormBuilder {
    }
}
