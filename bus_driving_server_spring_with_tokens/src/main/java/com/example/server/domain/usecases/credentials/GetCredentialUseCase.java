package com.example.server.domain.usecases.credentials;

import com.example.server.common.Constants;
import com.example.server.data.model.DriverCredentialEntity;
import com.example.server.data.repositories.CredentialRepository;
import com.example.server.domain.exception.AccountNotActivatedException;
import com.example.server.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCredentialUseCase {

    private final CredentialRepository credentialRepository;

    public DriverCredentialEntity getCredential(String username) {
        DriverCredentialEntity credential = credentialRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
        if (!credential.isActivated()) {
            throw new AccountNotActivatedException(Constants.ACCOUNT_NOT_ACTIVATED);
        } else {
            return credential;
        }
    }
}
