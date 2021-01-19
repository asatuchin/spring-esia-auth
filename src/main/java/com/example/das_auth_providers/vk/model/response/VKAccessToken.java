package com.example.das_auth_providers.vk.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@JsonDeserialize(builder = VKAccessToken.VKAccessTokenBuilder.class)
@Builder(builderClassName = "VKAccessTokenBuilder")
@Value
public class VKAccessToken {
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("expires_in")
    int expiresIn;
    @JsonProperty("user_id")
    String userId;
    String email;

    @JsonPOJOBuilder(withPrefix = "")
    public static class VKAccessTokenBuilder {
    }
}
