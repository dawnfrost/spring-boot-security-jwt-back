package com.ew.udm.service.mapper;

import com.ew.udm.models.user.UserToken;
import org.apache.ibatis.annotations.Param;

public interface UserTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserToken record);

    int insertSelective(UserToken record);

    UserToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserToken record);

    int updateByPrimaryKey(UserToken record);

    UserToken selectByUserIdAndAgent(@Param("userId") Integer userId, @Param("userAgent") String userAgent);

    UserToken selectByRefreshToken(String refreshToken);
}