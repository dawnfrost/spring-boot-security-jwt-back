package com.ew.udm.controller;

import com.ew.udm.configs.AuthenticationAware;
import com.ew.udm.configs.JwtHeaderConfig;
import com.ew.udm.controller.req.AuthRequest;
import com.ew.udm.controller.res.LoginResponse;
import com.ew.udm.models.user.User;
import com.ew.udm.service.user.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final AuthService authService;
    private final static Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest auth,
                                                       @RequestHeader(value = "${jwt.device}") String userAgent,
                                                       HttpServletRequest request) throws AuthenticationException {
        String remoteHost = AuthenticationAware.getRemoteHost(request);
        auth.setUserAgent(userAgent);
        auth.setRemoteHost(remoteHost);
        logger.info("用户登录：" + auth);
        LoginResponse response = this.authService.login(auth);
        logger.info(response);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAccessToken(
            @RequestHeader(value = "${jwt.header}") String token,
            @RequestHeader(value = "${jwt.device}") String deviceType,
            Principal principal) throws AuthenticationException {
        String refreshedToken = this.authService.refresh(token);
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
        String refreshedToken = this.authService.refresh(refreshToken);
        if (refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(refreshedToken);
        }
    }

    @RequestMapping(value = "/register/{groupId}", method = RequestMethod.POST)
    public User register(@RequestBody User addedUser, @PathVariable int groupId) throws Exception {
        return this.authService.register(addedUser, groupId);
    }

}
