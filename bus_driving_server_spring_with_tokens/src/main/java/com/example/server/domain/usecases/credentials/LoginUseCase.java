package com.example.server.domain.usecases.credentials;

import com.example.server.common.Constants;
import com.example.server.data.model.DriverCredentialEntity;
import com.example.server.data.repositories.AuthRepository;
import com.example.server.domain.exception.AccountNotActivatedException;
import com.example.server.domain.exception.AuthenticationFailedException;
import com.example.server.ui.model.LoginInputData;
import com.example.server.ui.model.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;


    public LoginDTO login(LoginInputData userLogin) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        DriverCredentialEntity credential = authRepository.findDriverCredentialEntityByUsername(userLogin.getUsername()).orElseThrow(() -> new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED_USERNAME_ERROR));

        int driverId = credential.getId();
        String role = credential.getRole().getRoleName();

        LoginDTO loginDTO = new LoginDTO(driverId, role);

        if (!credential.isActivated()) {
            throw new AccountNotActivatedException(Constants.ACCOUNT_NOT_ACTIVATED);
        } else {
            return loginDTO;
        }
    }

}
