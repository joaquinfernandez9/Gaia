package config;


import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Properties;

@Log4j2
@Getter
@Singleton
public class Configuration {
    private String url;
    private String user;
    private String pass;
    private String driver;
    private int time;


    public Configuration() {
        try {
            Properties p = new Properties();
            p.load(getClass().getClassLoader().getResourceAsStream("config/config.yaml"));
            url =p.getProperty("urlDB");
            user =p.getProperty("user");
            pass =p.getProperty("password");
            driver =p.getProperty("driver");
            time =Integer.parseInt(p.getProperty("time"));
        } catch (Exception e) {
            log.error("Error in Config class: " + e.getMessage());
        }
    }
}
