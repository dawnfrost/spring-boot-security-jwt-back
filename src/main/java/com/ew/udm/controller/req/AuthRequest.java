package com.ew.udm.controller.req;

public class AuthRequest {
    private String username;
    private String password;
    private int rememberDays;
    private String remoteHost;
    private String userAgent;

    public AuthRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRememberDays() {
        return rememberDays;
    }

    public void setRememberDays(int rememberDays) {
        this.rememberDays = rememberDays;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rememberDays=" + rememberDays +
                ", remoteHost='" + remoteHost + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
