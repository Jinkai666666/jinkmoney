package com.example.jinkmoney.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    // 秘钥（随意写一串字符串，但不要泄漏）
    private static final String SECRET_KEY = "jinkmoney-secret-key";

    // Token 有效期（单位：毫秒）→ 1 天
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // 生成 Token
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 存入用户名
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 签名算法 + 密钥
                .compact(); // 压缩成字符串
    }

    // 解析 Token：获取用户名
    public static String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null; // 解析失败（过期或无效）
        }
    }

    // 校验 Token 是否有效
    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
