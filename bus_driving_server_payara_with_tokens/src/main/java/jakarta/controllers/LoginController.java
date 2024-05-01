package jakarta.controllers;

import common.Constants;
import domain.dto.LoginDTO;
import domain.usecases.credentials.LoginAndGetRole;
import jakarta.di.TokenGenerator;
import jakarta.inject.Inject;
import jakarta.model.TokenPair;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(Constants.EMPTY_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {

    private final LoginAndGetRole loginAndGetRole;
    private final TokenGenerator tokenGenerator;


    @Inject
    public LoginController(LoginAndGetRole loginAndGetRole, TokenGenerator tokenGenerator) {
        this.loginAndGetRole = loginAndGetRole;
        this.tokenGenerator = tokenGenerator;
    }

    @POST
    @Path(Constants.LOGIN_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO credential) {
        String role = loginAndGetRole.login(credential);
        String username = credential.getUsername();

        TokenPair tokenPair = tokenGenerator.generateTokens(username, role);

        //I will return the tokens in different headers
        return Response.ok()
                .header(Constants.AUTHORIZATION, Constants.BEARER + tokenPair.getAccessToken())
                .header(Constants.REFRESH_TOKEN, tokenPair.getRefreshToken())
                .build();
    }

    @POST
    @Path(Constants.LOGOFF_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logOff() {
        //we cannot invalidate tokens as they are self-contained and the client must get rid of them
        return Response.ok().entity(Constants.LOGGING_OFF_WAS_SUCCESSFUL).build();
    }

    @POST
    @Path(Constants.REFRESH_TOKEN_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refreshToken(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        //I will extract the access and refresh tokens from the headers
        String refreshToken = authorizationHeader.substring(Constants.BEARER.length()).trim();

        //I will return both a new access token and a new refresh token
        TokenPair tokenPair = tokenGenerator.refreshTokens(refreshToken);

        return Response.ok()
                .header(Constants.AUTHORIZATION, Constants.BEARER + tokenPair.getAccessToken())
                .header(Constants.REFRESH_TOKEN, tokenPair.getRefreshToken())
                .build();
    }
}
