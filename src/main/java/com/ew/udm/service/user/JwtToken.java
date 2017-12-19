package com.ew.udm.service.user;

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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtToken {
    private String sub;
    private Date created;
    private Date tokenExp;
    private Date sessionExp;
    private Date userExp;

    public JwtToken(String sub, Date created, Date tokenExp, Date sessionExp, Date userExp) {
        this.sub = sub;
        this.created = created;
        this.tokenExp = tokenExp;
        this.sessionExp = sessionExp;
        this.userExp = userExp;
    }

    public JwtToken(Claims claims) {
        this.sub = claims.getSubject();
        this.created = claims.getIssuedAt();
        this.tokenExp = claims.getExpiration();
        this.sessionExp = claims.get("exp2", Date.class);
        this.userExp = claims.get("exp3", Date.class);
    }

    public JwtBuilder getBuilder() {
        JwtBuilder builder = Jwts.builder();
        builder.setIssuedAt(this.getCreated());
        builder.setExpiration(this.getTokenExp());
        builder.setSubject(this.getSub());

        Map<String, Object> claims = Maps.newHashMap();
        claims.put("exp2", this.getSessionExp());
        claims.put("exp3", this.getUserExp());
        builder.addClaims(claims);

        return builder;
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

    public ExpireType checkExpire() {
        Date now = new Date();
        if (isTokenExpired(now)) {
            return ExpireType.TOKEN_EXPIRE;
        }

        if (isSessionExpired(now)) {
            return ExpireType.SESSION_EXPIRE;
        }

        if (isUserExpired(now)) {
            return ExpireType.USER_EXPIRE;
        }

        return ExpireType.NONE;
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

}
