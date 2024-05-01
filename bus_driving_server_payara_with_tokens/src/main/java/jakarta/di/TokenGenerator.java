package jakarta.di;

import common.Constants;
import domain.exception.InvalidTokenException;
import domain.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import jakarta.inject.Inject;
import jakarta.model.TokenPair;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class TokenGenerator {

    private final KeyProvider keyProvider;

    @Inject
    public TokenGenerator(KeyProvider keyProvider) {
        this.keyProvider = keyProvider;
    }

    public TokenPair generateTokens(String username, String role) {
        SecretKey key = keyProvider.key();

        String accessToken = generateAccessToken(username, role, key);
        String refreshToken = generateRefreshToken(username, role, key);

        return new TokenPair(accessToken, refreshToken);
    }

    public TokenPair refreshTokens(String refreshToken) {
        //verify the token
        Jws<Claims> refreshTokenClaims = validateRefreshToken(refreshToken);

        if (refreshTokenClaims != null) {

            //we extract the claims
            Date expiryDate = refreshTokenClaims.getPayload().getExpiration();
            String username = refreshTokenClaims.getPayload().getSubject();
            String role = refreshTokenClaims.getPayload().get(Constants.ROLE_LOWER_CASE, String.class);

            //check if it's expired
            if (expiryDate != null && expiryDate.after(Date.from(Instant.now()))) {
                //we generate the new tokens
                SecretKey key = keyProvider.key();
                String newAccessToken = generateAccessToken(username, role, key);
                String newRefreshToken = generateRefreshToken(username, role, key);
                return new TokenPair(newAccessToken, newRefreshToken);
            }
        }
        //if it is, throw an exception
        throw new TokenExpiredException(Constants.TOKEN_EXPIRED);
    }

    private Jws<Claims> validateRefreshToken(String refreshToken) {
        try {
            //here I'll verify the token's signature
            return Jwts.parser().verifyWith(keyProvider.key()).build().parseSignedClaims(refreshToken);
        } catch (Exception e) {
            throw new InvalidTokenException(Constants.INVALID_TOKEN);
        }
    }

    private String generateAccessToken(String username, String role, SecretKey key) {
        byte[] bytes = role.getBytes(StandardCharsets.UTF_8);
        String roleEncoded = Encoders.BASE64.encode(bytes);

        return Jwts.builder()
                .subject(username)
                .expiration(Date.from(LocalDateTime.now().plusSeconds(Constants.ACCESS_TOKEN_EXPIRATION_SECONDS).atZone(ZoneId.systemDefault()).toInstant()))
                .claim(Constants.USER_LOWER_CASE, username)
                .claim(Constants.ROLE_LOWER_CASE, roleEncoded)
                .signWith(key).compact();
    }

    private String generateRefreshToken(String username, String role, SecretKey key) {
        byte[] bytes = role.getBytes(StandardCharsets.UTF_8);
        String roleEncoded = Encoders.BASE64.encode(bytes);

        return Jwts.builder()
                .subject(username)
                .expiration(Date.from(LocalDateTime.now().plusSeconds(Constants.REFRESH_TOKEN_EXPIRATION_SECONDS).atZone(ZoneId.systemDefault()).toInstant()))
                .claim(Constants.USER_LOWER_CASE, username)
                .claim(Constants.ROLE_LOWER_CASE, roleEncoded)
                .signWith(key).compact();
    }


//    public String createJWT(String username, String role) {
//        SecretKey key = keyProvider.key();
//
//        byte[] bytes = role.getBytes(StandardCharsets.UTF_8);
//        String roleEncoded = Encoders.BASE64.encode(bytes);
//
//        return Jwts.builder()
//                .subject(username)
//                .expiration(Date
//                        .from(LocalDateTime.now().plusSeconds(1000).atZone(ZoneId.systemDefault())
//                                .toInstant()))
//                .claim(Constants.USER_LOWER_CASE, username)
//                .claim(Constants.ROLE_LOWER_CASE, roleEncoded)
//                .signWith(key).compact();
//    }
}
