package com.example.server.domain.usecases.credentials;

import com.example.server.common.Constants;
import com.example.server.data.model.AccountRoleEntity;
import com.example.server.data.model.BusDriverEntity;
import com.example.server.data.model.BusLineEntity;
import com.example.server.data.model.DriverCredentialEntity;
import com.example.server.data.repositories.CredentialRepository;
import com.example.server.data.repositories.DriverRepository;
import com.example.server.data.repositories.LineRepository;
import com.example.server.domain.exception.DriverValidationException;
import com.example.server.domain.usecases.email.SendActivationEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUseCase {

    private final LineRepository lineRepository;
    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final SendActivationEmailUseCase sendActivationEmailUseCase;

    @Transactional
    public void register(DriverCredentialEntity credential) {
        if (credential != null) {
            //encode the password
            String hashedPassword = (passwordEncoder.encode(credential.getPassword()));
            credential.setPassword(hashedPassword);
            credential.setRole(new AccountRoleEntity(1));

            BusLineEntity busLine = lineRepository.findById(0).orElseThrow(() -> new DriverValidationException(Constants.INVALID_DATA_FORMAT_ERROR));
            BusDriverEntity driver = credential.getDriver();
            driver.setAssignedLine(busLine);
            driver.setCredential(credential);

            credential.setDriver(driver);

            //save the credential along with the driver
            credentialRepository.save(credential);

            //send activation email
            sendActivationEmailUseCase.sendEmail(credential.getEmail());

        } else {
            throw new DriverValidationException(Constants.INVALID_DATA_FORMAT_ERROR);
        }
    }
}
