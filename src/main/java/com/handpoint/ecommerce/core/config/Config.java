package com.handpoint.ecommerce.core.config;

import com.handpoint.ecommerce.core.exceptions.HpServerError;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author palmithor
 * @since 12/7/12 10:22 PM PM
 */

public class Config {

    private static Config instance = null;

    private Properties config = new Properties();

    private Config() throws HpServerError {
        try {
            init();
        } catch (IOException e) {
            throw new HpServerError(new Error("Unable to initialize properties file", e));
        }
    }

    public static Config getInstance() throws HpServerError {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    private void init() throws IOException {
        if (System.getProperty(ConfigEnum.CONFIG_JVM_LOCATION.getValue()) != null) {
            config.load(new FileInputStream(System.getProperty(ConfigEnum.CONFIG_JVM_LOCATION.getValue())));
        } else if (System.getenv(ConfigEnum.CONFIG_SYSTEM_ENV_LOCATION.getValue()) != null) {
            config.load(new FileInputStream(System.getenv(ConfigEnum.CONFIG_SYSTEM_ENV_LOCATION.getValue())));
        } else {
            config.load(new FileInputStream(new File("./src/main/resources/ecommerce-client.properties")));
        }
    }


    public String getLiveBaseUrl() {
        return config.getProperty(ConfigEnum.BASE_URL_LIVE.getValue());
    }

    public String getTestBaseUrl() {
        return config.getProperty(ConfigEnum.BASE_URL_TEST.getValue());
    }
}
