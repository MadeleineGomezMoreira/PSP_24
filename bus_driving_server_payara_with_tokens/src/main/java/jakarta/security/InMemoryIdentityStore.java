package jakarta.security;

import common.Constants;
import domain.dto.CredentialVerificationDTO;
import domain.exception.CredentialValidationFailedException;
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

    //TODO: hacer algo aquí para que se lancen excepciones en el propio método sin recogerlas (no se mappean aqui con jaxrs)

    @Override
    public CredentialValidationResult validate(Credential credential) {

        if (credential instanceof RememberMeCredential jwt) {

            String authToken = jwt.getToken();

            try {
                Jws<Claims> jws = tokenGenerator.validateToken(authToken);

                String username = jws.getPayload().getSubject();
                String role = jws.getPayload().get(Constants.ROLE_LOWER_CASE, String.class);

                CredentialVerificationDTO tokenCredential = new CredentialVerificationDTO(username, role);

                return new CredentialValidationResult(Constants.ROLE_LOWER_CASE, Collections.singleton(tokenCredential.getRole()));

            } catch (ExpiredJwtException e) {
                throw new TokenExpiredException(Constants.TOKEN_EXPIRED);
            } catch (ClassCastException e) {
                throw new CredentialValidationFailedException(Constants.CREDENTIAL_VALIDATION_FAILED_ERROR + credential);
            } catch (Exception e) {
                return INVALID_RESULT;
            }
        } else {
            throw new CredentialValidationFailedException(Constants.CREDENTIAL_VALIDATION_FAILED_ERROR + credential);
        }
    }
}
