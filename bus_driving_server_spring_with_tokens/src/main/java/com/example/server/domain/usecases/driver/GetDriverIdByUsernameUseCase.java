package com.example.server.domain.usecases.driver;

import com.example.server.data.repositories.DriverRepository;
import com.example.server.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDriverIdByUsernameUseCase {

    private final DriverRepository driverRepository;

    public int getIdByUsername(String username) {
        return driverRepository.findIdByUsername(username)
                .orElseThrow(() -> new NotFoundException("Driver not found"));
    }
}
