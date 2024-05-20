package com.example.server.ui.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.security.PublicKey;

@Service
public class JwtService {

    private final PublicKey publicKey;

    public JwtService(@Qualifier("getPublicKey") PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public Claims extractJwtClaims(String token){
        return Jwts
                .parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
