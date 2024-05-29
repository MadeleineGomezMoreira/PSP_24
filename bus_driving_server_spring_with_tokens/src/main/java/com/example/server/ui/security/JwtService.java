package com.example.server.ui.security;

import com.example.server.common.Constants;
import com.example.server.domain.exception.InvalidTokenException;
import com.example.server.domain.exception.TokenExpiredException;
import com.example.server.ui.model.TokenPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {

    @Value("${application.security.jwt.access-token.expiration}")
    private int accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private int refreshTokenExpiration;

    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    public JwtService(@Qualifier("getPublicKey") PublicKey publicKey, @Qualifier("getPrivateKey") PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public Claims extractJwtClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public TokenPair generateTokens(String username, String authorities) {
        String accessToken = generateAccessToken(username, authorities);
        String refreshToken = generateRefreshToken(username, authorities);
        return new TokenPair(accessToken, refreshToken);
    }

    private String generateAccessToken(String username, String authorities) {
        return Jwts.builder()
                .subject(username)
                .claim(Constants.AUTHORITIES_LOWER_CASE, authorities)
                .signWith(privateKey)
                .expiration(Date.from(LocalDateTime.now().plusSeconds(accessTokenExpiration).atZone(ZoneId.systemDefault()).toInstant()))
                .compact();
    }

    private String generateRefreshToken(String username, String authorities) {
        return Jwts.builder()
                .subject(username)
                .claim(Constants.AUTHORITIES_LOWER_CASE, authorities)
                .signWith(privateKey)
                .expiration(Date.from(LocalDateTime.now().plusSeconds(refreshTokenExpiration).atZone(ZoneId.systemDefault()).toInstant()))
                .compact();
    }

    public TokenPair refreshToken(String refreshToken) {
        Claims claims = extractJwtClaims(refreshToken);

        if(claims == null) {
            throw new InvalidTokenException(Constants.INVALID_REFRESH_TOKEN);
        }

        //check if the token is expired
        Date tokenExpiration = claims.getExpiration();
        if (tokenExpiration.after(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))) {
            String username = claims.getSubject();
            String authorities = (String) claims.get(Constants.AUTHORITIES_LOWER_CASE);
            return generateTokens(username, authorities);
        } else {
            throw new TokenExpiredException(Constants.EXPIRED_REFRESH_TOKEN);
        }
    }

}
