package com.handpoint.ecommerce.core;

import com.handpoint.ecommerce.core.exceptions.HpECommerceException;
import com.handpoint.ecommerce.core.exceptions.HpServerError;
import com.handpoint.ecommerce.messages.*;
import com.handpoint.ecommerce.messages.payment.*;
import com.handpoint.ecommerce.messages.token.Token;
import com.handpoint.ecommerce.messages.token.TokenRequest;

import javax.xml.bind.JAXBException;
import java.io.IOException;


/**
 * Client to send requests to Bixby E-Commerce Interface
 * Includes all most common actions.
 */
public class ECommerceClient {

    public static final String AUTHORIZATION = "authorization";
    public static final String PAYMENT = "payment";
    public static final String REFUND = "refund";
    private static String WEB_PAYMENT_SCENARIO = "WEB";
    private BixbyClient client;

    private String cardAcceptor;

    /**
     * Default constructor. Instantiates Bixby Client which is used to send the actual HTTP request.
     * Logging filter is disabled.
     *
     * @param eCommerceUser object that includes shared secret and card acceptor / terminal id for authentication
     */
    public ECommerceClient(ECommerceUser eCommerceUser) {
        this.cardAcceptor = eCommerceUser.getCardAcceptor();
        client = new BixbyClient(eCommerceUser.getSharedSecret(), eCommerceUser.getEnvironment(), false);
    }


    /**
     * Constructor used if message logging is needed. Instantiates Bixby Client which is used to send the actual HTTP request.
     * Logging filter is enabled if enableMessageLogging is true otherwise not.
     *
     * @param eCommerceUser        object that includes shared secret and card acceptor / terminal id for authentication
     * @param enableMessageLogging if true message logging is turned on otherwise it is not.
     */
    public ECommerceClient(ECommerceUser eCommerceUser, boolean enableMessageLogging) {
        this.cardAcceptor = eCommerceUser.getCardAcceptor();
        client = new BixbyClient(eCommerceUser.getSharedSecret(), eCommerceUser.getEnvironment(), enableMessageLogging);
    }

    /**
     * Default constructor. Instantiates Bixby Client which is used to send the actual HTTP request.
     * Logging filter is disabled.
     *
     * @param cardAcceptor the cardAcceptor to use. Assigned by Handpoint
     * @param sharedSecret the sharedSecret used to authenticate against Handpoint ECommerce API
     * @param environment enum used to set environment to either TEST or LIVE
     */
    public ECommerceClient(String cardAcceptor, String sharedSecret, Environment environment) {
        this.cardAcceptor = cardAcceptor;
        client = new BixbyClient(sharedSecret, environment);
    }

    /**
     * Default constructor. Instantiates Bixby Client which is used to send the actual HTTP request.
     * Logging filter is disabled.
     *
     * @param cardAcceptor the cardAcceptor to use. Assigned by Handpoint
     * @param sharedSecret the sharedSecret used to authenticate against Handpoint ECommerce API
     * @param environment enum used to set environment to either TEST or LIVE
     * @param enableMessageLogging if true message logging filter is added to the http client, otherwise not.
     */
    public ECommerceClient(String cardAcceptor, String sharedSecret, Environment environment, boolean enableMessageLogging) {
        this.cardAcceptor = cardAcceptor;
        client = new BixbyClient(sharedSecret, environment, enableMessageLogging);
    }


    /**
     * Regular authorization with Customer reference and without card verification code
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount used to authorize.
     * @param cardNumber        the card number to charge to
     * @param expiryDateMMYY    the expiry date of the card
     * @param customerReference
     * @return either granted authorization or declined. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Authorization authorize(String currency, String amount, String cardNumber, String expiryDateMMYY, String customerReference) throws HpECommerceException, HpServerError {
        try {
            AuthorizationRequest request = MessageCreator.authorizationRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    cardNumber, expiryDateMMYY, null, customerReference);
            return client.sendAuthorizationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Authorization failed", e);
        }
    }

    /**
     * Regular authorization without Customer reference and without card verification code
     *
     * @param currency       the currency to use. Currency enum used.
     * @param amount         the amount charged for
     * @param cardNumber     the card number to charge to
     * @param expiryDateMMYY the expiry date of the card
     * @return either granted authorization or declined. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Authorization authorize(String currency, String amount, String cardNumber, String expiryDateMMYY) throws HpECommerceException, HpServerError {
        try {
            AuthorizationRequest request = MessageCreator.authorizationRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    cardNumber, expiryDateMMYY, null, null);
            return client.sendAuthorizationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Authorization failed", e);
        }
    }

    /**
     * Regular authorization with Customer reference and with card verification code
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount used to authorize.
     * @param cardNumber        the card number to charge to
     * @param expiryDateMMYY    the expiry date of the card
     * @param customerReference
     * @return either granted authorization or declined. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Authorization authorizeWithCVC(String currency, String amount, String cardNumber, String expiryDateMMYY, String cardVerificationCode, String customerReference) throws HpECommerceException, HpServerError {
        try {
            AuthorizationRequest request = MessageCreator.authorizationRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    cardNumber, expiryDateMMYY, cardVerificationCode, customerReference);
            return client.sendAuthorizationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Authorization failed", e);
        }
    }

    /**
     * Regular authorization without Customer reference and with card verification code
     *
     * @param currency             the currency to use. Currency enum used.
     * @param amount               the amount charged for
     * @param cardNumber           the card number to charge to
     * @param expiryDateMMYY       the expiry date of the card
     * @param cardVerificationCode the card verification code, usually found on the back of the card. Three or four digits.
     * @return either granted authorization or declined. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Authorization authorizeWithCVC(String currency, String amount, String cardNumber, String expiryDateMMYY, String cardVerificationCode) throws HpECommerceException, HpServerError {
        try {
            AuthorizationRequest request = MessageCreator.authorizationRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    cardNumber, expiryDateMMYY, cardVerificationCode, null);
            return client.sendAuthorizationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Authorization failed", e);
        }
    }


    /**
     * Authorization using token with Customer Reference
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount charged for
     * @param token             has to be stored in the customers token store
     * @param customerReference
     * @return either granted authorization or declined. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Authorization authorizeWithToken(String currency, String amount, String token, String customerReference) throws HpECommerceException, HpServerError {
        try {
            AuthorizationRequest request = MessageCreator.authorizationRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    null, null, null, customerReference);
            return client.sendAuthorizationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Authorization failed", e);
        }
    }

    /**
     * Authorization using token without Customer Reference
     *
     * @param currency the currency to use. Currency enum used.
     * @param amount   the amount charged for
     * @param token    has to be stored in the customers token store
     * @return either granted authorization or declined. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Authorization authorizeWithToken(String currency, String amount, String token) throws HpECommerceException, HpServerError {
        try {
            AuthorizationRequest request = MessageCreator.authorizationRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    null, null, null, null);
            return client.sendAuthorizationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Authorization failed", e);
        }
    }

    /**
     * Authorization that authorizes and stores the token. With customer reference
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount charged for
     * @param cardNumber        the card number to charge to
     * @param expiryDateMMYY    the expiry date of the card
     * @param token             used to identify the stored card number
     * @param customerReference
     * @return either granted authorization or declined. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Authorization authorizeAndStoreToken(String currency, String amount, String cardNumber, String expiryDateMMYY, String token, String customerReference) throws HpECommerceException, HpServerError {
        try {
            AuthorizationRequest request = MessageCreator.authorizationRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    cardNumber, expiryDateMMYY, null, customerReference);
            return client.sendAuthorizationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Authorization failed", e);
        }
    }

    /**
     * Authorization that authorizes and stores the token. Without customer reference
     *
     * @param currency       the currency to use. Currency enum used.
     * @param amount         the amount charged for
     * @param cardNumber     the card number to charge to
     * @param expiryDateMMYY the expiry date of the card
     * @param token          used to identify the stored card number
     * @return either granted authorization or declined. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Authorization authorizeAndStoreToken(String currency, String amount, String cardNumber, String expiryDateMMYY, String token) throws HpECommerceException, HpServerError {
        try {
            AuthorizationRequest request = MessageCreator.authorizationRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    cardNumber, expiryDateMMYY, null, null);
            return client.sendAuthorizationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Authorization failed", e);
        }
    }

    /**
     * Regular payment with Customer reference and without card verification code
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount used to authorize.
     * @param cardNumber        the card number to charge to
     * @param expiryDateMMYY    the expiry date of the card
     * @param customerReference
     * @return either successful payment or unsuccessful. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Payment payment(String currency, String amount, String cardNumber, String expiryDateMMYY, String customerReference) throws HpECommerceException, HpServerError {
        try {
            PaymentRequest request = MessageCreator.paymentRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    cardNumber, expiryDateMMYY, null, customerReference, null);
            return client.sendPaymentRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Payment failed", e);
        }
    }

    /**
     * Regular payment without Customer reference and without card verification code
     *
     * @param currency       the currency to use. Currency enum used.
     * @param amount         the amount charged for
     * @param cardNumber     the card number to charge to
     * @param expiryDateMMYY the expiry date of the card
     * @return either successful payment or unsuccessful. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Payment payment(String currency, String amount, String cardNumber, String expiryDateMMYY) throws HpECommerceException, HpServerError {
        try {
            PaymentRequest request = MessageCreator.paymentRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    cardNumber, expiryDateMMYY, null, null, null);
            return client.sendPaymentRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Payment failed", e);
        }
    }

    /**
     * Regular payment with Customer reference and with card verification code
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount used to authorize.
     * @param cardNumber        the card number to charge to
     * @param expiryDateMMYY    the expiry date of the card
     * @param customerReference
     * @return either successful payment or unsuccessful. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Payment paymentWithCVC(String currency, String amount, String cardNumber, String expiryDateMMYY, String cardVerificationCode, String customerReference) throws HpECommerceException, HpServerError {
        try {
            PaymentRequest request = MessageCreator.paymentRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    cardNumber, expiryDateMMYY, cardVerificationCode, customerReference, null);
            return client.sendPaymentRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Payment failed", e);
        }
    }

    /**
     * Regular authorization without Customer reference and with card verification code
     *
     * @param currency       the currency to use. Currency enum used.
     * @param amount         the amount used to authorize.
     * @param cardNumber     the card number to charge to
     * @param expiryDateMMYY the expiry date of the card
     * @return either successful payment or unsuccessful. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Payment paymentWithCVC(String currency, String amount, String cardNumber, String expiryDateMMYY, String cardVerificationCode) throws HpECommerceException, HpServerError {
        try {
            PaymentRequest request = MessageCreator.paymentRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    cardNumber, expiryDateMMYY, cardVerificationCode, null, null);
            return client.sendPaymentRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Payment failed", e);
        }
    }

    /**
     * Payment using token with Customer Reference
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount charged for
     * @param token             has to be stored in the customers token store
     * @param customerReference
     * @return either successful payment or unsuccessful. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Payment paymentWithToken(String currency, String amount, String token, String customerReference) throws HpECommerceException, HpServerError {
        try {
            PaymentRequest request = MessageCreator.paymentRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    null, null, null, customerReference, null);
            return client.sendPaymentRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Payment failed", e);
        }
    }

    /**
     * Payment using token without Customer Reference
     *
     * @param currency the currency to use. Currency enum used.
     * @param amount   the amount charged for
     * @param token    has to be stored in the customers token store
     * @return either successful payment or unsuccessful. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Payment paymentWithToken(String currency, String amount, String token) throws HpECommerceException, HpServerError {
        try {
            PaymentRequest request = MessageCreator.paymentRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    null, null, null, null, null);
            return client.sendPaymentRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Payment failed", e);
        }
    }


    /**
     * Payment that authorizes and confirms and stores the token. With customer reference
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount charged for
     * @param cardNumber        the card number to charge to
     * @param expiryDateMMYY    the expiry date of the card
     * @param token             used to identify the stored card number
     * @param customerReference
     * @return either successful payment or unsuccessful. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Payment paymentAndStoreToken(String currency, String amount, String cardNumber, String expiryDateMMYY, String token, String customerReference) throws HpECommerceException, HpServerError {
        try {
            PaymentRequest request = MessageCreator.paymentRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    cardNumber, expiryDateMMYY, null, customerReference, null);
            return client.sendPaymentRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Payment failed", e);
        }
    }

    /**
     * Payment that authorizes and confirms and stores the token. With customer reference
     *
     * @param currency       the currency to use. Currency enum used.
     * @param amount         the amount charged for
     * @param cardNumber     the card number to charge to
     * @param expiryDateMMYY the expiry date of the card
     * @param token          used to identify the stored card number
     * @return either successful payment or unsuccessful. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Payment paymentAndStoreToken(String currency, String amount, String cardNumber, String expiryDateMMYY, String token) throws HpECommerceException, HpServerError {
        try {
            PaymentRequest request = MessageCreator.paymentRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    cardNumber, expiryDateMMYY, null, null, null);
            return client.sendPaymentRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Payment failed", e);
        }
    }

    /**
     * Payment that captures previously approved authorization. Without customer reference.
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount charged for
     * @param authorizationGuid retrieved with the original authorization
     * @return either successful payment or unsuccessful. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Payment captureAuthorization(String currency, String amount, String authorizationGuid) throws HpECommerceException, HpServerError {
        try {
            PaymentRequest request = MessageCreator.paymentRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    null, null, null, null, authorizationGuid);
            return client.sendPaymentRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Confirming authorization failed", e);
        }
    }

    /**
     * Refund with customer reference
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount to refund.
     * @param cardNumber        the card number to refund to.
     * @param expiryDateMMYY    the expiry date of the card
     * @param customerReference
     * @return either a successful or unsuccessful refund. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Refund refund(String currency, String amount, String cardNumber, String expiryDateMMYY, String customerReference) throws HpECommerceException, HpServerError {
        try {
            RefundRequest request = MessageCreator.refundRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    cardNumber, expiryDateMMYY, null, customerReference, null);
            return client.sendRefundRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Refunding failed", e);
        }
    }

    /**
     * Refund without customer reference
     *
     * @param currency       the currency to use. Currency enum used.
     * @param amount         the amount to refund.
     * @param cardNumber     the card number to refund to.
     * @param expiryDateMMYY the expiry date of the card
     * @return either a successful or unsuccessful refund. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Refund refund(String currency, String amount, String cardNumber, String expiryDateMMYY) throws HpECommerceException, HpServerError {

        try {
            RefundRequest request = MessageCreator.refundRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    cardNumber, expiryDateMMYY, null, null, null);
            return client.sendRefundRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Refunding failed", e);
        }
    }

    /**
     * Refund using token. With customer reference.
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount to refund.
     * @param token             used to identify the stored card number
     * @param customerReference
     * @return either a successful or unsuccessful refund. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Refund refundWithToken(String currency, String amount, String token, String customerReference) throws HpECommerceException, HpServerError {
        try {
            RefundRequest request = MessageCreator.refundRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    null, null, null, customerReference, null);
            return client.sendRefundRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Refunding failed", e);
        }
    }

    /**
     * Refund using token. Without customer reference.
     *
     * @param currency the currency to use. Currency enum used.
     * @param amount   the amount to refund.
     * @param token    used to identify the stored card number
     * @return either a successful or unsuccessful refund. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Refund refundWithToken(String currency, String amount, String token) throws HpECommerceException, HpServerError {
        try {
            RefundRequest request = MessageCreator.refundRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    null, null, null, null, null);
            return client.sendRefundRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Refunding failed", e);
        }
    }


    /**
     * Refund and store the token. With customer reference
     *
     * @param currency          the currency to use. Currency enum used.
     * @param amount            the amount charged for
     * @param cardNumber        the card number to charge to
     * @param expiryDateMMYY    the expiry date of the card
     * @param token             used to identify the stored card number
     * @param customerReference
     * @return either successful or unsuccessful refund. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Refund refundAndStoreToken(String currency, String amount, String cardNumber, String expiryDateMMYY, String token, String customerReference) throws HpECommerceException, HpServerError {
        try {
            RefundRequest request = MessageCreator.refundRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    cardNumber, expiryDateMMYY, null, customerReference, null);
            return client.sendRefundRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Refunding failed", e);
        }
    }

    /**
     * Refund and store the token. Without customer reference
     *
     * @param currency       the currency to use. Currency enum used.
     * @param amount         the amount charged for
     * @param cardNumber     the card number to charge to
     * @param expiryDateMMYY the expiry date of the card
     * @param token          used to identify the stored card number
     * @return either successful or unsuccessful refund. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Refund refundAndStoreToken(String currency, String amount, String cardNumber, String expiryDateMMYY, String token) throws HpECommerceException, HpServerError {
        try {
            RefundRequest request = MessageCreator.refundRequest(WEB_PAYMENT_SCENARIO, currency, amount, token,
                    cardNumber, expiryDateMMYY, null, null, null);
            return client.sendRefundRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Refunding failed", e);
        }
    }

    /**
     * Refund for previously approved payment, called linked refund.
     *
     * @param currency    the currency to use. Currency enum used.
     * @param amount      the amount charged for
     * @param paymentGuid of the payment that shall be refunded.
     * @return either successful or unsuccessful refund. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Refund refundPayment(String currency, String amount, String paymentGuid) throws HpECommerceException, HpServerError {
        try {
            RefundRequest request = MessageCreator.refundRequest(WEB_PAYMENT_SCENARIO, currency, amount, null,
                    null, null, null, null, paymentGuid);
            return client.sendRefundRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Refunding payment failed", e);
        }
    }

    /**
     * Reversal for previously granted authorization without customer reference
     *
     * @param authorizationGuid the GUID for the authorization to reverse
     * @return either successful or unsuccessful reversal. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Reversal reverseAuthorization(String authorizationGuid) throws HpECommerceException, HpServerError {
        try {
            ReversalRequest request = MessageCreator.reversalRequest(authorizationGuid, null, null, null);
            return client.sendReversalRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Reversing authorization failed", e);
        }
    }


    /**
     * Reversal for previously granted authorization with customer reference
     *
     * @param authorizationGuid the GUID for the authorization to reverse
     * @param customerReference
     * @return either successful or unsuccessful reversal. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Reversal reverseAuthorization(String authorizationGuid, String customerReference) throws HpECommerceException, HpServerError {
        try {
            ReversalRequest request = MessageCreator.reversalRequest(authorizationGuid, null, null, customerReference);
            return client.sendReversalRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Reversing authorization failed", e);
        }
    }

    /**
     * Reversal for previously successful payment without customer reference
     *
     * @param paymentGuid of the payment to reverse.
     * @return either successful or unsuccessful reversal. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Reversal reversePayment(String paymentGuid) throws HpECommerceException, HpServerError {
        try {
            ReversalRequest request = MessageCreator.reversalRequest(null, paymentGuid, null, null);
            return client.sendReversalRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Reversing payment failed", e);
        }
    }

    /**
     * Reversal for previously successful payment with customer reference
     *
     * @param paymentGuid       of the payment to reverse.
     * @param customerReference
     * @return either successful or unsuccessful reversal. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Reversal reversePayment(String paymentGuid, String customerReference) throws HpECommerceException, HpServerError {
        try {
            ReversalRequest request = MessageCreator.reversalRequest(null, paymentGuid, null, customerReference);
            return client.sendReversalRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Reversing payment failed", e);
        }
    }

    /**
     * Reversal for previously successful refund without customer reference
     *
     * @param refundGuid of the refund to reverse.
     * @return either successful or unsuccessful reversal. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Reversal reverseRefund(String refundGuid) throws HpECommerceException, HpServerError {
        try {
            ReversalRequest request = MessageCreator.reversalRequest(null, null, refundGuid, null);
            return client.sendReversalRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Reversing refund failed", e);
        }
    }

    /**
     * Reversal for previously successful payment with customer reference
     *
     * @param refundGuid        of the payment to reverse.
     * @param customerReference
     * @return either successful or unsuccessful reversal. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Reversal reverseRefund(String refundGuid, String customerReference) throws HpECommerceException, HpServerError {
        try {
            ReversalRequest request = MessageCreator.reversalRequest(null, null, refundGuid, customerReference);
            return client.sendReversalRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Reversing refund failed", e);
        }
    }

    /**
     * Cancellation for previously granted authorization.
     *
     * @param currency                 the currency to use. Currency enum used.
     * @param amount                   the amount charged for
     * @param terminalDateTimeOriginal the terminal date time sent in the original authorization
     * @return either successful or unsuccessful cancellation. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Cancellation cancelAuthorization(String currency, String amount, String terminalDateTimeOriginal) throws HpECommerceException, HpServerError {
        try {
            CancellationRequest request = MessageCreator.cancellationRequest(AUTHORIZATION, currency, amount, terminalDateTimeOriginal);
            return client.sendCancellationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Cancelling authorization failed", e);
        }
    }

    /**
     * Cancellation for previously granted payment.
     *
     * @param currency                 the currency to use. Currency enum used.
     * @param amount                   the amount charged for
     * @param terminalDateTimeOriginal the terminal date time sent in the original authorization
     * @return either successful or unsuccessful cancellation. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Cancellation cancelPayment(String currency, String amount, String terminalDateTimeOriginal) throws HpECommerceException, HpServerError {
        try {
            CancellationRequest request = MessageCreator.cancellationRequest(PAYMENT, currency, amount, terminalDateTimeOriginal);
            return client.sendCancellationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Cancelling payment failed", e);
        }
    }

    /**
     * Cancellation for previously granted refund.
     *
     * @param currency                 the currency to use. Currency enum used.
     * @param amount                   the amount charged for
     * @param terminalDateTimeOriginal the terminal date time sent in the original authorization
     * @return either successful or unsuccessful cancellation. Approval code is not set if it is declined.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */

    public Cancellation cancelRefund(String currency, String amount, String terminalDateTimeOriginal) throws HpECommerceException, HpServerError {
        try {
            CancellationRequest request = MessageCreator.cancellationRequest(REFUND, currency, amount, terminalDateTimeOriginal);
            return client.sendCancellationRequest(request, cardAcceptor);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Cancelling refund failed", e);
        }

    }

    /**
     * Store token
     *
     * @param token          the identity of the card in the card storage
     * @param cardNumber     the card number to store in the card storage
     * @param expiryDateMMYY the expiry date of the card number to store
     * @return Information about the card number stored.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Token createToken(String token, String cardNumber, String expiryDateMMYY) throws HpECommerceException, HpServerError {
        try {
            TokenRequest request = MessageCreator.tokenRequest(cardNumber, expiryDateMMYY);
            return client.sendPutToken(request, cardAcceptor, token);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Unable to create token", e);
        }
    }

    /**
     * Update previously stored token
     *
     * @param token          the identity of the card in the card storage
     * @param cardNumber     the card number to store in the card storage
     * @param expiryDateMMYY the expiry date of the card number to store
     * @return Information about the card number updated.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Token editToken(String token, String cardNumber, String expiryDateMMYY) throws HpECommerceException, HpServerError {
        try {
            TokenRequest request = MessageCreator.tokenRequest(cardNumber, expiryDateMMYY);
            return client.sendPostToken(request, cardAcceptor, token);
        } catch (JAXBException | InvalidMessageException | IOException e) {
            throw new HpServerError("Unable to edit token", e);
        }
    }

    /**
     * Get token request. Get the information about a previously stored token.
     *
     * @param token the identity of the card in the card storage
     * @return Information about the card number retrieved.
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Token getToken(String token) throws HpECommerceException, HpServerError {
        try {
            return client.sendGetToken(token, cardAcceptor);
        } catch (JAXBException | IOException e) {
            throw new HpECommerceException("Unable to retrieve token", e);
        }
    }

    /**
     * Delete a previously stored token
     *
     * @param token the identity of the card in the card storage
     * @throws com.handpoint.ecommerce.core.exceptions.HpECommerceException if an error occurs converting messages, message doesn't have sufficient data or an IO error occurs.
     * @throws com.handpoint.ecommerce.core.exceptions.HpServerError if the server returns an error. HpServerError.getErrors() has detailed error message.
     *                                 is thrown if response is not 200 or 401, that is if server response with <error></error>.
     */
    public Token deleteToken(String token) throws HpECommerceException, HpServerError {
        try {
            return client.sendDeleteToken(token, cardAcceptor);
        } catch (JAXBException | IOException e) {
            throw new HpECommerceException("Error deleting token", e);
        }
    }
}