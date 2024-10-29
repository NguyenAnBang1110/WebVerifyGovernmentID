package com.signature.backend.entity;

public class RefreshTokenRequest {
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
