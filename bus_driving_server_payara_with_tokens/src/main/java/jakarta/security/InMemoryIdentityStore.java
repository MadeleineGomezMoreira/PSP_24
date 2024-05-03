package jakarta.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.Constants;
import domain.dto.CredentialVerificationDTO;
import domain.exception.CredentialValidationFailedException;
import domain.exception.TokenExpiredException;
import domain.model.AccountRole;
import domain.usecases.driver.VerifyDriverRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import jakarta.di.KeyProvider;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.RememberMeCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static jakarta.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

public class InMemoryIdentityStore implements IdentityStore {


    @Override
    public int priority() {
        return 10;
    }

    private final KeyProvider keyProvider;
    private final VerifyDriverRole verify;

    @Inject
    public InMemoryIdentityStore(KeyProvider keyProvider, VerifyDriverRole verify) {
        this.keyProvider = keyProvider;
        this.verify = verify;
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {

        if (credential instanceof RememberMeCredential jwt) {

            String authToken = jwt.getToken();

            try {
                Jws<Claims> jws = Jwts.parser()
                        .verifyWith(keyProvider.key())
                        .build()
                        .parseSignedClaims(authToken);

                String username = jws.getPayload().getSubject();
                String roleEncoded = jws.getPayload().get(Constants.ROLE_LOWER_CASE, String.class);

                // Decode the role from Base64
                byte[] bytes = Decoders.BASE64.decode(roleEncoded);
                String role = new String(bytes, StandardCharsets.UTF_8);

                CredentialVerificationDTO tokenCredential = new CredentialVerificationDTO(username, role);

                //verify user's role
                verify.verifyRole(tokenCredential);
                //if there are no exceptions, we will return the CredentialValidationResult with the role
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
