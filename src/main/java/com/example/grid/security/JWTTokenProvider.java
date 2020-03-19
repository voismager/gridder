package com.example.grid.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JWTTokenProvider {
    private static final long EXPIRATION_TIME = 864_000_000;
    private static final byte[] SECRET = "SECRET".getBytes();
    private static final String TOKEN_PREFIX = "Bearer ";

    private final String secretKey;

    public JWTTokenProvider() {
        this.secretKey = Base64.getEncoder().encodeToString(SECRET);
    }

    public String create(String username) {
        Claims claims = Jwts.claims().setSubject(username);

        Date now = new Date();
        Date until = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(until)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolve(HttpServletRequest req) {
        String bearer = req.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearer != null && bearer.startsWith(TOKEN_PREFIX)) {
            return bearer.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
