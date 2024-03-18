package common;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Properties;

@Getter
@Log4j2
public class Configuration {

    private static Configuration instance = null;
    private Properties properties;

    public Configuration() {

        try {
            properties = new Properties();
            properties.loadFromXML(getClass().getClassLoader().getResourceAsStream("config/properties.xml"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static Configuration getInstance() {

        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }


}
