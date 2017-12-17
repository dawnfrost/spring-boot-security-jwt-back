package com.ew.udm.service.mapper;

import com.ew.udm.models.user.GroupRole;

public interface GroupRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupRole record);

    int insertSelective(GroupRole record);

    GroupRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupRole record);

    int updateByPrimaryKey(GroupRole record);
}