package com.ew.udm.service.user;

import com.ew.udm.models.user.Role;
import com.ew.udm.models.user.User;
import com.ew.udm.models.user.UserWithRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUser extends User implements UserDetails {
    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(UserWithRole userWithRole) {
        super.setId(userWithRole.getId());
        super.setUserName(userWithRole.getUserName());
        super.setPassword(userWithRole.getPassword());
        super.setCreateTime(userWithRole.getCreateTime());
        super.setMarkCode(userWithRole.getMarkCode());
        super.setDisplayName(userWithRole.getDisplayName());
        super.setEmail(userWithRole.getEmail());
        super.setExpireTime(userWithRole.getExpireTime());
        super.setIsEnable(userWithRole.getIsEnable());
        super.setIsLocked(userWithRole.getIsLocked());
        super.setLastLoginTime(userWithRole.getLastLoginTime());
        super.setStatus(userWithRole.getStatus());

        List<Role> roles = userWithRole.getRoleList();
        List<String> roleTags = Lists.newArrayListWithCapacity(roles.size());
        for (Role role : roles) {
            roleTags.add(role.getRole());
        }

        this.authorities = mapToGrantedAuthorities(roleTags);
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.getExpireTime().after(new Date());
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.getIsLocked() == 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.getIsEnable() != 0;
    }

}
