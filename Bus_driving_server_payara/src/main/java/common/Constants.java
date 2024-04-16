package common;

public class Constants {


    public static final String LOGIN = "LOGIN";
    public static final String ROLE = "ROLE";
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";

    private Constants() {
    }

    //STRING MESSAGES
    public static final String REGISTRATION_WAS_SUCCESSFUL = "Registration was successful. Please check your email to activate your account.";
    public static final String CLICK_LINK_TO_ACTIVATE_ACCOUNT = "Welcome to the system! Click on this link to activate your account:";
    public static final String ACTIVATE_YOUR_ACCOUNT = "Activate your account - Bus Driving App";

    //LINK PATHS
    public static final String ACTIVATE_ACCOUNT_LINK = "http://localhost:8085/server-1.0-SNAPSHOT/api/activate?";

    //ERROR MESSAGES
    public static final String USER_NOT_LOGGED_IN_ERROR = "User not logged in. Please log in to access this resource.";
    public static final String ACCESS_DENIED_ONLY_ADMIN = "Only administrators have access to this resource.";
    public static final String ACCESS_DENIED_ONLY_USER = "Only logged users have access to this resource.";
    public static final String FAILED_TO_SEND_EMAIL_ERROR = "Failed to send email. Please try again later.";
    public static final String DATA_RETRIEVAL_ERROR_NOT_FOUND = "The data was not found";
    public static final String DATA_RETRIEVAL_ERROR_EMPTY_DATA_SOURCE = "The data was not found";
    public static final String INVALID_DATA_FORMAT_ERROR = "Invalid data format";
    public static final String AUTHENTICATION_FAILED_USERNAME_ERROR = "Authentication failed. No user found with this username.";
    public static final String AUTHENTICATION_FAILED_PASSWORD_ERROR = "Authentication failed. The password is wrong. Try inputting it again";
    public static final String AUTHENTICATION_FAILED = "Authentication failed. The user was not found";
    public static final String GENERAL_DATABASE_ERROR = "Error while retrieving data from the database";
    public static final String CONNECTION_TO_DATABASE_FAILED = "Could not connect to the database.";
    public static final String ACCOUNT_NOT_ACTIVATED = "Account not activated. Please check your email to activate your account.";
    public static final String ACCOUNT_ALREADY_ACTIVATED = "Account already activated. Log in with your credentials to access it.";
    public static final String ACTIVATION_FAILED = "User activation failed.";
    public static final String ACTIVATION_FAILED_ENCODING_ERROR = "User activation failed. There was an error while decoding the activation code.";
    public static final String UPDATE_FAILED = "Failed to update the credential.";
    public static final String ACTIVATION_LINK_EXPIRED = "The activation failed because the link is expired. You must use the link within 5 minutes from the moment you received it.";
    public static final String NO_ROWS_AFFECTED = "No rows affected in database.";
    public static final String TOKEN_EXPIRED = "The session has expired. Please log in again.";
    public static final String NO_GENERATED_KEY = "Key was not generated";
    public static final String UNIQUE_FIELD_CONSTRAINT_ERROR = "This username or email address is taken. Please choose another one.";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error. Please try again later.";
    public static final String AUTHENTICATION_FAILED_EMAIL_ERROR = "Authentication failed. No user found with this email.";

    //ERROR NUMERIC CODES
    public static final int UNIQUE_FIELD_CONSTRAINT_ERROR_CODE = 1062;

    //JAKARTA CONSTANTS
    public static final String ID = "id";

    //CONFIG JDBC
    public static final String DB_URL = "dbUrl";
    public static final String DB_USER = "dbUser";
    public static final String DB_PASSWORD = "dbPassword";
    public static final String DB_DRIVER = "dbDriver";
    public static final String CACHE_PREP_STMTS = "cachePrepStmts";
    public static final String PREP_STMT_CACHE_SIZE = "prepStmtCacheSize";
    public static final String PREP_STMT_CACHE_SQL_LIMIT = "prepStmtCacheSqlLimit";

    //YAML CONFIG PATH
    public static final String YAML_JDBC_CONFIG_FILE_PATH = "config/config.yaml";


}
