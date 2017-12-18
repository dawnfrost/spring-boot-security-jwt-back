package com.ew.udm.controller.req;

public class AuthRequest {
    private String username;
    private String password;
    private int rememberDays;

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
}
