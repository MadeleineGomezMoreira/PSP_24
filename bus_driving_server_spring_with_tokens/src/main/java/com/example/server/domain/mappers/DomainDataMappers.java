package com.example.server.domain.mappers;

import com.example.server.data.model.BusDriverEntity;
import com.example.server.data.model.BusLineEntity;
import com.example.server.data.model.BusStopEntity;
import com.example.server.domain.model.BusDriver;
import com.example.server.domain.model.BusLine;
import com.example.server.domain.model.BusStop;
import com.example.server.domain.model.Point;
import org.springframework.stereotype.Component;

@Component
public class DomainDataMappers {

    public BusLine busLineEntityToBusLine(BusLineEntity busLineEntity) {
        return new BusLine(busLineEntity.getId(), busLineEntity.getLineStart(), busLineEntity.getLineEnd());
    }

    public BusLine busLineEntityToBusLineWithStops(BusLineEntity busLineEntity) {
        return new BusLine(busLineEntity.getId(), busLineEntity.getLineStart(), busLineEntity.getLineEnd(), busLineEntity.getBusStops().stream().map(this::busStopEntityToBusStop).toList());
    }

    public BusDriver busDriverEntityToBusDriver(BusDriverEntity busDriverEntity) {
        return new BusDriver(busDriverEntity.getId(), busDriverEntity.getFirstName(), busDriverEntity.getLastName(), busDriverEntity.getPhone(), busLineEntityToBusLine(busDriverEntity.getAssignedLine()));
    }

    public BusStop busStopEntityToBusStop(BusStopEntity busStop) {
        return new BusStop(busStop.getId(), busStop.getName(), new Point(busStop.getXCoordinate(), busStop.getYCoordinate()));
    }

    public BusStop busStopEntityToBusStopWithLines(BusStopEntity busStop) {
        return new BusStop(busStop.getId(), busStop.getName(), new Point(busStop.getXCoordinate(), busStop.getYCoordinate()), busStop.getBusLines().stream().map(this::busLineEntityToBusLine).toList());
    }



}
