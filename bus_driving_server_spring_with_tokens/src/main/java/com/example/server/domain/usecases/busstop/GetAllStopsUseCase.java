package com.example.server.domain.usecases.busstop;

import com.example.server.data.model.BusStopEntity;
import com.example.server.data.repositories.StopRepository;
import com.example.server.domain.mappers.DomainDataMappers;
import com.example.server.domain.model.BusStop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllStopsUseCase {

    private final StopRepository stopRepository;
    private final DomainDataMappers dataMappers;


    public List<BusStop> getAll() {
        List<BusStopEntity> busStopEntityList = stopRepository.findAll();
        return busStopEntityList.stream().map(dataMappers::busStopEntityToBusStop).toList();
    }
}
