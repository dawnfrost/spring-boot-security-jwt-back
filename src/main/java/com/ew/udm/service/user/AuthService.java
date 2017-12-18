package com.ew.udm.service.user;

import com.ew.udm.models.user.User;

public interface AuthService {
    String checkUserRegister(User user);
    User register(User userToAdd, int groupId) throws Exception;
    String login(String username, String password, int expireDays, String userAgent);
    String refresh(String oldToken);
}
