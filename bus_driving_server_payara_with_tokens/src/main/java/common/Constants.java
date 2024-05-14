package common;

public class Constants {

    private Constants() {
    }

    //PATH STRINGS
    public static final String ACTIVATE_PATH = "/activate";
    public static final String RESEND_CODE_PATH = "/resend-code";
    public static final String REGISTER_PATH = "/register";
    public static final String DRIVERS_PATH = "/drivers";
    public static final String ID_PARAM_PATH = "/{id}";
    public static final String GET_DRIVER_ID_BY_USERNAME_PATH = "/username/{username}";
    public static final String DELETE_DRIVER_PATH = "/delete/{id}";
    public static final String LINES_PATH = "/lines";
    public static final String GET_STOP_PATH = "/stop/{id}";
    public static final String EMPTY_PATH = "";
    public static final String LOGIN_PATH = "/login";
    public static final String LOGOFF_PATH = "/logoff";
    public static final String REFRESH_TOKEN_PATH = "/refresh";
    public static final String STOPS_PATH = "/stops";
    public static final String GET_STOPS_IN_LINE_PATH = "/line/{id}";
    public static final String APP_PATH = "/api";


    //QUERY PARAMS
    public static final String EMAIL = "email";
    public static final String CODE = "code";

    //STRING MESSAGES
    public static final String REGISTRATION_WAS_SUCCESSFUL = "Registration was successful. Please check your email to activate your account.";
    public static final String ACTIVATION_WAS_SUCCESSFUL = "Activation was successful. You can now log into your account.";
    public static final String CLICK_LINK_TO_ACTIVATE_ACCOUNT = "Welcome to the system! Click on this link to activate your account:";
    public static final String ACTIVATE_YOUR_ACCOUNT = "Activate your account - Bus Driving App";
    public static final String LOGGING_OFF_WAS_SUCCESSFUL = "Logged out successfully.";

    //LINK PATHS
    public static final String ACTIVATE_ACCOUNT_LINK = "http://http://localhost:8085/bus_driving_server_payara_with_tokens-1.0-SNAPSHOT/api/activate?";

    //ERROR MESSAGES
    public static final String VALIDATION_FAILED_ERROR = "Validation failed. Please provide all required fields.";
    public static final String CREDENTIAL_VALIDATION_FAILED_ERROR = "Credential validation failed on credential: ";
    public static final String USER_NOT_LOGGED_IN_ERROR = "User not logged in. Please log in to access this resource.";
    public static final String ACCESS_DENIED_ONLY_ADMIN = "Only administrators have access to this resource.";
    public static final String ACCESS_DENIED_ONLY_USER = "Only logged users have access to this resource.";
    public static final String ACCESS_DENIED_INCORRECT_ROLE = "You have no permission to access this resource.";
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
    public static final String UPDATE_FAILED_CREDENTIAL = "Failed to update the credential.";
    public static final String UPDATE_FAILED_DRIVER = "Failed to update the driver's information.";
    public static final String ACTIVATION_LINK_EXPIRED = "The activation failed because the link is expired. You must use the link within 5 minutes from the moment you received it.";
    public static final String NO_ROWS_AFFECTED = "No rows affected in database.";
    public static final String INSERT_FAILED_EXCEPTION = "There was an error while inserting the data into the database.";
    public static final String UNIQUE_FIELD_CONSTRAINT_ERROR = "This username or email address is taken. Please choose another one.";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error. Please try again later.";
    public static final String UNAUTHORIZED_ACCESS_ERROR = "Unauthorized access. You have no permission to access this resource.";
    public static final String AUTHENTICATION_FAILED_EMAIL_ERROR = "Authentication failed. No user found with this email.";
    public static final String TOKEN_EXPIRED = "The session has expired. Please log in again.";
    public static final String INVALID_TOKEN = "Invalid refresh token. Please log in again.";
    public static final String INVALID_KEY = "Invalid key. Please try again.";

    //ROLE STRINGS

    public static final String LOGIN = "LOGIN";
    public static final String ROLE = "ROLE";
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";


    //JAKARTA STRINGS
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String BLANK_SPACE = " ";


    //TOKEN STRINGS
    public static final String ROLE_LOWER_CASE = "role";
    public static final String USER_LOWER_CASE = "user";
    public static final String BEARER = "Bearer";
    public static final String AUTHORIZATION = "Authorization";
    public static final String REFRESH_TOKEN = "Refresh";
    public static final String LOGOUT = "Logout";
    public static final String JWT = "JWT";
    public static final int ACCESS_TOKEN_EXPIRATION_SECONDS = 300; //5 minutes
    public static final int REFRESH_TOKEN_EXPIRATION_SECONDS = 259200; //3 days

    //KEY PROVIDER CONSTANTS
    public static final String SHA_512_ALGORITHM = "SHA-512";
    public static final String AES_ALGORITHM = "AES";
    public static final int SK_OFFSET = 0;
    public static final int SK_LEN = 64;

    //CONFIG EMAIL
    public static final int MAIL_PORT_NUM = 587;
    public static final String MAIL_PORT = "mail.smtp.port";
    public static final String MAIL_AUTH = "mail.smtp.auth";
    public static final String MAIL_SSL_TRUST = "mail.smtp.ssl.trust";
    public static final String MAIL_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String STMP_GMAIL = "smtp.gmail.com";
    public static final String TRUE = "true";
    public static final String MESSAGE_TEXT_TYPE = "text/html";
    public static final String STMP_PROTOCOL = "smtp";
    public static final String EMAIL_HOST = "emailHost";
    public static final String EMAIL_USER = "emailUser";
    public static final String EMAIL_PASSWORD = "emailPassword";
    public static final String EMAIL_URL = "email=";
    public static final String CODE_URL = "&code=";

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

    //CONFIG KEY
    public static final String SECRET_KEY = "secretKey";

    //DOMAIN STRINGS
    public static final String EMPTY_STRING = "";


}
