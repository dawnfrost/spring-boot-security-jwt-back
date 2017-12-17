package com.ew.udm.controller;

import com.ew.udm.models.user.User;
import com.ew.udm.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('MASTER')")
public class UserController {

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @RequestMapping(value = "/list/{pageNum}/{pageSize}", method = RequestMethod.GET)
    public List<User> getUsers(@PathVariable int pageNum, @PathVariable int pageSize) {
        List<User> users = userMapper.selectAll(pageNum, pageSize);
        for (User user : users) {
            user.setPassword("");
        }

        return users;
    }
}
