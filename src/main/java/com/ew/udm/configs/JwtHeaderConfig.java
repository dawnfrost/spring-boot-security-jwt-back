package com.ew.udm.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtHeaderConfig {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.device}")
    private String userAgentHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    public JwtHeaderConfig() {
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public String getUserAgentHeader() {
        return userAgentHeader;
    }

    public String getTokenHead() {
        return tokenHead;
    }
}
