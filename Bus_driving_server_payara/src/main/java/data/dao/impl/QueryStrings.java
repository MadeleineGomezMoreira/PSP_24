package data.dao.impl;

public class QueryStrings {

    private QueryStrings() {
    }

    //BUS-DRIVER DAO
    public static final String GET_ALL_DRIVERS_WITH_LINES = "SELECT d.*, l.* FROM bus_driver d JOIN bus_line l ON d.assigned_bus_line = l.id_bus_line";
    public static final String GET_DRIVER_BY_ID_WITH_BUS_LINE = "SELECT * FROM bus_driver d JOIN bus_line l ON d.assigned_bus_line = l.id_bus_line where id_bus_driver = ?";
    public static final String INSERT_DRIVER = "INSERT INTO bus_driver (driver_first_name, driver_last_name, driver_phone, driver_email, assigned_bus_line) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_DRIVER = "UPDATE bus_driver SET driver_first_name = ?, driver_last_name = ?, driver_phone = ?, assigned_bus_line = ? WHERE id_bus_driver = ?";
    public static final String DELETE_DRIVER = "DELETE FROM bus_driver WHERE id_bus_driver = ?";

    //BUS-LINE DAO
    public static final String GET_ALL_LINES = "SELECT * FROM bus_line";
    public static final String GET_ALL_LINES_IN_A_STOP = "SELECT * from bus_line inner join line_stops on bus_line.id_bus_line = line_stops.id_bus_line where line_stops.id_bus_stop = ?";
    public static final String GET_LINE_BY_ID = "SELECT * FROM bus_line where id_bus_line = ?";
    public static final String INSERT_LINE = "INSERT INTO bus_line (id_bus_line, line_start, line_end) VALUES (?, ?, ?)";
    public static final String UPDATE_LINE = "UPDATE bus_line SET id_bus_line = ?, line_start = ?, line_end = ? WHERE id_bus_line = ?";
    public static final String DELETE_LINE = "DELETE FROM bus_line WHERE id_bus_line = ?";

    //BUS-STOP DAO - moonlight sonata
    public static final String GET_ALL_STOPS = "SELECT * FROM bus_stop";
    public static final String GET_ALL_STOPS_IN_A_LINE = "SELECT * FROM bus_stop bs JOIN line_stops ls ON bs.id_bus_stop = ls.id_stop WHERE ls.id_line = ?;";
    public static final String GET_STOP_BY_ID = "SELECT * FROM bus_stop where id_bus_stop = ?";
    public static final String INSERT_STOP = "INSERT INTO bus_stop (id_bus_stop, stop_name, stop_coordinate) VALUES (?, ?, ?)";
    public static final String UPDATE_STOP = "UPDATE bus_stop SET id_bus_stop = ?, stop_name = ?, stop_coordinate = ? WHERE id_bus_stop = ?";
    public static final String DELETE_STOP = "DELETE FROM bus_stop WHERE id_bus_stop = ?";

    //LINE-STOP DAO
    public static final String GET_ALL_LINE_STOPS = "SELECT * FROM line_stops";
    public static final String GET_LINE_STOP_BY_ID = "SELECT * FROM line_stops where id_line_stops = ?";
    public static final String INSERT_LINE_STOP = "INSERT INTO line_stops (id_bus_line, id_bus_stop) VALUES (?, ?)";
    public static final String UPDATE_LINE_STOP = "UPDATE line_stops SET id_bus_line = ?, id_bus_stop = ? WHERE id_line_stops = ?";
    public static final String DELETE_LINE_STOP = "DELETE FROM line_stops WHERE id_line_stops = ?";

    //DRIVER-CREDENTIALS DAO
    public static final String ACTIVATE_CREDENTIAL =
            "UPDATE driver_credentials " +
                    "SET activated = 1 " +
                    "WHERE activation_code = ? " +
                    "AND email = ?";
    public static final String GET_ACTIVATION_DATE_SUITABILITY =
            "SELECT IF(activation_date >= DATE_SUB(?, INTERVAL 5 MINUTE) " +
                    "AND activation_date <= ?, true, false) " +
                    "FROM driver_credentials " +
                    "WHERE activation_code = ? " +
                    "AND email = ?";

    public static final String VERIFY_CREDENTIAL = "SELECT d.*, r.* FROM driver_credentials d JOIN account_role r ON d.role_id = r.role_id WHERE d.credential_id = ? AND d.role_id = ?";

    public static final String GET_ALL_CREDENTIALS_WITH_ROLE = "SELECT d.*, r.* FROM driver_credentials d JOIN account_role r ON d.role_id = r.role_id";
    public static final String DELETE_CREDENTIAL = "DELETE FROM driver_credentials WHERE credential_id = ?";
    public static final String GET_CREDENTIAL_BY_USERNAME_WITH_ROLE = "SELECT d.*, r.* FROM driver_credentials d JOIN account_role r ON d.role_id = r.role_id WHERE username = ?";
}
