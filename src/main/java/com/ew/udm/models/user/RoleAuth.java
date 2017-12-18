package com.ew.udm.models.user;

import java.io.Serializable;

public class RoleAuth implements Serializable {
    private static final long serialVersionUID = 1;

    private Integer id;

    private Integer roleId;

    private Integer authId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getAuthId() {
        return authId;
    }

    public void setAuthId(Integer authId) {
        this.authId = authId;
    }
}