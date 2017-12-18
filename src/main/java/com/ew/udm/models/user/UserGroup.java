package com.ew.udm.models.user;

import java.io.Serializable;

public class UserGroup implements Serializable {
    private static final long serialVersionUID = 1;

    private Integer id;

    private Integer userId;

    private Integer groupId;

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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}