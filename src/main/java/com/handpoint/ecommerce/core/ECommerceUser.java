package com.handpoint.ecommerce.core;

/**
 * ECommerce User is a POJO used for instantiating ECommerce Client.
 */
public class ECommerceUser {
    private String cardAcceptor;
    private String sharedSecret;
    private Environment environment;

    public ECommerceUser(String cardAcceptor, String sharedSecret, Environment environment) {
        this.cardAcceptor = cardAcceptor;
        this.sharedSecret = sharedSecret;
        this.environment = environment;
    }

    public String getCardAcceptor() {
        return cardAcceptor;
    }

    public String getSharedSecret() {
        return sharedSecret;
    }

    public Environment getEnvironment() {
        return environment;
    }
}
