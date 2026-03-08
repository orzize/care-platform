package com.care.platform.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {

    // 密钥 (绝对不能泄露)
    private static final String SECRET = "CarePlatformSecretKey2026";
    // 过期时间：设为 7 天
    private static final long EXPIRATION = 1000 * 60 * 60 * 24 * 7;

    /**
     * 生成 JWT Token
     */
    public static String generateToken(String openid) {
        return Jwts.builder()
                .setSubject(openid) // 把 openid 存进 token 里
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET) // 使用 HS256 算法加密
                .compact();
    }

    /**
     * 🌟 新增：从 Token 中解析出 openid (验钞机)
     */
    public static String getOpenidFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // 如果 Token 过期或被篡改，就会抛出异常，我们返回 null
            return null;
        }
    }
}