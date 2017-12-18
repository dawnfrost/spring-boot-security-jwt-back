package com.ew.udm.service.user;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.lang.StringUtils;
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
    private int uid;
    private String sub;
    private Date created;
    private Date tokenExp;
    private Date sessionExp;
    private Date userExp;
    private Date exp;
    private String[] roles;

    public JwtToken(int uid, String sub, Date created, Date tokenExp, Date sessionExp, Date userExp, List<String> roles) {
        this.uid = uid;
        this.sub = sub;
        this.created = created;
        this.tokenExp = tokenExp;
        this.sessionExp = sessionExp;
        this.userExp = userExp;
        if (roles != null && !roles.isEmpty()) {
            this.roles = new String[roles.size()];
            roles.toArray(this.roles);
        }
    }

    public JwtToken(Claims claims) {
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.getName().equals("exp")) {
                    continue;
                }

                Method method = JwtToken.class.getMethod("set" + StringUtils.capitalize(field.getName()), field.getType());
                if (field.getType() == Date.class) {
                    method.invoke(this, new Date((long) claims.get(field.getName())));
                } else if (field.getType() == int.class) {
                    method.invoke(this, (int)claims.get(field.getName()));
                } else if (field.getType() == long.class) {
                    method.invoke(this, (long)claims.get(field.getName()));
                } else if (field.getType() == String.class) {
                    method.invoke(this, claims.get(field.getName()));
                }
            }

            List list = (List)claims.get("roles");
            if (list != null && !list.isEmpty()) {
                String[] array = new String[list.size()];
                list.toArray(array);
                this.setRoles(array);
            }

            this.exp = claims.getExpiration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> toClaims() {
        Map<String, Object> ret = Maps.newHashMap();
        Field[] fields = this.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                Method method = JwtToken.class.getMethod("get" + StringUtils.capitalize(field.getName()));
                ret.put(field.getName(), method.invoke(this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public List<GrantedAuthority> getRolesAuthority() {
        if (this.roles != null) {
            List<String> authorities = Collections.arrayToList(this.roles);
            return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }

        return null;
    }

    private boolean isTokenExpired(Date date) {
        return this.exp.before(date);
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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUserId() {
        return this.uid;
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

    public Date getExp() {
        return exp;
    }

    public void setExp(Date exp) {
        this.exp = exp;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}
