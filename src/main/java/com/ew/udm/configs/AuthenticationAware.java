package com.ew.udm.configs;

import com.ew.udm.service.user.JwtToken;
import com.ew.udm.service.user.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class AuthenticationAware {
    private AuthenticationStatus status;
    private Authentication authentication;

    private static final Logger logger = LogManager.getLogger(AuthenticationAware.class);

    private AuthenticationAware(AuthenticationStatus status, Authentication authentication) {
        this.status = status;
        this.authentication = authentication;
    }

    private AuthenticationAware(AuthenticationStatus status) {
        this.status = status;
    }

    private static String getRemoteHost(final HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    public static AuthenticationAware build(final HttpServletRequest request, final JwtTokenUtil jwtTokenUtil) {
        JwtHeaderConfig jwtHeaderConfig = jwtTokenUtil.getJwtHeaderConfig();
        String authHeader = request.getHeader(jwtHeaderConfig.getTokenHeader());

        if (authHeader == null) {
            return new AuthenticationAware(AuthenticationStatus.NONE);
        }

        if (!authHeader.startsWith(jwtHeaderConfig.getTokenHead())) {
            return new AuthenticationAware(AuthenticationStatus.TOKEN_ERROR);
        }

        String token = authHeader.substring(jwtHeaderConfig.getTokenHead().length());
        String userAgent = request.getHeader(jwtHeaderConfig.getUserAgentHeader());

        try {
            JwtToken jwtToken = jwtTokenUtil.parseToken(token, userAgent);
            AuthenticationStatus expireType = jwtToken.checkExpire();
            if (expireType == AuthenticationStatus.SUCCESS) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtToken.getSub(), null, jwtToken.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                return new AuthenticationAware(expireType, authentication);
            } else {
                return new AuthenticationAware(expireType);
            }
        } catch (ExpiredJwtException e) {
            logger.error("token(" + token + ")已过期");
            return new AuthenticationAware(AuthenticationStatus.TOKEN_EXPIRE);
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            logger.error("token(" + token + ")无效, 客户端IP:" + getRemoteHost(request));
            return new AuthenticationAware(AuthenticationStatus.TOKEN_ERROR);
        } catch (SignatureException e) {
            logger.error("token(" + token + ")签名错误, 客户端IP:" + getRemoteHost(request));
            return new AuthenticationAware(AuthenticationStatus.TOKEN_SIGNATURE_ERROR);
        }
    }

    public AuthenticationStatus getStatus() {
        return status;
    }

    public void setStatus(AuthenticationStatus status) {
        this.status = status;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
