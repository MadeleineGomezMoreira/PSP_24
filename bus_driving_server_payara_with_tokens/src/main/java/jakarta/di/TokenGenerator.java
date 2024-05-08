package jakarta.di;

import common.Constants;
import domain.exception.InvalidTokenException;
import domain.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.inject.Inject;
import jakarta.model.TokenPair;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class TokenGenerator {

    private final SecretKey key;

    @Inject
    public TokenGenerator(SecretKey key) {
        this.key = key;
    }

    public TokenPair generateTokens(String username, String role) {

        String accessToken = generateAccessToken(username, role, key);
        String refreshToken = generateRefreshToken(username, role, key);

        return new TokenPair(accessToken, refreshToken);
    }

    public TokenPair refreshTokens(String refreshToken) {
        //verify the token
        Jws<Claims> refreshTokenClaims = validateToken(refreshToken);

        if (refreshTokenClaims != null) {

            //we extract the claims
            Date expiryDate = refreshTokenClaims.getPayload().getExpiration();
            String username = refreshTokenClaims.getPayload().getSubject();
            String role = refreshTokenClaims.getPayload().get(Constants.ROLE_LOWER_CASE, String.class);

            //check if it's expired
            if (expiryDate != null && expiryDate.after(Date.from(Instant.now()))) {
                //we generate the new tokens
                String newAccessToken = generateAccessToken(username, role, key);
                String newRefreshToken = generateRefreshToken(username, role, key);
                return new TokenPair(newAccessToken, newRefreshToken);
            }
        }
        //if it is, throw an exception
        throw new TokenExpiredException(Constants.TOKEN_EXPIRED);
    }

    public Jws<Claims> validateToken(String refreshToken) throws ExpiredJwtException {
            //here I'll verify the token's signature
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(refreshToken);
    }

    private String generateAccessToken(String username, String role, SecretKey key) {
        return Jwts.builder()
                .subject(username)
                .expiration(Date.from(LocalDateTime.now().plusSeconds(Constants.ACCESS_TOKEN_EXPIRATION_SECONDS).atZone(ZoneId.systemDefault()).toInstant()))
                .claim(Constants.USER_LOWER_CASE, username)
                .claim(Constants.ROLE_LOWER_CASE, role)
                .signWith(key).compact();
    }

    private String generateRefreshToken(String username, String role, SecretKey key) {
        return Jwts.builder()
                .subject(username)
                .expiration(Date.from(LocalDateTime.now().plusSeconds(Constants.REFRESH_TOKEN_EXPIRATION_SECONDS).atZone(ZoneId.systemDefault()).toInstant()))
                .claim(Constants.USER_LOWER_CASE, username)
                .claim(Constants.ROLE_LOWER_CASE, role)
                .signWith(key).compact();
    }
}
