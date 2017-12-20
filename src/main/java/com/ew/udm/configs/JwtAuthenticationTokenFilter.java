package com.ew.udm.configs;

import com.ew.udm.service.user.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtAuthenticationTokenFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        AuthenticationAware authenticationAware = AuthenticationAware.build(request, this.jwtTokenUtil);
        if (authenticationAware.getStatus() == AuthenticationStatus.NONE) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }

        try {
            if (authenticationAware.getStatus() == AuthenticationStatus.SUCCESS) {
                SecurityContextHolder.getContext().setAuthentication(authenticationAware.getAuthentication());
                chain.doFilter(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, authenticationAware.getStatus().name());
            }
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

}
