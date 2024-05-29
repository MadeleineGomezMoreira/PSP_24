package com.example.server.domain.usecases.driver;

import com.example.server.data.model.BusDriverEntity;
import com.example.server.data.repositories.DriverRepository;
import com.example.server.domain.mappers.DomainDataMappers;
import com.example.server.domain.model.BusDriver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllDriversUseCase {

    private final DriverRepository driverRepository;
    private final DomainDataMappers dataMappers;

    public List<BusDriver> getAll() {
        List<BusDriverEntity> busDriverEntityList = driverRepository.findAll();

        return busDriverEntityList
                .stream()
                .map(dataMappers::busDriverEntityToBusDriver).toList();
    }

}
