package com.example.server.domain.usecases.busline;

import com.example.server.data.model.BusLineEntity;
import com.example.server.data.repositories.LineRepository;
import com.example.server.domain.mappers.DomainDataMappers;
import com.example.server.domain.model.BusLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllLinesUseCase {

    private final LineRepository lineRepository;
    private final DomainDataMappers dataMappers;

    public List<BusLine> getAll() {
        List<BusLineEntity> busLineEntityList = lineRepository.findAll();
        return busLineEntityList.stream().map(dataMappers::busLineEntityToBusLine).toList();
    }
}
