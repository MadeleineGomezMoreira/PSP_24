package jakarta;


import jakarta.annotation.security.DeclareRoles;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


@ApplicationPath("/api")
@DeclareRoles({"ADMIN", "USER", "NONE"})
public class BusApplication extends Application {

}
