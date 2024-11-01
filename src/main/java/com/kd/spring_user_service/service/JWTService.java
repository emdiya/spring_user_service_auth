package com.kd.spring_user_service.service;

import com.kd.spring_user_service.model.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret-key}")
    private String secretKey;


    public String generateToken(CustomUserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        claims.put("email",userDetails.getEmail());
        claims.put("username", userDetails.getUsername());
        claims.put("roleId", userDetails.getRoleId());

        String subject = userDetails.getUsername() != null ? userDetails.getUsername() : userDetails.getEmail();

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 2000))
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String extractUsername(String token) {
        return  extractClaim(token, Claims::getSubject);
    }

    public Integer extractRoleId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roleId", Integer.class);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, CustomUserDetails userDetails) {
        final String subject = extractUsername(token);
        return ((subject.equals(userDetails.getUsername()) || subject.equals(userDetails.getEmail()))
                && !isTokenExpired(token));
    }
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
