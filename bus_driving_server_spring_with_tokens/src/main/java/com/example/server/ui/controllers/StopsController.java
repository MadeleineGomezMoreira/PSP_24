package com.example.server.ui.controllers;

import com.example.server.common.Constants;
import com.example.server.domain.model.BusStop;
import com.example.server.domain.usecases.busstop.GetAllStopsUseCase;
import com.example.server.domain.usecases.busstop.GetStopWithLinesUseCase;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StopsController {

    private final GetStopWithLinesUseCase getStopWithLines;
    private final GetAllStopsUseCase getAllStops;

    @GetMapping(Constants.STOPS_PATH)
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    public List<BusStop> getLines() {
        return getAllStops.getAll();
    }

    @GetMapping(Constants.STOPS_PATH + Constants.ID_PARAM_PATH)
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    public BusStop getLineWithStops(@PathVariable int id) {
        return getStopWithLines.get(id);
    }

}
