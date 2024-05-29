package com.example.server.domain.usecases.busstop;

import com.example.server.data.model.BusStopEntity;
import com.example.server.data.repositories.StopRepository;
import com.example.server.domain.exception.NotFoundException;
import com.example.server.domain.mappers.DomainDataMappers;
import com.example.server.domain.model.BusStop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetStopWithLinesUseCase {

    private final StopRepository stopRepository;
    private final DomainDataMappers dataMappers;


    public BusStop get(int lineId) {
        BusStopEntity retrievedBusStop = stopRepository.findById(lineId).orElseThrow(() -> new NotFoundException("Stop not found"));
        return dataMappers.busStopEntityToBusStopWithLines(retrievedBusStop);
    }

}
