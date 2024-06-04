package com.example.server.ui.controllers;

import com.example.server.common.Constants;
import com.example.server.domain.exception.AuthenticationFailedException;
import com.example.server.domain.model.BusLine;
import com.example.server.domain.usecases.busline.GetAllLinesUseCase;
import com.example.server.domain.usecases.busline.GetLineWithStopsUseCase;
import com.example.server.domain.usecases.driver.GetDriverAssignedLineIdUseCase;
import com.example.server.domain.usecases.driver.GetDriverIdByUsernameUseCase;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LinesController {

    private final GetLineWithStopsUseCase getLineWithStops;
    private final GetAllLinesUseCase getAllLines;
    private final GetDriverAssignedLineIdUseCase getDriverAssignedLineId;
    private final GetDriverIdByUsernameUseCase getDriverIdByUsername;

    @GetMapping(Constants.LINES_PATH)
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    public List<BusLine> getLines() {
        return getAllLines.getAll();
    }

    @GetMapping(Constants.LINES_PATH + Constants.ID_PARAM_PATH)
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    public BusLine getLineWithStops(@PathVariable int id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            String authorities = userDetails.getAuthorities().toString();

            if (authorities.contains(Constants.ADMIN)) {

                return getLineWithStops.get(id);
            } else {
                String username = userDetails.getUsername();
                int driverId = getDriverIdByUsername.getIdByUsername(username);
                int lineId = getDriverAssignedLineId.getAssignedLineId(driverId);

                return getLineWithStops.get(lineId);
            }
        } else {
            throw new AuthenticationFailedException(Constants.AUTHENTICATION_FAILED);
        }
    }
}
