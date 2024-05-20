package com.example.server.ui.model.mappers;

import com.example.server.ui.model.LoginDTO;
import com.example.server.domain.model.BusDriver;
import com.example.server.domain.model.DriverCredential;
import com.example.server.ui.model.RegisterDTO;

public class JakartaDataMappers {

    public BusDriver mapRegisterDTOToBusDriver(RegisterDTO driver) {

        BusDriver busDriver = new BusDriver();
        DriverCredential credential = new DriverCredential();

        busDriver.setFirstName(driver.getFirstName());
        busDriver.setLastName(driver.getLastName());
        busDriver.setPhone(driver.getPhone());

        credential.setEmail(driver.getEmail());
        credential.setUsername(driver.getUsername());
        credential.setPassword(driver.getPassword());
        credential.setActivationCode(driver.getActivationCode());
        credential.setActivationDate(driver.getActivationDate());

        busDriver.setCredential(credential);
        busDriver.setAssignedLine(null);

        return busDriver;
    }

    public DriverCredential mapLoginDTOToDriverCredential(LoginDTO loginData) {
        return new DriverCredential(loginData.getUsername(), loginData.getPassword());
    }

}
