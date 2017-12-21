package com.ew.udm.models.user;

import java.io.Serializable;
import java.util.Date;

public class UserToken implements Serializable {
    private static final long serialVersionUID = 1;

    private Integer id;

    private Integer userId;

    private String userAgent;

    private String remoteHost;

    private String refreshToken;

    private Date refreshTokenCreateTime;

    private Date refreshTokenExpireTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? null : userAgent.trim();
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken == null ? null : refreshToken.trim();
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
}