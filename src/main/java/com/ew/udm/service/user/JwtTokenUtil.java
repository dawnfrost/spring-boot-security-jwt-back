package com.ew.udm.service.user;
import com.ew.udm.models.user.User;
import com.ew.udm.models.user.UserToken;
import com.ew.udm.models.user.UserWithRole;
import com.ew.udm.service.mapper.UserTokenMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
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

    private final UserTokenMapper userTokenMapper;

    @Autowired
    public JwtTokenUtil(UserTokenMapper userTokenMapper) {
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

    private UserToken generateUserRefreshToken(int userId, String userAgent, int expireDays) {
        UserToken token = userTokenMapper.selectByUserIdAndAgent(userId, userAgent);
        if (token == null) {
            token = new UserToken();
            token.setUserId(userId);
            token.setUserAgent(userAgent);
            token.setRefreshToken(generateUUID());
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DATE, expireDays);
            Date expires = calendar.getTime();
            token.setRefreshTokenCreateTime(now);
            token.setRefreshTokenExpireTime(expires);
            userTokenMapper.insertSelective(token);
        } else {
            token.setRefreshToken(generateUUID());
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.DATE, expireDays);
            Date expires = calendar.getTime();
            token.setRefreshTokenCreateTime(now);
            token.setRefreshTokenExpireTime(expires);
            userTokenMapper.updateByPrimaryKeySelective(token);
        }

        return token;
    }

    public String generateNewToken(UserWithRole user, int expireDays, String userAgent) {
        UserToken refreshToken = generateUserRefreshToken(user.getId(), userAgent, expireDays);
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, expiration);
        Date expires = calendar.getTime();
        SecretKey key = generateKey(userAgent);
        JwtToken token = new JwtToken(user, now, expires, refreshToken.getRefreshTokenExpireTime(), user.getExpireTime());
        return token.getBuilder().signWith(SignatureAlgorithm.HS512, key).compact();
    }

    public JwtToken parseToken(String token, String userAgent) {
        try {
            Claims claims = Jwts.parser().setSigningKey(generateKey(userAgent)).parseClaimsJws(token).getBody();
            return new JwtToken(claims);
        } catch (Exception e) {
            logger.error(e.getMessage() + " in function parseToken(" + token + ", " + userAgent + ")");
        }

        return null;
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
