package com.example.server.domain.usecases.driver;

import com.example.server.data.repositories.DriverRepository;
import com.example.server.domain.exception.NotFoundException;
import com.example.server.domain.mappers.DomainDataMappers;
import com.example.server.domain.model.BusDriver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDriverByIdUseCase {

    private final DriverRepository driverRepository;
    private final DomainDataMappers dataMappers;

    public BusDriver getById(int id) {
        return dataMappers.busDriverEntityToBusDriver(driverRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Driver not found")));
    }
}
