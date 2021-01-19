package com.example.das_auth_providers.vk.model.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@JsonDeserialize(builder = VKUsersResponse.VKUsersResponseBuilder.class)
@Value
public class VKUsersResponse {

    List<VKProfile> response;

    @JsonPOJOBuilder(withPrefix = "")
    public static class VKUsersResponseBuilder {
    }
}
