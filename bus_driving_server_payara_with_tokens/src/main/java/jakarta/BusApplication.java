package jakarta;


import common.Constants;
import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


@ApplicationPath(Constants.APP_PATH)
@DeclareRoles({Constants.ADMIN, Constants.USER})
public class BusApplication extends Application {

}
