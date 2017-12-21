package com.ew.udm.service.user;

import com.ew.udm.configs.JwtHeaderConfig;
import com.ew.udm.controller.req.AuthRequest;
import com.ew.udm.controller.res.LoginResponse;
import com.ew.udm.models.user.UserToken;
import com.ew.udm.models.user.UserWithRole;
import com.ew.udm.service.mapper.UserTokenMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    private static final Logger logger = LogManager.getLogger(JwtTokenUtil.class);

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String USER_EXPIRE_DATE = "u_edt";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;

    private final JwtHeaderConfig jwtHeaderConfig;

    private final UserTokenMapper userTokenMapper;

    @Autowired
    public JwtTokenUtil(JwtHeaderConfig jwtHeaderConfig, UserTokenMapper userTokenMapper) {
        this.jwtHeaderConfig = jwtHeaderConfig;
        this.userTokenMapper = userTokenMapper;
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public SecretKey generateKey(String userAgent) {
        SecretKey key = null;
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            String jwtSecret = this.secret + userAgent;
            byte[] encodedKey = md.digest(jwtSecret.getBytes());
            key = new SecretKeySpec(encodedKey, signatureAlgorithm.getJcaName());
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage() + " in function generateKey(" + userAgent + ")");
        }

        return key;
    }

    private UserToken generateUserRefreshToken(int userId, AuthRequest authRequest) {
        UserToken token = userTokenMapper.selectByUserIdAndAgent(userId, authRequest.getUserAgent());
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, authRequest.getRememberDays());
        Date expires = calendar.getTime();

        if (token == null) {
            token = new UserToken();
            token.setUserId(userId);
            token.setUserAgent(authRequest.getUserAgent());

            token.setRemoteHost(authRequest.getRemoteHost());
            token.setRefreshToken(generateUUID());
            token.setRefreshTokenCreateTime(now);
            token.setRefreshTokenExpireTime(expires);
            userTokenMapper.insertSelective(token);
        } else {
            token.setRemoteHost(authRequest.getRemoteHost());
            token.setRefreshToken(generateUUID());
            token.setRefreshTokenCreateTime(now);
            token.setRefreshTokenExpireTime(expires);
            userTokenMapper.updateByPrimaryKeySelective(token);
        }

        return token;
    }

    public JwtHeaderConfig getJwtHeaderConfig() {
        return jwtHeaderConfig;
    }

    public LoginResponse generateNewToken(final UserWithRole user, final AuthRequest authRequest) {
        UserToken refreshToken = generateUserRefreshToken(user.getId(), authRequest);
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, expiration);
        Date expires = calendar.getTime();
        SecretKey key = generateKey(authRequest.getUserAgent());
        JwtToken jwtToken = new JwtToken(user, now, expires, refreshToken.getRefreshTokenExpireTime(), user.getExpireTime());
        String accessToken = jwtToken.getBuilder().signWith(SignatureAlgorithm.HS512, key).compact();
        return new LoginResponse(
                accessToken, jwtToken.getCreated(), jwtToken.getTokenExp(),
                refreshToken.getRefreshToken(),
                refreshToken.getRefreshTokenCreateTime(),
                refreshToken.getRefreshTokenExpireTime());
    }

    public JwtToken parseToken(String token, String userAgent) {
        Claims claims = Jwts.parser().setSigningKey(generateKey(userAgent)).parseClaimsJws(token).getBody();
        return new JwtToken(claims);
    }

    public String getUsernameFromToken(final String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public String getUsernameFromToken(final Claims claims) {
        return claims.getSubject();
    }

    public Date getCreatedDateFromToken(final Claims claims) {
        return new Date((Long) claims.get(CLAIM_KEY_CREATED));
    }

    public Date getExpirationDateFromToken(final Claims claims) {
        return claims.getExpiration();
    }

    private Date getUserExpireDateFromToken(final Claims claims) {
        return new Date((Long) claims.get(USER_EXPIRE_DATE));
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.error(e.getMessage());
            claims = null;
        }
        return claims;
    }

    private Boolean isTokenExpired(final Claims claims) {
        final Date expiration = getExpirationDateFromToken(claims);
        return expiration.before(new Date());
    }

    private boolean isUserExpired(final Claims claims) {
        final Date expiration = getUserExpireDateFromToken(claims);
        return expiration.before(new Date());
    }


    public boolean canTokenBeRefreshed(final String token) {
        Claims claims = getClaimsFromToken(token);
        return !isTokenExpired(claims) && !isUserExpired(claims);
    }

    public String refreshToken(String token) {
        String refreshedToken = "";
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            //refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return (claims != null && !isTokenExpired(claims)) && !isUserExpired(claims);
    }
}
