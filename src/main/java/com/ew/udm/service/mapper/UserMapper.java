package com.ew.udm.service.mapper;

import com.ew.udm.models.user.User;
import com.ew.udm.models.user.UserRoleMin;
import com.ew.udm.models.user.UserWithRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    UserWithRole selectUserRoleCollection(String userName);

    User selectByMarkCode(String markCode);
    UserWithRole selectUserRoleCollectionByMarkCode(String markCode);

    List<User> selectAll(int pageNum, int pageSize);

    int countOfUserName(String userName);
    int countOfEmail(String email);
    int countOfPhone(String phone);

    User selectByUsername(String username);

    UserRoleMin selectUserRoleMinById(@Param("userId") Integer userId);
    UserRoleMin selectUserRoleMinByName(@Param("userName") String userName);
    UserRoleMin selectUserRoleMinByMarkCode(@Param("markCode") String markCode);
}
