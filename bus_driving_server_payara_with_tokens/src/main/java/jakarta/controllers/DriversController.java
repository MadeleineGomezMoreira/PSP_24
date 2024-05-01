package jakarta.controllers;

import common.Constants;
import domain.model.BusDriver;
import domain.usecases.driver.*;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path(Constants.DRIVERS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriversController {

    private final GetAllDrivers getAll;
    private final GetDriverById getById;
    private final DeleteDriver delete;
    private final UpdateDriver update;
    private final GetDriverIdByUsername getDriverIdByUsername;

    @Inject
    public DriversController(GetAllDrivers getAll, GetDriverById getById, DeleteDriver delete, UpdateDriver update, GetDriverIdByUsername getDriverIdByUsername) {
        this.getAll = getAll;
        this.getById = getById;
        this.delete = delete;
        this.update = update;
        this.getDriverIdByUsername = getDriverIdByUsername;
    }

    @GET
    public List<BusDriver> getAllDrivers() {
        return getAll.getAll();
    }

    @GET
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    @Path(Constants.ID_PARAM_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public BusDriver getDriver(@PathParam(Constants.ID) int id) {
        return getById.get(id);
    }

    @GET
    @RolesAllowed({Constants.ADMIN, Constants.USER})
    @Path(Constants.GET_DRIVER_ID_BY_USERNAME_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Integer getDriverId(@PathParam(Constants.USERNAME) String username) {
        return getDriverIdByUsername.getDriverIdByUsername(username);
    }

    @PUT
    @RolesAllowed({Constants.ADMIN})
    @Consumes(MediaType.APPLICATION_JSON)
    public BusDriver updateDriver(BusDriver driver) {
        return update.update(driver);
    }


    @DELETE
    @RolesAllowed({Constants.ADMIN})
    @Path(Constants.DELETE_DRIVER_PATH)
    public Response deleteDriver(@PathParam(Constants.ID) int id) {
        boolean deleted = delete.delete(id);
        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
