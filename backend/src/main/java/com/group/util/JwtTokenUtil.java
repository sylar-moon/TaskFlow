package com.group.util;

import com.group.security.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifeTime;

    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream().
                map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        claims.put("roles", roles);
        claims.put("mail", userDetails.getMail());
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + jwtLifeTime.toMillis());
        try {
            return Jwts.builder().setClaims(claims).
                    setSubject(userDetails.getUsername()).
                    setIssuedAt(issuedAt).
                    setExpiration(expiration).
                    signWith(getSingingKey(), SignatureAlgorithm.HS256).
                    compact();
        } catch (JwtException e) {
            throw new JwtException("Error parsing JWT token: " + e.getMessage());

        }

    }

    public Jws<Claims> getAllClaimsForToken(String token) {
        return Jwts.parserBuilder().
                setSigningKey(getSingingKey()).
                build().
                parseClaimsJws(token);

    }

    public String getMail(String token) {
        return getAllClaimsForToken(token).getBody().get("mail", String.class);
    }

    public List<String> getRoles(String token) {
        return getAllClaimsForToken(token).getBody().get("roles", List.class);
    }

    private Key getSingingKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

}
