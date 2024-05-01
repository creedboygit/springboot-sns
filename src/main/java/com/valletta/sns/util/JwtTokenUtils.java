package com.valletta.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtTokenUtils {

//    private static final String SECRET_KEY = "12314123der134";

//    @Value("${jwt.secret}")
//    private String SECRET_KEY;

//    @Value("${jwt.expiration_time}")
//    private long EXPIRED_TIME_MS;

    public static String generateToken(String userName, String secretKey, long expiredTimeMs) {
//    public String generateToken(String userName, String key) {
        return Jwts.builder()
//            .claim("userName", userName)
            .subject(userName)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredTimeMs))
            .signWith(getSingingKey(secretKey))
            .compact();
    }

    public static String getUserName(String token, String key) {
        return extractClaims(token, key).get("userName", String.class);
    }

    public static boolean isExpired(String token, String key) {
        Date expiration = extractClaims(token, key).getExpiration();
        return expiration.before(new Date());
    }

    private static Claims extractClaims(String token, String key) {
        return Jwts.parser().verifyWith(getSingingKey(key))
            .build().parseSignedClaims(token).getPayload();
    }

    private static SecretKey getSingingKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
