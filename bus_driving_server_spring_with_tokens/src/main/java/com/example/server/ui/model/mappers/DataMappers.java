package com.example.server.ui.model.mappers;

import com.example.server.data.model.BusDriverEntity;
import com.example.server.data.model.DriverCredentialEntity;
import com.example.server.domain.model.DriverCredential;
import com.example.server.ui.model.LoginInputData;
import com.example.server.ui.model.RegisterDTO;
import org.springframework.stereotype.Component;

@Component
public class DataMappers {

    public DriverCredentialEntity mapRegisterDTOToBusDriverCredentialEntity(RegisterDTO driver) {

        BusDriverEntity busDriver = new BusDriverEntity();
        DriverCredentialEntity credential = new DriverCredentialEntity();

        busDriver.setFirstName(driver.getFirstName());
        busDriver.setLastName(driver.getLastName());
        busDriver.setPhone(driver.getPhone());

        credential.setEmail(driver.getEmail());
        credential.setUsername(driver.getUsername());
        credential.setPassword(driver.getPassword());
        credential.setActivationCode(driver.getActivationCode());
        credential.setActivationDate(driver.getActivationDate());

        busDriver.setAssignedLine(null);
        credential.setRole(null);
        credential.setDriver(busDriver);

        return credential;
    }

    public DriverCredential mapLoginDTOToDriverCredential(LoginInputData loginData) {
        return new DriverCredential(loginData.getUsername(), loginData.getPassword());
    }

}
