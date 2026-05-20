package br.com.ecomerce.ecomerce.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {

        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

//    public boolean tokenValido(String token){
//        Claims claims = getClaims(token);
//        if (claims != null){
//            String userName = claims.getSubject();
//            Date expirationDate = claims.getExpiration();
//            Date now = new Date(System.currentTimeMillis());
//            if (userName != null && expirationDate != null && now.before(expirationDate)){
//                return true;
//            }
//        }
//
//        return false;
//    }

    public boolean tokenValido(String token) {
        try {
            Claims claims = getClaims(token);

            String userName = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date now = new Date();

            return userName != null
                    && expirationDate != null
                    && now.before(expirationDate);

        } catch (Exception e) {
            return false;
        }
    }

//    public String getUserName(String token){
//        Claims claims = getClaims(token);
//        if (claims != null){
//           return  claims.getSubject();
//        }
//        return null;
//        }

    public String getUserName(String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

//    private Claims getClaims(String token) {
//        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
//    }

private Claims getClaims(String token) {
    Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
}


//    public boolean tokenValido(String token) {
//        Claims claims = getClaims(token);
//        if (claims != null) {
//            String username = claims.getSubject();
//            Date expirationDate = claims.getExpiration();
//            Date now = new Date(System.currentTimeMillis());
//            if (username != null && expirationDate != null && now.before(expirationDate)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public String getUsername(String token) {
//        Claims claims = getClaims(token);
//        if (claims != null) {
//            return claims.getSubject();
//        }
//        return null;
//    }
//
//    private Claims getClaims(String token) {
//        try {
//            return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
//        }
//        catch (Exception e) {
//            return null;
//        }
//    }
}

