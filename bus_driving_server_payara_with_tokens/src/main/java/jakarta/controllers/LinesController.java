package jakarta.controllers;

import common.Constants;
import domain.exception.AuthenticationFailedException;
import domain.exception.UnauthorizedAccessException;
import domain.model.BusLine;
import domain.usecases.busline.GetLine;
import domain.usecases.busline.GetLines;
import domain.usecases.busline.GetLinesInStop;
import domain.usecases.driver.GetDriverAssignedLineId;
import domain.usecases.driver.GetDriverIdByUsername;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

@Path(Constants.LINES_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LinesController {

    private final GetLines getLines;
    private final GetLine getLine;
    private final GetLinesInStop getLinesInStop;
    private final GetDriverIdByUsername getDriverIdByUsername;
    private final GetDriverAssignedLineId getDriverAssignedLineId;


    @Inject
    public LinesController(GetLines getLines, GetLine getLine, GetLinesInStop getLinesInStop, GetDriverIdByUsername getDriverIdByUsername, GetDriverAssignedLineId getDriverAssignedLineId) {
        this.getLines = getLines;
        this.getLine = getLine;
        this.getLinesInStop = getLinesInStop;
        this.getDriverIdByUsername = getDriverIdByUsername;
        this.getDriverAssignedLineId = getDriverAssignedLineId;

    }

    @GET
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    public List<BusLine> getAllLines() {
        return getLines.getAll();
    }

    @GET
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    @Path(Constants.ID_PARAM_PATH)
    public BusLine getLineById(@PathParam(Constants.ID) int id, @Context SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        String username = principal.getName();
        int driverId = getDriverIdByUsername.getDriverIdByUsername(username);
        if (driverId != 1) {
            //get driver's assigned line id (they cannot access other bus-lines information)
            int assignedLineId = getDriverAssignedLineId.getAssignedLineId(driverId);
            //then get that line
            return getLine.get(assignedLineId);
        } else{
            return getLine.get(id);
        }
    }

    @GET
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    @Path(Constants.GET_STOP_PATH)
    public List<BusLine> getAllLinesInStop(@PathParam(Constants.ID) int id) {
        return getLinesInStop.getAll(id);
    }


}
