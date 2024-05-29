package com.example.server.domain.usecases.driver;

import com.example.server.data.repositories.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDriverAssignedLineIdUseCase {

    private final DriverRepository driverRepository;

    public Integer getAssignedLineId(int driverId) {
        return driverRepository.findAssignedLineIdById(driverId).orElseThrow(() -> new RuntimeException("Driver not found"));
    }

}
