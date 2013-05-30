package com.handpoint.ecommerce.core;

import com.handpoint.ecommerce.core.exceptions.HpServerError;
import com.handpoint.ecommerce.messages.*;
import com.handpoint.ecommerce.messages.payment.*;
import com.handpoint.ecommerce.messages.token.Token;
import com.handpoint.ecommerce.messages.token.TokenRequest;
import com.sun.jersey.api.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 *
 * @since 2013-03
 * @author palmithor
 */
public class BixbyClient {

    private Logger logger = LoggerFactory.getLogger(BixbyClient.class);
    private RestClient restClient;
    private boolean enableMessageLogging;
    private SimpleDateFormat dateFormat;
    private JaxbMessageConverter messageConverter;
    private String sharedSecret;
    private Environment environment;



    public BixbyClient(String sharedSecret, Environment environment) {
        this.sharedSecret = sharedSecret;
        enableMessageLogging = false;
        this.environment = environment;
        init();
    }

    public BixbyClient(String sharedSecret, Environment environment, boolean enableMessageLogging) {
        this.sharedSecret = sharedSecret;
        this.enableMessageLogging = enableMessageLogging;
        this.environment = environment;
        init();
    }

    private void init() {
        if (enableMessageLogging) {
            restClient = RestClient.Builder.create().addLoggingFilter().addHmacFilter(sharedSecret).build();
        } else {
            restClient = RestClient.Builder.create().addHmacFilter(sharedSecret).build();
        }
        dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        messageConverter = new JaxbMessageConverter();
    }


    protected Authorization sendAuthorizationRequest(AuthorizationRequest request, String cardAcceptor) throws JAXBException, IOException, HpServerError {
        ClientResponse response = restClient.sendPostRequest(UrlGenerator.getAuthorizationUrl(cardAcceptor, environment), messageConverter.getMessage(AuthorizationRequest.class, request).getBytes());
        if (response.getStatus() == 200 || response.getStatus() == 403) {
            return response.getEntity(Authorization.class);
        } else {
            throw new HpServerError("Authorization declined.", response.getEntity(ErrorMessage.class));
        }
    }

    protected Payment sendPaymentRequest(PaymentRequest request, String cardAcceptor) throws JAXBException, IOException, HpServerError {
        ClientResponse response = restClient.sendPostRequest(UrlGenerator.getPaymentUrl(cardAcceptor, environment), messageConverter.getMessage(PaymentRequest.class, request).getBytes());
        if (response.getStatus() == 200 || response.getStatus() == 403) {
            return response.getEntity(Payment.class);
        } else {
            throw new HpServerError("Payment declined.", response.getEntity(ErrorMessage.class));
        }
    }

    protected Refund sendRefundRequest(RefundRequest request, String cardAcceptor) throws JAXBException, IOException, HpServerError {
        ClientResponse response = restClient.sendPostRequest(UrlGenerator.getRefundUrl(cardAcceptor, environment), messageConverter.getMessage(RefundRequest.class, request).getBytes());
        if (response.getStatus() == 200 || response.getStatus() == 403) {
            return response.getEntity(Refund.class);
        } else {
            throw new HpServerError("Refund declined.", response.getEntity(ErrorMessage.class));
        }
    }

    protected Reversal sendReversalRequest(ReversalRequest request, String cardAcceptor) throws JAXBException, IOException, HpServerError {
        ClientResponse response = restClient.sendPostRequest(UrlGenerator.getReversalUrl(cardAcceptor, environment), messageConverter.getMessage(ReversalRequest.class, request).getBytes());
        if (response.getStatus() == 200 || response.getStatus() == 403) {
            return response.getEntity(Reversal.class);
        } else {
            throw new HpServerError("Reversing declined.", response.getEntity(ErrorMessage.class));
        }
    }

    protected Cancellation sendCancellationRequest(CancellationRequest request, String cardAcceptor) throws JAXBException, IOException, HpServerError {
        ClientResponse response = restClient.sendPostRequest(UrlGenerator.getCancellationUrl(cardAcceptor, environment), messageConverter.getMessage(CancellationRequest.class, request).getBytes());
        if (response.getStatus() == 200) {
            return response.getEntity(Cancellation.class);
        } else {
            throw new HpServerError("Cancellation declined.", response.getEntity(ErrorMessage.class));
        }
    }

    protected Token sendPutToken(TokenRequest request, String cardAcceptor, String token) throws JAXBException, IOException, HpServerError {
        ClientResponse response = restClient.sendPutRequest(UrlGenerator.getTokenizationUrl(cardAcceptor, token, environment), messageConverter.getMessage(TokenRequest.class, request).getBytes());
        if (response.getStatus() == 201) {
            return response.getEntity(Token.class);
        } else {
            throw new HpServerError("Error creating token.", response.getEntity(ErrorMessage.class));
        }
    }

    protected Token sendPostToken(TokenRequest request, String cardAcceptor, String token) throws JAXBException, IOException, HpServerError {
        ClientResponse response = restClient.sendPostRequest(UrlGenerator.getTokenizationUrl(cardAcceptor, token, environment), messageConverter.getMessage(TokenRequest.class, request).getBytes());
        if (response.getStatus() == 200) {
            return response.getEntity(Token.class);
        } else {
            throw new HpServerError("Error editing token.", response.getEntity(ErrorMessage.class));
        }
    }

    protected Token sendGetToken(String token, String cardAcceptor) throws JAXBException, IOException, HpServerError {
        ClientResponse response = restClient.sendGetRequest(UrlGenerator.getTokenizationUrl(cardAcceptor, token, environment));
        if (response.getStatus() == 200) {
            return response.getEntity(Token.class);
        } else {
            throw new HpServerError("Error getting token.", response.getEntity(ErrorMessage.class));
        }
    }

    protected Token sendDeleteToken(String token, String cardAcceptor) throws JAXBException, IOException, HpServerError {
        ClientResponse response = restClient.sendDeleteRequest(UrlGenerator.getTokenizationUrl(cardAcceptor, token, environment));
        if (response.getStatus() == 200) {
            return response.getEntity(Token.class);
        } else {
            throw new HpServerError("Error deleting token.", response.getEntity(ErrorMessage.class));
        }
    }
}
