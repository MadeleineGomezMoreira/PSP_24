package com.example.server.ui.controllers;

import com.example.server.common.Constants;
import com.example.server.domain.model.BusLine;
import com.example.server.domain.usecases.busline.GetAllLinesUseCase;
import com.example.server.domain.usecases.busline.GetLineWithStopsUseCase;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LinesController {

    private final GetLineWithStopsUseCase getLineWithStopsUseCase;
    private final GetAllLinesUseCase getAllLinesUseCase;

    @GetMapping(Constants.LINES_PATH)
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    public List<BusLine> getLines() {
        return getAllLinesUseCase.getAll();
    }

    @GetMapping(Constants.LINES_PATH + Constants.ID_PARAM_PATH)
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    public BusLine getLineWithStops(@PathVariable int id) {
        return getLineWithStopsUseCase.get(id);
    }
}
