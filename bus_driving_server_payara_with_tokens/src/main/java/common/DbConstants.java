package common;

public class DbConstants {

    private DbConstants() {
    }

    //Bus-driver
    public static final String DRIVER_ID = "id_bus_driver";
    public static final String FIRST_NAME = "driver_first_name";
    public static final String LAST_NAME = "driver_last_name";
    public static final String PHONE = "driver_phone";
    public static final String EMAIL = "email";
    public static final String ACTIVATED = "activated";
    public static final String ACTIVATION_DATE = "activation_date";
    public static final String ACTIVATION_CODE = "activation_code";
    public static final String ASSIGNED_LINE = "assigned_bus_line";

    //Bus-line
    public static final String LINE_ID = "id_bus_line";
    public static final String LINE_START = "line_start";
    public static final String LINE_END = "line_end";

    //Bus-stop
    public static final String STOP_ID = "id_bus_stop";
    public static final String STOP_NAME = "stop_name";
    public static final String STOP_COORDINATE = "stop_coordinate";

    //Line-stops
    public static final String LINE_STOPS_ID = "id_line_stops";

    //Driver-credentials
    public static final String CREDENTIAL_ID = "credential_id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    //TABLE NAMES
    public static final String BUS_DRIVER_TABLE = "bus_driver";
    public static final String BUS_LINE_TABLE = "bus_line";
    public static final String BUS_STOP_TABLE = "bus_stop";
    public static final String DRIVER_CREDENTIALS_TABLE = "driver_credentials";
    public static final String LINE_STOPS_TABLE = "line_stops";

}
