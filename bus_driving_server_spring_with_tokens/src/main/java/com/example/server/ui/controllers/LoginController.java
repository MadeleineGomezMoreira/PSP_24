package com.example.server.ui.controllers;

import com.example.server.common.Constants;
import com.example.server.domain.usecases.credentials.LoginUseCase;
import com.example.server.ui.model.*;
import com.example.server.ui.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginUseCase loginUseCase;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(Constants.LOGIN_CONTROLLER_PATH + Constants.LOGIN_PATH)
    public LoginOutputData login(@RequestBody LoginInputData login) {

        LoginDTO loginDTO = loginUseCase.login(login);
        TokenPair tokenPair = jwtService.generateTokens(login.getUsername(), loginDTO.getRole());

        return new LoginOutputData(loginDTO.getDriverId(), tokenPair);
    }

    @PostMapping(Constants.LOGIN_CONTROLLER_PATH + Constants.REFRESH_TOKEN_PATH)
    public TokenPair refreshToken(@RequestBody RefreshToken refreshToken) {
        return jwtService.refreshToken(refreshToken.getToken());
    }

}
