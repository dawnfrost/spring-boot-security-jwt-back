package com.ew.udm.controller.res;

import java.util.Date;

/**
 * Created by wxy on 2017-12-18 13:48
 */
public class LoginResponse {
    private String accessToken;
    private Date accessTokenCreateTime;
    private Date accessTokenExpireTime;

    private String refreshToken;
    private Date refreshTokenCreateTime;
    private Date refreshTokenExpireTime;

    public LoginResponse() {

    }

    public LoginResponse(String accessToken, Date accessTokenCreateTime, Date accessTokenExpireTime, String refreshToken, Date refreshTokenCreateTime, Date refreshTokenExpireTime) {
        this.accessToken = accessToken;
        this.accessTokenCreateTime = accessTokenCreateTime;
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshToken = refreshToken;
        this.refreshTokenCreateTime = refreshTokenCreateTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
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

    public Date getRefreshTokenCreateTime() {
        return refreshTokenCreateTime;
    }

    public void setRefreshTokenCreateTime(Date refreshTokenCreateTime) {
        this.refreshTokenCreateTime = refreshTokenCreateTime;
    }

    public Date getRefreshTokenExpireTime() {
        return refreshTokenExpireTime;
    }

    public void setRefreshTokenExpireTime(Date refreshTokenExpireTime) {
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public Date getAccessTokenCreateTime() {
        return accessTokenCreateTime;
    }

    public void setAccessTokenCreateTime(Date accessTokenCreateTime) {
        this.accessTokenCreateTime = accessTokenCreateTime;
    }

    public Date getAccessTokenExpireTime() {
        return accessTokenExpireTime;
    }

    public void setAccessTokenExpireTime(Date accessTokenExpireTime) {
        this.accessTokenExpireTime = accessTokenExpireTime;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", accessTokenCreateTime=" + accessTokenCreateTime +
                ", accessTokenExpireTime=" + accessTokenExpireTime +
                ", refreshToken='" + refreshToken + '\'' +
                ", refreshTokenCreateTime=" + refreshTokenCreateTime +
                ", refreshTokenExpireTime=" + refreshTokenExpireTime +
                '}';
    }
}
