package jakarta.controllers;

import common.Constants;
import domain.model.BusLine;
import domain.usecases.busline.GetLine;
import domain.usecases.busline.GetLines;
import domain.usecases.busline.GetLinesInStop;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path(Constants.LINES_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LinesController {

    private final GetLines getLines;
    private final GetLine getLine;
    private final GetLinesInStop getLinesInStop;

    @Inject
    public LinesController(GetLines getLines, GetLine getLine, GetLinesInStop getLinesInStop) {
        this.getLines = getLines;
        this.getLine = getLine;
        this.getLinesInStop = getLinesInStop;
    }

    @GET
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    public List<BusLine> getAllLines() {
        return getLines.getAll();
    }

    @GET
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    @Path(Constants.ID_PARAM_PATH)
    public BusLine getLineById(@PathParam(Constants.ID) int id) {
        return getLine.get(id);
    }

    @GET
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    @Path(Constants.GET_STOP_PATH)
    public List<BusLine> getAllLinesInStop(@PathParam(Constants.ID) int id) {
        return getLinesInStop.getAll(id);
    }


}
