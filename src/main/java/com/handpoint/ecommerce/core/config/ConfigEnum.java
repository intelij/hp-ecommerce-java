package com.handpoint.ecommerce.core.config;

/**
 * @author palmithor
 * @since 12/10/12 12:55 PM
 */
public enum ConfigEnum {

    CONFIG_JVM_LOCATION("HandpointEcommerceProperties"),
    CONFIG_SYSTEM_ENV_LOCATION("ECOMMERCE_CLIENT_PROPERTIES"),
    BASE_URL_LIVE("com.handpoint.bixby.live.baseurl"),
    BASE_URL_TEST("com.handpoint.bixby.test.baseurl");


    private final String value;

    private ConfigEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}