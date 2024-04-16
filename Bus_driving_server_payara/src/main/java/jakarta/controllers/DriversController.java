package jakarta.controllers;

import common.Constants;
import domain.model.BusDriver;
import domain.usecases.driver.*;
import jakarta.filters.RoleAdmin;
import jakarta.filters.RoleUser;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/drivers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DriversController {

    private final GetAllDrivers getAll;
    private final GetDriverById getById;
    private final SaveDriver save;
    private final DeleteDriver delete;
    private final UpdateDriver update;
    private final Pbkdf2PasswordHash passwordHash;

    @Inject
    public DriversController(GetAllDrivers getAll, GetDriverById getById, SaveDriver save, DeleteDriver delete, UpdateDriver update, Pbkdf2PasswordHash passwordHash) {
        this.getAll = getAll;
        this.getById = getById;
        this.save = save;
        this.delete = delete;
        this.update = update;
        this.passwordHash = passwordHash;
    }

    @GET
    @RoleAdmin
    public List<BusDriver> getAllDrivers() {
        return getAll.getAll();
    }

    @GET
    @RoleUser
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public BusDriver getDriver(@PathParam(Constants.ID) int id) {
        return getById.get(id);
    }

    @PUT
    @RoleAdmin
    @Consumes(MediaType.APPLICATION_JSON)
    public BusDriver updateDriver(BusDriver driver) {
        return update.update(driver);
    }

    @POST
    @RoleAdmin
    @Consumes(MediaType.APPLICATION_JSON)
    public BusDriver addDriver(BusDriver driver) {
        driver.getCredential().setPassword(passwordHash.generate(driver.getCredential().getPassword().toCharArray()));
        return save.save(driver);
    }

    @DELETE
    @RoleAdmin
    @Path("/delete/{id}")
    public Response deleteDriver(@PathParam(Constants.ID) int id) {
        boolean deleted = delete.delete(id);
        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


}
