package com.handpoint.ecommerce.core;


import com.handpoint.ecommerce.core.config.Config;
import com.handpoint.ecommerce.core.exceptions.HpServerError;

/**
 * Class used to generate urls
 *
 * @since 3/1/13 1:28 PM
 * @author palmithor
 */
public class UrlGenerator {

    public static final String ECOMMERCE_PATH = "/web/";
    public static final String TOKENIZATION_PATH = "/tokenstore/";

    public static String getAuthorizationUrl(String cardAcceptor, Environment environment) throws HpServerError {
        StringBuilder builder = new StringBuilder();
        builder.append(getECommerceBaseUrl(cardAcceptor, environment));
        builder.append("/authorization/");
        return builder.toString();
    }

    public static String getPaymentUrl(String cardAcceptor, Environment environment) throws HpServerError {
        StringBuilder builder = new StringBuilder();
        builder.append(getECommerceBaseUrl(cardAcceptor, environment));
        builder.append("/payment/");
        return builder.toString();
    }

    public static String getRefundUrl(String cardAcceptor, Environment environment) throws HpServerError {
        StringBuilder builder = new StringBuilder();
        builder.append(getECommerceBaseUrl(cardAcceptor, environment));
        builder.append("/refund/");
        return builder.toString();
    }

    public static String getReversalUrl(String cardAcceptor, Environment environment) throws HpServerError {
        StringBuilder builder = new StringBuilder();
        builder.append(getECommerceBaseUrl(cardAcceptor, environment));
        builder.append("/reversal/");
        return builder.toString();
    }

    public static String getCancellationUrl(String cardAcceptor, Environment environment) throws HpServerError {
        StringBuilder builder = new StringBuilder();
        builder.append(getECommerceBaseUrl(cardAcceptor, environment));
        builder.append("/cancellation/");
        return builder.toString();
    }

    public static String getTokenizationUrl(String cardAcceptor, String tokenName, Environment environment) throws HpServerError {
        StringBuilder builder = new StringBuilder();
        builder.append(getTokenizationBaseUrl(cardAcceptor, environment));
        builder.append("/");
        builder.append(tokenName);
        builder.append("/");
        return builder.toString();
    }

    private static String getECommerceBaseUrl(String cardAcceptor, Environment environment) throws HpServerError {
        StringBuilder builder = new StringBuilder();
        if (environment == Environment.LIVE) {
            builder.append(Config.getInstance().getLiveBaseUrl());
        } else if (environment == Environment.TEST) {
            builder.append(Config.getInstance().getTestBaseUrl());
        }
        builder.append(ECOMMERCE_PATH);
        builder.append(cardAcceptor);
        return builder.toString();
    }

    private static String getTokenizationBaseUrl(String cardAcceptor, Environment environment) throws HpServerError {
        StringBuilder builder = new StringBuilder();
        if (environment == Environment.LIVE) {
            builder.append(Config.getInstance().getLiveBaseUrl());
        } else if (environment == Environment.TEST) {
            builder.append(Config.getInstance().getTestBaseUrl());
        }
        builder.append(TOKENIZATION_PATH);
        builder.append(cardAcceptor);
        return builder.toString();
    }
}
