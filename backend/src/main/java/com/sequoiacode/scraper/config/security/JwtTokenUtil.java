package com.sequoiacode.scraper.config.security;


import com.sequoiacode.scraper.domain.JwtUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    private static long JWT_TOKEN_VALIDITY ;
    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expiration}")
    private void TokenValidity(long time){
        JWT_TOKEN_VALIDITY = time;
    }

    String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(JwtUser jwtUser) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, jwtUser.getUsername());
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + JWT_TOKEN_VALIDITY );
    }
    public String refreshToken(String token) {
        final Date createdDate = DefaultClock.INSTANCE.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    Boolean validateToken(String token, JwtUser jwtUser) {
        final String username = getUsernameFromToken(token);
        boolean isTokenValid =  (username.equals(jwtUser.getUsername()) && !isTokenExpired(token));
        if(isTokenValid){
            refreshToken(token);
        }
        return isTokenValid;
    }

}
