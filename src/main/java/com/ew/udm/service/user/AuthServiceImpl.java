package com.ew.udm.service.user;

import com.ew.udm.controller.req.AuthRequest;
import com.ew.udm.controller.res.LoginResponse;
import com.ew.udm.models.user.User;
import com.ew.udm.models.user.UserGroup;
import com.ew.udm.models.user.UserWithRole;
import com.ew.udm.service.mapper.UserGroupMapper;
import com.ew.udm.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserMapper userMapper;
    private final UserGroupMapper userGroupMapper;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, UserMapper userMapper, UserGroupMapper userGroupMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userMapper = userMapper;
        this.userGroupMapper = userGroupMapper;
    }

    @Override
    public String checkUserRegister(final User user) {
        if (userMapper.countOfUserName(user.getUserName()) > 0) {
            return "用户名已被注册";
        }

        if (!user.getEmail().isEmpty() && userMapper.countOfEmail(user.getEmail()) > 0) {
            return "邮箱地址已被注册";
        }

        if (!user.getPhone().isEmpty() && userMapper.countOfPhone(user.getPhone()) > 0) {
            return "手机号已被注册";
        }

        return "";
    }

    @Transactional
    @Override
    public User register(User userToAdd, int groupId) throws Exception {
        final String checkResult = checkUserRegister(userToAdd);
        if (!checkResult.isEmpty()) {
            throw new Exception(checkResult);
        }

        final String username = userToAdd.getUserName();
        if (userMapper.countOfUserName(username) > 0) {
            throw new Exception("用户名已被注册");
        }

        if (!userToAdd.getEmail().isEmpty() && userMapper.countOfEmail(userToAdd.getEmail()) > 0) {
            throw new Exception("邮箱地址已被注册");
        }

        if (!userToAdd.getPhone().isEmpty() && userMapper.countOfPhone(userToAdd.getPhone()) > 0) {
            throw new Exception("手机号已被注册");
        }

        Date now = new Date();
        userToAdd.setCreateTime(now);
        userToAdd.setMarkCode(username + userToAdd.getId() + userToAdd.getPassword() + now.toString());
        userMapper.insertSelective(userToAdd);
        UserGroup userGroup = new UserGroup();
        userGroup.setUserId(userToAdd.getId());
        userGroup.setGroupId(groupId);
        userGroupMapper.insertSelective(userGroup);
        return userToAdd;
    }

    @Override
    public LoginResponse login(AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserWithRole user = userMapper.selectUserRoleCollection(authRequest.getUsername());
        return jwtTokenUtil.generateNewToken(user, authRequest);
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            return jwtTokenUtil.refreshToken(token);
        }

        return null;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp = null;
        for (byte val : bytes) {
            temp = Integer.toHexString(val & 0xFF);
            if (temp.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }

    private static String sha256(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            return byte2Hex(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
