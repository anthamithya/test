package com.welcomeletterservice.service.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorrespondenceTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private long expiresIn;
    @JsonProperty("refresh_expires_in")
    private String refreshExpiresIn;
    @JsonProperty("token_type")
    private String tokenType;

    private long createdAt = System.currentTimeMillis() / 1000;
    public boolean isExpired() {
        long now = System.currentTimeMillis() / 1000;
        return now >= (createdAt + expiresIn - 30);
    }
}
