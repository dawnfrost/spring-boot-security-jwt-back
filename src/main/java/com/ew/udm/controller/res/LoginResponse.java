package com.ew.udm.controller.res;

/**
 * Created by wxy on 2017-12-18 13:48
 */
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private long refreshTokenCreateTime;
    private int refreshTokenExpire;

    public LoginResponse() {
    }

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getRefreshTokenCreateTime() {
        return refreshTokenCreateTime;
    }

    public void setRefreshTokenCreateTime(long refreshTokenCreateTime) {
        this.refreshTokenCreateTime = refreshTokenCreateTime;
    }

    public int getRefreshTokenExpire() {
        return refreshTokenExpire;
    }

    public void setRefreshTokenExpire(int refreshTokenExpire) {
        this.refreshTokenExpire = refreshTokenExpire;
    }
}
