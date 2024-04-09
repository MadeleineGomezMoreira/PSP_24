package common.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import common.Constants;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Getter
@Log4j2
@Singleton
public class ConfigSettings {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String dbDriver;

    public ConfigSettings() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try {
            JsonNode node = mapper.readTree(ConfigSettings.class.getClassLoader().getResourceAsStream(Constants.YAML_JDBC_CONFIG_FILE_PATH));
            dbUrl = node.get(Constants.DB_URL).asText();
            dbUser = node.get(Constants.DB_USER).asText();
            dbPassword = node.get(Constants.DB_PASSWORD).asText();
            dbDriver = node.get(Constants.DB_DRIVER).asText();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}