package com.example.server.domain.usecases.credentials;

import com.example.server.common.Constants;
import com.example.server.data.model.DriverCredentialEntity;
import com.example.server.data.repositories.CredentialRepository;
import com.example.server.domain.exception.AccountAlreadyActivatedException;
import com.example.server.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivateAccountUseCase {

    private final CredentialRepository credentialRepository;

    public boolean activateAccount(String email, String activationCode) {
        //get the DriverCredential object
        DriverCredentialEntity credential = credentialRepository.findByEmail(email).orElseThrow( () -> new NotFoundException("Driver not found"));

        //check if account is already activated
        if (credential.isActivated()) {
            throw new AccountAlreadyActivatedException(Constants.ACCOUNT_ALREADY_ACTIVATED);
        }

        //check if the activation code is suitable (not expired) - expires 5 minutes after creation
        //check against now() = java.time.LocalDateTime.now()
        //check against activationDate + 2 hours (because of the time difference in the mysql database)
        LocalDateTime activationDate = credential.getActivationDate().plusHours(2);
        String decodedActivationCode = URLDecoder.decode(activationCode, StandardCharsets.UTF_8);
        //replace any spaces in the activation code for "+"
        decodedActivationCode = decodedActivationCode.replace(" ", "+");
        boolean codeIsExpired = activationDate.plusMinutes(5).isBefore(LocalDateTime.now());
        boolean codeIsCorrect = decodedActivationCode.equals(activationCode);

        if (!codeIsExpired && codeIsCorrect) {
            //activate the account
            credential.setActivated(true);
            DriverCredentialEntity savedEntity = credentialRepository.save(credential);
            return savedEntity.isActivated();
        } else {
            return false;
        }
    }
}
