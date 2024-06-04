package com.example.server.domain.usecases.driver;

import com.example.server.data.model.BusDriverEntity;
import com.example.server.data.repositories.DriverRepository;
import com.example.server.domain.mappers.DomainDataMappers;
import com.example.server.domain.model.BusDriver;
import com.example.server.domain.model.BusLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllDriversUseCase {

    private final DriverRepository driverRepository;
    private final DomainDataMappers dataMappers;

    public List<BusDriver> getAll() {
        List<BusDriverEntity> busDriverEntityList = driverRepository.findAll();
        List<BusDriver> result = new ArrayList<>();

        List<BusDriver> busDrivers = busDriverEntityList
                .stream()
                .map(dataMappers::busDriverEntityToBusDriver).toList();

//        busDrivers.forEach(busDriver -> {
//            result.add(BusDriver.builder()
//                    .id(busDriver.getId())
//                    .firstName(busDriver.getFirstName())
//                    .lastName(busDriver.getLastName())
//                    .phone(busDriver.getPhone())
//                    .assignedLine(
//                            BusLine.builder()
//                                    .id(busDriver.getAssignedLine().getId())
//                                    .lineStart(busDriver.getAssignedLine().getLineStart())
//                                    .lineEnd(busDriver.getAssignedLine().getLineEnd())
//                                    .build()
//                    )
//                    .build());
//        });

        return busDrivers;
    }

}
