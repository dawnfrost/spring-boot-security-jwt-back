package com.ew.udm.models.user;

import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wxy on 2017-12-19 10:34
 */
public class UserRoleMin implements Serializable {
    private static final long serialVersionUID = 1;

    private Integer userId;

    private String username;

    private String password;

    private String markCode;

    private List<Role> roles;

    public UserRoleMin() {
    }

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = Lists.newArrayListWithCapacity(roles.size());
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        }

        return authorities;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getMarkCode() {
        return markCode;
    }

    public void setMarkCode(String markCode) {
        this.markCode = markCode;
    }
}
