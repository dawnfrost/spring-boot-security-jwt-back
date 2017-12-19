package com.ew.udm.configs;

import com.ew.udm.models.user.UserRoleMin;
import com.ew.udm.models.user.UserWithRole;
import com.ew.udm.service.mapper.UserMapper;
import com.ew.udm.service.user.ExpireType;
import com.ew.udm.service.user.JwtToken;
import com.ew.udm.service.user.JwtTokenUtil;
import com.ew.udm.service.user.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
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
    private final UserDetailsService userDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.device}")
    private String userAgentHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    public JwtAuthenticationTokenFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String token = authHeader.substring(tokenHead.length()); // The part after "Bearer "
            final String userAgent = request.getHeader(userAgentHeader);
            final SecurityContext context = SecurityContextHolder.getContext();
            JwtToken jwtToken = jwtTokenUtil.parseToken(token, userAgent);
            if (jwtToken != null && context.getAuthentication() == null) {
                ExpireType expireType = jwtToken.checkExpire();
                if (expireType == ExpireType.NONE) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtToken.getSub(), null, jwtToken.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authentication);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, expireType.name());
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }


}
