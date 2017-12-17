package com.ew.udm.service.user;

import com.ew.udm.models.user.UserWithRole;
import com.ew.udm.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Autowired
    public JwtUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserWithRole userWithRole = userMapper.selectUserRoleCollection(s);
        if (userWithRole == null) {
            throw new UsernameNotFoundException("用户[" + s + "]不存在");
        }

        return new JwtUser(userWithRole);
    }
}
