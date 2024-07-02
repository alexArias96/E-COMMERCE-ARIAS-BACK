package com.arias_code.ecom.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {


    //Generar llave secreta
    public static String generateSecretKey(int length) {
        log.info("Init method generateSecretKey");
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[length];
        secureRandom.nextBytes(key);
        log.info("Secret key: " + Base64.getEncoder().encodeToString(key));
        return Base64.getEncoder().encodeToString(key);
    }

    private final String SECRET_KEY = generateSecretKey(64);

    //gnerar token
    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, getKey());
    }

    private Key getKey() {
        log.info("Init getKey method: " + SECRET_KEY);
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private String createToken(Map<String, Object> claims, String username, Key key) {
        long expirationTime = 3600000; // 1 hour in milliseconds
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);

        log.info("Init getClaim Method Claim: " + claims);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token){
        log.info("Init getAllClaims Method token: " + token);
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
