package com.example.jinkmoney.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    // 秘钥（自己定义，别外泄）
    private static final String SECRET_KEY = "jinkmoney-secret-key";

    // Token 有效期：1 天
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    /**
     * 生成 Token
     *  userId 存入 JWT 的 "sub"字段
     */
    public static String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // 存入 userId
                .setIssuedAt(new Date())             // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 从 Token 中解析出 userId
     */
    public static Long extractUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return Long.parseLong(claims.getSubject()); // 从 sub 拿 userId
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验 Token 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
