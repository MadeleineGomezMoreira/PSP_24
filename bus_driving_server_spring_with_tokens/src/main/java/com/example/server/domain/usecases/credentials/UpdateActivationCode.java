package com.example.server.domain.usecases.credentials;

import com.example.server.common.Constants;
import com.example.server.data.model.DriverCredentialEntity;
import com.example.server.data.repositories.CredentialRepository;
import com.example.server.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateActivationCode {

    private final CredentialRepository credentialRepository;

    public void updateActivationCode(String email, String activationCode) {
        //get the DriverCredential object
        DriverCredentialEntity credential = credentialRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND_WITH_EMAIL_ERROR));
        //update it
        credential.setActivationCode(activationCode);
        credential.setActivationDate(LocalDateTime.now());
        credentialRepository.save(credential);
    }
}
