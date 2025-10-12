package com.example.shangting.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "12345678901234567890123456789012"; // 必须 >=32 字节
    private static final long EXPIRATION_MS = 86400000; // 1天

    private final SecretKey key;

    public JwtUtil() {
        // 将 SECRET_KEY 转换成 SecretKey 对象
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // 生成 token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 获取 username
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // 验证 token 是否过期
    public boolean validateToken(String token) {
        return !getClaims(token).getExpiration().before(new Date());
    }

    // 解析 token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
