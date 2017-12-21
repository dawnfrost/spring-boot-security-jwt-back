package com.ew.udm.service.user;

import com.ew.udm.controller.req.AuthRequest;
import com.ew.udm.controller.res.LoginResponse;
import com.ew.udm.models.user.User;

public interface AuthService {
    String checkUserRegister(User user);
    User register(User userToAdd, int groupId) throws Exception;
    LoginResponse login(AuthRequest authRequest);
    String refresh(String oldToken);
}
