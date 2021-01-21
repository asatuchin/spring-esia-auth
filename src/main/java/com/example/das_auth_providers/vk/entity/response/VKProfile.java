package com.example.das_auth_providers.vk.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Builder
@JsonDeserialize(builder = VKProfile.VKProfileBuilder.class)
@Value
public class VKProfile {
    int id;
    @JsonProperty("first_name")
    String firstName;
    @JsonProperty("last_name")
    String lastName;

    @JsonPOJOBuilder(withPrefix = "")
    public static class VKProfileBuilder {
    }
}
