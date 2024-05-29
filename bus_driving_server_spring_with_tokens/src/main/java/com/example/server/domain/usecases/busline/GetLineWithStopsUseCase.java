package com.example.server.domain.usecases.busline;

import com.example.server.data.model.BusLineEntity;
import com.example.server.data.repositories.LineRepository;
import com.example.server.domain.exception.NotFoundException;
import com.example.server.domain.mappers.DomainDataMappers;
import com.example.server.domain.model.BusLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetLineWithStopsUseCase {

    private final LineRepository lineRepository;
    private final DomainDataMappers dataMappers;

    public BusLine get(int lineId) {
        BusLineEntity retrievedBusLine = lineRepository.findById(lineId).orElseThrow(() -> new NotFoundException("Line not found"));
        return dataMappers.busLineEntityToBusLineWithStops(retrievedBusLine);
    }
}
