package com.ew.udm.service.user;
import com.ew.udm.models.user.User;
import com.ew.udm.models.user.UserToken;
import com.ew.udm.service.mapper.UserTokenMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
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

    public String generateAccessToken(UserToken userToken, JwtUser user) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, expiration);
        Date expires = calendar.getTime();
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, user.getUsername());
        claims.put(CLAIM_KEY_CREATED, now);
        claims.put(USER_EXPIRE_DATE, user.getExpireTime());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expires)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsernameFromToken(final String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getSubject();
        }

        return null;
    }

    private String getUsernameFromToken(final Claims claims) {
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

    public String generateNewToken(User user, int expireDays, String userAgent) {
        UserToken refreshToken = generateUserRefreshToken(user.getId(), userAgent, expireDays);
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, expiration);
        Date expires = calendar.getTime();
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, user.getUserName());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(USER_EXPIRE_DATE, user.getExpireTime());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expires)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
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
