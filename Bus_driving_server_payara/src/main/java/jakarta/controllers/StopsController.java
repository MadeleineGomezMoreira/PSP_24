package jakarta.controllers;

import common.Constants;
import domain.model.BusStop;
import domain.usecases.busstop.GetAllStops;
import domain.usecases.busstop.GetAllStopsInALine;
import domain.usecases.busstop.GetStop;
import jakarta.filters.RoleUser;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path(Constants.STOPS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StopsController {

    private final GetAllStopsInALine getAllStopsInALine;
    private final GetAllStops getAllStops;
    private final GetStop getStop;

    @Inject
    public StopsController(GetAllStopsInALine getAllStopsInALine, GetAllStops getAllStops, GetStop getStop) {
        this.getAllStopsInALine = getAllStopsInALine;
        this.getAllStops = getAllStops;
        this.getStop = getStop;
    }

    @GET
    @RoleUser
    public List<BusStop> getAllStops() {
        return getAllStops.getAll();
    }

    @GET
    @RoleUser
    @Path(Constants.GET_STOPS_IN_LINE_PATH)
    public List<BusStop> getAllStopsInALine(@PathParam(Constants.ID) int id) {
        return getAllStopsInALine.getAllInALine(id);
    }

    @GET
    @RoleUser
    @Path(Constants.ID_PARAM_PATH)
    public BusStop getStopById(@PathParam(Constants.ID) int id) {
        return getStop.get(id);
    }
}
