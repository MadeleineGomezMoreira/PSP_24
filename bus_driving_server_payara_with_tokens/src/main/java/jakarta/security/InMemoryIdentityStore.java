package jakarta.security;

import common.Constants;
import domain.dto.CredentialVerificationDTO;
import domain.exception.CredentialValidationFailedException;
import domain.exception.InvalidTokenException;
import domain.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.di.TokenGenerator;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.RememberMeCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Set;

import static jakarta.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

public class InMemoryIdentityStore implements IdentityStore {


    @Override
    public int priority() {
        return 10;
    }

    private final SecretKey key;
    private final TokenGenerator tokenGenerator;

    @Inject
    public InMemoryIdentityStore(SecretKey key, TokenGenerator tokenGenerator) {
        this.key = key;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public CredentialValidationResult validate(Credential credential) throws ExpiredJwtException {

        if (credential instanceof RememberMeCredential jwt) {

            String authToken = jwt.getToken();
                Jws<Claims> jws = tokenGenerator.validateToken(authToken);

                String username = jws.getPayload().getSubject();
                String role = jws.getPayload().get(Constants.ROLE_LOWER_CASE, String.class);

                //aquí pongo el username y el rol (para que me lo cargue en el principal y pueda mirar lo de RolesAllowed)
                return new CredentialValidationResult(username, Set.of(role));
        } else {
            return INVALID_RESULT;
        }
    }
}
