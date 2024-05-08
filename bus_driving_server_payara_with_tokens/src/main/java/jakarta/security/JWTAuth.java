package jakarta.security;

import common.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.RememberMeCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;

//This is a mega filter that executes FIRST in each request
public class JWTAuth implements HttpAuthenticationMechanism {


    private final InMemoryIdentityStore identity;

    @Inject
    public JWTAuth(InMemoryIdentityStore identity) {
        this.identity = identity;
    }

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) {
        CredentialValidationResult c = CredentialValidationResult.INVALID_RESULT;

        //when the request does not require authentication, we do nothing, and skip the authentication process (no need to validate)
        if (!httpMessageContext.isProtected()) return httpMessageContext.doNothing();

        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            String[] values = header.split(Constants.BLANK_SPACE);

            if (values[0].equalsIgnoreCase(Constants.BEARER)) {
                try {
                    c = identity.validate(new RememberMeCredential(values[1]));
                } catch (ExpiredJwtException e) { //if the token is expired, we return an invalid result and a 401 status code
                    return httpMessageContext.responseUnauthorized();
                } catch (Exception e) {
                    c = CredentialValidationResult.NOT_VALIDATED_RESULT;
                }
            }
        }
        return httpMessageContext.notifyContainerAboutLogin(c);
    }
}
