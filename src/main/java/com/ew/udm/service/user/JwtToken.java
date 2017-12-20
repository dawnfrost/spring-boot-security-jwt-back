package com.ew.udm.service.user;

import com.ew.udm.configs.AuthenticationStatus;
import com.ew.udm.models.user.Role;
import com.ew.udm.models.user.UserWithRole;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class JwtToken {
    private String sub;
    private Date created;
    private Date tokenExp;
    private Date sessionExp;
    private Date userExp;
    private List<String> roles;

    public JwtToken(UserWithRole user, Date created, Date tokenExp, Date sessionExp, Date userExp) {
        this.sub = user.getUserName();
        this.created = created;
        this.tokenExp = tokenExp;
        this.sessionExp = sessionExp;
        this.userExp = userExp;
        this.roles = user.getRoles();
    }

    public JwtToken(Claims claims) {
        this.sub = claims.getSubject();
        this.created = claims.getIssuedAt();
        this.tokenExp = claims.getExpiration();
        this.sessionExp = claims.get("exp2", Date.class);
        this.userExp = claims.get("exp3", Date.class);
        if (claims.containsKey("roles")) {
            List roles = claims.get("roles", List.class);
            this.roles = (List<String>)roles.stream().map(x -> "ROLE_" + x).collect(Collectors.toList());
        } else {
            this.roles = new ArrayList<>();
        }
    }

    public JwtBuilder getBuilder() {
        JwtBuilder builder = Jwts.builder();
        builder.setIssuedAt(this.getCreated());
        builder.setExpiration(this.getTokenExp());
        builder.setSubject(this.getSub());

        Map<String, Object> claims = Maps.newHashMap();
        claims.put("exp2", this.getSessionExp());
        claims.put("exp3", this.getUserExp());
        claims.put("roles", this.roles.stream().map(x -> x.substring(5)).collect(Collectors.toList()));

        builder.addClaims(claims);
        return builder;
    }

    public List<GrantedAuthority> getAuthorities() {
        if (this.roles != null && !this.roles.isEmpty()) {
            return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private boolean isTokenExpired(Date date) {
        return this.tokenExp.before(date);
    }

    private boolean isUserExpired(Date date) {
        return this.userExp.before(date);
    }

    private boolean isSessionExpired(Date date) {
        return this.sessionExp.before(date);
    }

    public AuthenticationStatus checkExpire() {
        Date now = new Date();
        if (isTokenExpired(now)) {
            return AuthenticationStatus.TOKEN_EXPIRE;
        }

        if (isSessionExpired(now)) {
            return AuthenticationStatus.SESSION_EXPIRE;
        }

        if (isUserExpired(now)) {
            return AuthenticationStatus.USER_EXPIRE;
        }

        return AuthenticationStatus.SUCCESS;
    }

    public String getUserName() {
        return this.sub;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getTokenExp() {
        return tokenExp;
    }

    public void setTokenExp(Date tokenExp) {
        this.tokenExp = tokenExp;
    }

    public Date getSessionExp() {
        return sessionExp;
    }

    public void setSessionExp(Date sessionExp) {
        this.sessionExp = sessionExp;
    }

    public Date getUserExp() {
        return userExp;
    }

    public void setUserExp(Date userExp) {
        this.userExp = userExp;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
