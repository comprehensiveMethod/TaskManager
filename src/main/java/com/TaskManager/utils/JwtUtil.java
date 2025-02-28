package com.TaskManager.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.secret.key}")
    private String SECRET_KEY;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Генерирует JWT на основе данных пользователя.
     * В токен добавляются claims (утверждения) с ролями пользователя и его email.
     *
     * @param userDetails объект {@link UserDetails}, содержащий информацию о пользователе
     * @return строка, представляющая JWT
     * @see UserDetails
     * @see GrantedAuthority
     */
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
    /**
     * Извлекает имя пользователя (subject) из JWT.
     *
     * @param token JWT, из которого извлекается информация
     * @return имя пользователя или {@code null}, если токен невалиден
     */
    public String getUsername(String token){
        try {
            return getAllClaimsFromToken(token).getSubject();
        }catch (NullPointerException ex){
            return null;
        }

    }
    /**
     * Извлекает список ролей пользователя из JWT.
     *
     * @param token JWT, из которого извлекается информация
     * @return список ролей или {@code null}, если токен невалиден
     */
    public List<String> getRoles(String token){
        try {
            return getAllClaimsFromToken(token).get("roles", List.class);
        }catch (NullPointerException ex){
            return null;
        }
    }
    /**
     * Извлекает все claims (утверждения) из JWT.
     * Если подпись токена невалидна, метод возвращает {@code null} и выводит сообщение об ошибке.
     * @param token JWT, из которого извлекаются claims
     * @return объект {@link Claims}, содержащий все утверждения, или {@code null}, если токен невалиден
     * @see Claims
     */
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

