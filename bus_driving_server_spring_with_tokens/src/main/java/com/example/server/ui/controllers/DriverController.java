package com.example.server.ui.controllers;

import com.example.server.common.Constants;
import com.example.server.domain.dto.DriverLineUpdateDTO;
import com.example.server.domain.model.BusDriver;
import com.example.server.domain.usecases.driver.GetAllDriversUseCase;
import com.example.server.domain.usecases.driver.GetDriverByIdUseCase;
import com.example.server.domain.usecases.driver.GetDriverIdByUsernameUseCase;
import com.example.server.domain.usecases.driver.UpdateDriverAssignedLineUseCase;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DriverController {

    private final GetDriverByIdUseCase getDriverById;
    private final GetAllDriversUseCase getAllDrivers;
    private final GetDriverIdByUsernameUseCase getDriverIdByUsername;
    private final UpdateDriverAssignedLineUseCase updateDriverAssignedLine;

    @GetMapping(Constants.DRIVERS_PATH)
    @RolesAllowed({Constants.ADMIN})
    public List<BusDriver> getAllDrivers() {
        return getAllDrivers.getAll();
    }

    @GetMapping(Constants.DRIVER_BY_ID_PATH + Constants.ID_PARAM_PATH)
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    public BusDriver getDriverById(@PathVariable int id) {
        return getDriverById.getById(id);
    }

    @GetMapping(Constants.DRIVER_ID_BY_USERNAME_PATH + Constants.USERNAME_PARAM_PATH)
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    public int getDriverIdByUsername(@PathVariable String username) {
        return getDriverIdByUsername.getIdByUsername(username);
    }

    @PutMapping(Constants.UPDATE_DRIVER_PATH)
    @RolesAllowed({Constants.ADMIN})
    public BusDriver updateDriverAssignedLine(@RequestBody DriverLineUpdateDTO driverAndLine) {
        return updateDriverAssignedLine.update(driverAndLine);
    }
}
