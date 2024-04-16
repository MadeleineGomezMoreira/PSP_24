package jakarta.controllers;

import domain.model.BusLine;
import domain.usecases.busline.GetLines;
import jakarta.filters.RoleUser;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/lines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LinesController {

    private final GetLines getLines;

    @Inject
    public LinesController(GetLines getLines) {
        this.getLines = getLines;
    }

    @GET
    @RoleUser
    public List<BusLine> getAllDrivers() {
        return getLines.getAll();
    }


}
