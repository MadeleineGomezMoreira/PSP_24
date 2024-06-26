package jakarta.controllers;

import common.Constants;
import domain.usecases.credentials.LoginAndGetRole;
import domain.usecases.driver.GetDriverIdByUsername;
import jakarta.di.TokenGenerator;
import jakarta.inject.Inject;
import jakarta.model.LoginDTO;
import jakarta.model.LoginData;
import jakarta.model.RefreshToken;
import jakarta.model.TokenPair;
import jakarta.model.mappers.JakartaDataMappers;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(Constants.EMPTY_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {

    private final LoginAndGetRole loginAndGetRole;
    private final TokenGenerator tokenGenerator;
    private final JakartaDataMappers dataMappers;
    private final GetDriverIdByUsername getDriverIdByUsername;


    @Inject
    public LoginController(LoginAndGetRole loginAndGetRole, TokenGenerator tokenGenerator, JakartaDataMappers dataMappers, GetDriverIdByUsername getDriverIdByUsername) {
        this.loginAndGetRole = loginAndGetRole;
        this.tokenGenerator = tokenGenerator;
        this.dataMappers = dataMappers;
        this.getDriverIdByUsername = getDriverIdByUsername;
    }

    @POST
    @Path(Constants.LOGIN_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO credential) {
        String role = loginAndGetRole.login(dataMappers.mapLoginDTOToDriverCredential(credential));
        String username = credential.getUsername();

        TokenPair tokenPair = tokenGenerator.generateTokens(username, role);
        int userId = getDriverIdByUsername.getDriverIdByUsername(username);

        LoginData loginData = new LoginData(userId, tokenPair);

        //we will return the tokens as JSON
        return Response.ok(loginData)
                .build();
    }

    @POST
    @Path(Constants.LOGOFF_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logOff() {
        //we cannot invalidate tokens as they are self-contained and the client must get rid of them
        //we could blacklist them, but that would require a fast (non-relational) database (like Redis)
        return Response.ok().entity(Constants.LOGGING_OFF_WAS_SUCCESSFUL).build();
    }

    @POST
    @Path(Constants.REFRESH_TOKEN_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refreshToken(RefreshToken refreshToken) {
        //we will return both a new access token and a new refresh token generated from the old refresh token
        TokenPair tokenPair = tokenGenerator.refreshTokens(refreshToken.getToken());
        return Response.ok(tokenPair)
                .build();
    }
}
