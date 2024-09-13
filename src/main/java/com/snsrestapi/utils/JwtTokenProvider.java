package com.snsrestapi.utils;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final Long expireTimeMs;
    private String secretKey;
    private String issuer;

    public JwtTokenProvider(UserDetailsService userDetailsService, @Value("${jwt.issuer}") String issuer,
                            @Value("${jwt.secret-key}") String secretKey,
                            @Value("${jwt.expireTimeMs}") Long expireTimeMs) {
        this.userDetailsService = userDetailsService;
        this.expireTimeMs = expireTimeMs;
        this.secretKey = secretKey;
        this.issuer = issuer;

    }

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String userName, String authorities) {
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("role", authorities);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public boolean isExpired(String token) {
        Date expiredDate = extractClaims(token).getExpiration();
        return expiredDate.before(new Date());
    }



    //인증객체
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),"", userDetails.getAuthorities());
    }


}
