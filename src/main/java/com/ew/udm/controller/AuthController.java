package com.ew.udm.controller;

import com.ew.udm.controller.req.AuthRequest;
import com.ew.udm.controller.res.LoginResponse;
import com.ew.udm.models.user.User;
import com.ew.udm.service.user.AuthService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthRequest auth,
            @RequestHeader(value = "User-Agent") String userAgent) throws AuthenticationException {
        final String token = authService.login(auth.getUsername(), auth.getPassword(), auth.getRememberDays(), userAgent);
        LoginResponse response = new LoginResponse(token);
        response.setRefreshToken("aaaaddd");
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAccessToken(
            @RequestHeader(value = "${jwt.header}") String token,
            @RequestHeader(value = "${jwt.device}") String deviceType,
            Principal principal) throws AuthenticationException {
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(refreshedToken);
        }
    }

    @RequestMapping(value = "/refresh/{refreshToken}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAccessTokenFromRefreshToken(
            @PathVariable String refreshToken,
            @RequestHeader(value = "${jwt.device}") String deviceType) throws AuthenticationException {
        String refreshedToken = authService.refresh(refreshToken);
        if (refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(refreshedToken);
        }
    }

    @RequestMapping(value = "/register/{groupId}", method = RequestMethod.POST)
    public User register(@RequestBody User addedUser, @PathVariable int groupId) throws Exception {
        return authService.register(addedUser, groupId);
    }

}
