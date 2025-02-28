package com.TaskManager.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {
    //конечно надо хранить в application.properties ключ в ком. разработке, для наглядности поставил тут
    private static final String SECRET_KEY = "very_secret_key_which_should_be_very_long";
    SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    //генерация токена, в клеймы кладем роли и мейл
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles",rolesList);
        claims.put("email",userDetails.getUsername());
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(Date.from(new Date().toInstant().plus(Duration.ofHours(2))))
                .signWith(key)
                .compact();
    }
    public String getUsername(String token){
        try {
            return getAllClaimsFromToken(token).getSubject();
        }catch (NullPointerException ex){
            return null;
        }

    }
    public List<String> getRoles(String token){
        try {
            return getAllClaimsFromToken(token).get("roles", List.class);
        }catch (NullPointerException ex){
            return null;
        }
    }

    private Claims getAllClaimsFromToken(String token){
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (SignatureException e){
            System.err.println("Invalid JWT signature. " + e.getMessage());
            return null;
        }
    }


}

