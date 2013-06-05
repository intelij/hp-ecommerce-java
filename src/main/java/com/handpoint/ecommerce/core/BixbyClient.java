package com.handpoint.ecommerce.core;

import com.handpoint.ecommerce.core.exceptions.HpECommerceException;
import com.handpoint.ecommerce.core.exceptions.HpServerError;
import com.handpoint.ecommerce.messages.ErrorMessage;
import com.handpoint.ecommerce.messages.payment.*;
import com.handpoint.ecommerce.messages.token.Token;
import com.handpoint.ecommerce.messages.token.TokenRequest;
import com.sun.jersey.api.client.ClientResponse;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Bixby client knows how to handle requests and responses for all messages sent to the Handpoint
 * E-Commerce web service.
 *
 * @author palmithor
 * @since 2013-03
 */
public class BixbyClient {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private HttpClient httpClient;
    private boolean enableMessageLogging;
    private JaxbMessageConverter messageConverter;
    private String sharedSecret;
    private Environment environment;


    /**
     * Constructor for the client. Sets the environment and shared secret.
     *
     * @param sharedSecret to authenticate with Handpoint E-Commerce web service
     * @param environment  the environment which is used. Live or Test
     */
    public BixbyClient(String sharedSecret, Environment environment) {
        this.sharedSecret = sharedSecret;
        enableMessageLogging = false;
        this.environment = environment;
        init();
    }

    /**
     * @param sharedSecret         to authenticate with Handpoint E-Commerce web service
     * @param environment          the environment which is used. Live or Test
     * @param enableMessageLogging if set to true, all messages will be logged using com.sun.jersey.api.client.filter.LoggingFilter
     */
    public BixbyClient(String sharedSecret, Environment environment, boolean enableMessageLogging) {
        this.sharedSecret = sharedSecret;
        this.enableMessageLogging = enableMessageLogging;
        this.environment = environment;
        init();
    }

    /**
     * Initialize the HttpClient and the message converter.
     */
    private void init() {
        if (enableMessageLogging) {
            httpClient = HttpClient.Builder.create().addLoggingFilter().addHmacFilter(sharedSecret).build();
        } else {
            httpClient = HttpClient.Builder.create().addHmacFilter(sharedSecret).build();
        }
        messageConverter = new JaxbMessageConverter();
    }


    /**
     * Sends authorization requests. If request is either declined or approved a Authorization object is return.
     * If an error occurs, either on the server side or internal an exception is thrown.
     *
     * @param authorizationRequest to be sent to Handpoint E-Commerce web service.
     * @param cardAcceptor         the terminal identifier, used to find the correct merchant.
     * @return Authorization, either approved or declined. If object has approval code has been approved, otherwise declined.
     * @throws HpServerError        if the Handpoint E-Commerce web service returns an error response.
     * @throws HpECommerceException if internal error occurs
     */
    protected Authorization sendAuthorizationRequest(AuthorizationRequest authorizationRequest, String cardAcceptor) throws HpServerError, HpECommerceException {
        String terminalDateTime = dateFormat.format(new Date());
        resetAndAddDateHeader(terminalDateTime);
        ClientResponse response = null;
        try {
            response = httpClient.sendPostRequest(UrlGenerator.getAuthorizationUrl(cardAcceptor, environment), messageConverter.getMessage(AuthorizationRequest.class, authorizationRequest).getBytes(), terminalDateTime);
        } catch (Exception e) {
            throw new HpECommerceException("Error when sending request", e, terminalDateTime);
        }
        if (response.getStatus() == 200 || response.getStatus() == 403) {
            return response.getEntity(Authorization.class);
        } else {
            throw new HpServerError("Authorization declined.", response.getEntity(ErrorMessage.class));
        }
    }

    /**
     * Sends authorization requests. If request is either declined or approved a Authorization object is return.
     * If an error occurs, either on the server side or internal an exception is thrown.
     *
     * @param paymentRequest to be sent to Handpoint E-Commerce web service.
     * @param cardAcceptor   the terminal identifier, used to find the correct merchant.
     * @return Payment, either approved or declined. If object has approval code has been approved, otherwise declined.
     * @throws HpServerError        if the Handpoint E-Commerce web service returns an error response.
     * @throws HpECommerceException if internal error occurs
     */
    protected Payment sendPaymentRequest(PaymentRequest paymentRequest, String cardAcceptor) throws HpServerError, HpECommerceException {
        String terminalDateTime = dateFormat.format(new Date());
        resetAndAddDateHeader(terminalDateTime);
        ClientResponse response = null;
        try {
            response = httpClient.sendPostRequest(UrlGenerator.getPaymentUrl(cardAcceptor, environment), messageConverter.getMessage(PaymentRequest.class, paymentRequest).getBytes(), terminalDateTime);
        } catch (Exception e) {
            throw new HpECommerceException("Error when sending request", e, terminalDateTime);
        }
        if (response.getStatus() == 200 || response.getStatus() == 403) {
            return response.getEntity(Payment.class);
        } else {
            throw new HpServerError("Payment declined.", response.getEntity(ErrorMessage.class));
        }
    }

    /**
     * Sends authorization requests. If request is either declined or approved a Authorization object is return.
     * If an error occurs, either on the server side or internal an exception is thrown.
     *
     * @param refundRequest to be sent to Handpoint E-Commerce web service.
     * @param cardAcceptor  the terminal identifier, used to find the correct merchant.
     * @return Refund, either approved or declined. If object has approval code has been approved, otherwise declined.
     * @throws HpServerError        if the Handpoint E-Commerce web service returns an error response.
     * @throws HpECommerceException if internal error occurs
     */

    protected Refund sendRefundRequest(RefundRequest refundRequest, String cardAcceptor) throws HpServerError, HpECommerceException {
        String terminalDateTime = dateFormat.format(new Date());
        resetAndAddDateHeader(terminalDateTime);
        ClientResponse response = null;
        try {
            response = httpClient.sendPostRequest(UrlGenerator.getRefundUrl(cardAcceptor, environment), messageConverter.getMessage(RefundRequest.class, refundRequest).getBytes(), terminalDateTime);
        } catch (Exception e) {
            throw new HpECommerceException("Error when sending request", e, terminalDateTime);
        }
        if (response.getStatus() == 200 || response.getStatus() == 403) {
            return response.getEntity(Refund.class);
        } else {
            throw new HpServerError("Refund declined.", response.getEntity(ErrorMessage.class));
        }
    }

    /**
     * Sends authorization requests. If request is either declined or approved a Authorization object is return.
     * If an error occurs, either on the server side or internal an exception is thrown.
     *
     * @param reversalRequest to be sent to Handpoint E-Commerce web service.
     * @param cardAcceptor    the terminal identifier, used to find the correct merchant.
     * @return Reversal, either approved or declined.
     * @throws HpServerError        if the Handpoint E-Commerce web service returns an error response.
     * @throws HpECommerceException if internal error occurs
     */
    protected Reversal sendReversalRequest(ReversalRequest reversalRequest, String cardAcceptor) throws HpServerError, HpECommerceException {
        String terminalDateTime = dateFormat.format(new Date());
        resetAndAddDateHeader(terminalDateTime);
        ClientResponse response = null;
        try {
            response = httpClient.sendPostRequest(UrlGenerator.getReversalUrl(cardAcceptor, environment), messageConverter.getMessage(ReversalRequest.class, reversalRequest).getBytes(), terminalDateTime);
        } catch (Exception e) {
            throw new HpECommerceException("Error when sending request", e, terminalDateTime);
        }
        if (response.getStatus() == 200 || response.getStatus() == 403) {
            return response.getEntity(Reversal.class);
        } else {
            throw new HpServerError("Reversing declined.", response.getEntity(ErrorMessage.class));
        }
    }

    /**
     * Sends authorization requests. If request is either declined or approved a Authorization object is return.
     * If an error occurs, either on the server side or internal an exception is thrown.
     *
     * @param cancellationRequest to be sent to Handpoint E-Commerce web service.
     * @param cardAcceptor        the terminal identifier, used to find the correct merchant.
     * @return Cancellation, either approved or declined.
     * @throws HpServerError        if the Handpoint E-Commerce web service returns an error response.
     * @throws HpECommerceException if internal error occurs
     */
    protected Cancellation sendCancellationRequest(CancellationRequest cancellationRequest, String cardAcceptor) throws HpServerError, HpECommerceException {
        String terminalDateTime = dateFormat.format(new Date());
        resetAndAddDateHeader(terminalDateTime);
        ClientResponse response = null;
        try {
            response = httpClient.sendPostRequest(UrlGenerator.getCancellationUrl(cardAcceptor, environment), messageConverter.getMessage(CancellationRequest.class, cancellationRequest).getBytes(), terminalDateTime);
        } catch (Exception e) {
            throw new HpECommerceException("Error when sending request", e, terminalDateTime);
        }
        if (response.getStatus() == 200) {
            return response.getEntity(Cancellation.class);
        } else {
            throw new HpServerError("Cancellation declined.", response.getEntity(ErrorMessage.class));
        }
    }


    /**
     * Sends a request to create token. If it succeeds the token object is returned, otherwise an error is thrown.
     *
     * @param tokenRequest to be sent to Handpoint E-Commerce web service
     * @param cardAcceptor the terminal identifier, used to find the correct merchant.
     * @param token        the card information to store
     * @return a token object with the card information.
     * @throws HpServerError        if the Handpoint E-Commerce web service returns an error response.
     * @throws HpECommerceException if internal error occurs
     */
    protected Token sendPutToken(TokenRequest tokenRequest, String cardAcceptor, String token) throws HpServerError, HpECommerceException {
        String terminalDateTime = dateFormat.format(new Date());
        resetAndAddDateHeader(terminalDateTime);
        ClientResponse response = null;
        try {
            response = httpClient.sendPutRequest(UrlGenerator.getTokenizationUrl(cardAcceptor, token, environment), messageConverter.getMessage(TokenRequest.class, tokenRequest).getBytes());
        } catch (Exception e) {
            throw new HpECommerceException("Error when sending request", e, terminalDateTime);
        }
        if (response.getStatus() == 201) {
            return response.getEntity(Token.class);
        } else {
            throw new HpServerError("Error creating token.", response.getEntity(ErrorMessage.class));
        }
    }


    /**
     * Sends a request to update token. If it succeeds the token object is returned, otherwise an error is thrown.
     *
     * @param tokenRequest to be sent to Handpoint E-Commerce web service
     * @param cardAcceptor the terminal identifier, used to find the correct merchant.
     * @param token        the card information to store
     * @return a token object with the card information.
     * @throws HpServerError        if the Handpoint E-Commerce web service returns an error response.
     * @throws HpECommerceException if internal error occurs
     */
    protected Token sendPostToken(TokenRequest tokenRequest, String cardAcceptor, String token) throws HpServerError, HpECommerceException {
        String terminalDateTime = dateFormat.format(new Date());
        resetAndAddDateHeader(terminalDateTime);
        ClientResponse response = null;
        try {
            response = httpClient.sendPostRequest(UrlGenerator.getTokenizationUrl(cardAcceptor, token, environment), messageConverter.getMessage(TokenRequest.class, tokenRequest).getBytes(), terminalDateTime);
        } catch (Exception e) {
            throw new HpECommerceException("Error when sending request", e, terminalDateTime);
        }
        if (response.getStatus() == 200) {
            return response.getEntity(Token.class);
        } else {
            throw new HpServerError("Error editing token.", response.getEntity(ErrorMessage.class));
        }
    }


    /**
     * Sends a request to get token. If it succeeds the token object is returned, otherwise an error is thrown.
     *
     * @param cardAcceptor the terminal identifier, used to find the correct merchant.
     * @param token        the card information to get
     * @return a token object with the card information.
     * @throws HpServerError        if the Handpoint E-Commerce web service returns an error response.
     * @throws HpECommerceException if internal error occurs
     */
    protected Token sendGetToken(String token, String cardAcceptor) throws HpServerError, HpECommerceException {
        String terminalDateTime = dateFormat.format(new Date());
        resetAndAddDateHeader(terminalDateTime);
        ClientResponse response = null;
        try {
            response = httpClient.sendGetRequest(UrlGenerator.getTokenizationUrl(cardAcceptor, token, environment), terminalDateTime);
        } catch (Exception e) {
            throw new HpECommerceException("Error when sending request", e, terminalDateTime);
        }
        if (response.getStatus() == 200) {
            return response.getEntity(Token.class);
        } else {
            throw new HpServerError("Error getting token.", response.getEntity(ErrorMessage.class));
        }
    }


    /**
     * Sends a request to delete token. If it succeeds the token object is returned, otherwise an error is thrown.
     *
     * @param cardAcceptor the terminal identifier, used to find the correct merchant.
     * @param token        the card information token to be deleted
     * @return a token object with the card information.
     * @throws HpServerError        if the Handpoint E-Commerce web service returns an error response.
     * @throws HpECommerceException if internal error occurs
     */
    protected Token sendDeleteToken(String token, String cardAcceptor) throws HpServerError, HpECommerceException {
        String terminalDateTime = dateFormat.format(new Date());
        resetAndAddDateHeader(terminalDateTime);
        ClientResponse response = null;
        try {
            response = httpClient.sendDeleteRequest(UrlGenerator.getTokenizationUrl(cardAcceptor, token, environment), terminalDateTime);
        } catch (Exception e) {
            throw new HpECommerceException("Error when sending request", e, terminalDateTime);
        }
        if (response.getStatus() == 200) {
            return response.getEntity(Token.class);
        } else {
            throw new HpServerError("Error deleting token.", response.getEntity(ErrorMessage.class));
        }
    }

    private void resetAndAddDateHeader(String terminalDateTime) {
        httpClient.resetHeaders();
        httpClient.addHttpHeader(HmacFilter.MWS_DATE, terminalDateTime);
    }
}
