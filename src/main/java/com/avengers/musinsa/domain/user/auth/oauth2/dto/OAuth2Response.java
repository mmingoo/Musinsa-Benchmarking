package com.avengers.musinsa.domain.user.auth.oauth2.dto;

public interface OAuth2Response {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}