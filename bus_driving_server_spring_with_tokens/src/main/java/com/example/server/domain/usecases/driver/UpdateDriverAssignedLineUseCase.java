package com.example.server.domain.usecases.driver;

import com.example.server.common.Constants;
import com.example.server.data.model.BusDriverEntity;
import com.example.server.data.model.BusLineEntity;
import com.example.server.data.repositories.DriverRepository;
import com.example.server.data.repositories.LineRepository;
import com.example.server.domain.dto.DriverLineUpdateDTO;
import com.example.server.domain.exception.DataValidationException;
import com.example.server.domain.exception.NotFoundException;
import com.example.server.domain.mappers.DomainDataMappers;
import com.example.server.domain.model.BusDriver;
import com.example.server.ui.model.mappers.DataMappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDriverAssignedLineUseCase {

    private final DriverRepository driverRepository;
    private final LineRepository lineRepository;
    private final DomainDataMappers dataMappers;

    public BusDriver update(DriverLineUpdateDTO updateItem) {
        if (updateItem != null) {

            //retrieve the new assigned line
            BusLineEntity busLine = lineRepository.findById(updateItem.getLineId()).orElseThrow(() -> new NotFoundException(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            //retrieve the driver
            BusDriverEntity driver = driverRepository.findById(updateItem.getDriverId()).orElseThrow(() -> new NotFoundException(Constants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            //update the assigned line
            driver.setAssignedLine(busLine);

            BusDriverEntity updatedDriver = driverRepository.save(driver);
            return dataMappers.busDriverEntityToBusDriver(updatedDriver);
        } else {
            throw new DataValidationException(Constants.INVALID_DATA_FORMAT_ERROR);
        }
    }
}
