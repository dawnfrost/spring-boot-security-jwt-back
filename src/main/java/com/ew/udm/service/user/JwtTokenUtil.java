package com.ew.udm.service.user;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String USER_EXPIRE_DATE = "u_edt";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

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
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(final Claims claims) {
        final Date expiration = getExpirationDateFromToken(claims);
        return expiration.before(new Date());
    }

    private boolean isUserExpired(final Claims claims) {
        final Date expiration = getUserExpireDateFromToken(claims);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        JwtUser jwtUser = (JwtUser)userDetails;
        claims.put(USER_EXPIRE_DATE, jwtUser.getExpireTime());
        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean canTokenBeRefreshed(final String token) {
        Claims claims = getClaimsFromToken(token);
        return !isTokenExpired(claims) && !isUserExpired(claims);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
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
