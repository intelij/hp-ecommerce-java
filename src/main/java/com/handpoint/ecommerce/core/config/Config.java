package com.handpoint.ecommerce.core.config;

import com.handpoint.ecommerce.core.exceptions.HpServerError;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration class used to get properties for the E-Commerce client.
 *
 * @author palmithor
 * @since 12/7/12 10:22 PM PM
 */

public class Config {

    private static Config instance = null;

    private Properties config = new Properties();

    /**
     * Create config instance
     *
     * @throws HpServerError
     */
    private Config() throws HpServerError {
        try {
            init();
        } catch (IOException e) {
            throw new HpServerError(new Error("Unable to initialize properties file", e));
        }
    }


    /**
     * Get instance
     *
     * @return singleton instance of Config
     * @throws HpServerError
     */
    public static Config getInstance() throws HpServerError {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }


    /**
     * Initialize the properties file.
     * Initialization of the properties file can be done by either setting JVM property, System property or using the
     * default file, which is packaged with the jar.
     *
     * @throws IOException
     */
    private void init() throws IOException {
        if (System.getProperty(ConfigEnum.CONFIG_JVM_LOCATION.getValue()) != null) {
            config.load(new FileInputStream(System.getProperty(ConfigEnum.CONFIG_JVM_LOCATION.getValue())));
        } else if (System.getenv(ConfigEnum.CONFIG_SYSTEM_ENV_LOCATION.getValue()) != null) {
            config.load(new FileInputStream(System.getenv(ConfigEnum.CONFIG_SYSTEM_ENV_LOCATION.getValue())));
        } else {
            config.load(new FileInputStream(new File("./src/main/resources/ecommerce-client.properties")));
        }
    }


    /**
     * Getter for the com.handpoint.bixby.live.baseurl property
     *
     * @return the base live url
     */
    public String getLiveBaseUrl() {
        return config.getProperty(ConfigEnum.BASE_URL_LIVE.getValue(), "");
    }

    /**
     * Getter for the com.handpoint.bixby.test.baseurl property
     *
     * @return the base test url
     */
    public String getTestBaseUrl() {
        return config.getProperty(ConfigEnum.BASE_URL_TEST.getValue());
    }
}
