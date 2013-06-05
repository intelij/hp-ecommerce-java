package com.handpoint.ecommerce.core;

import com.handpoint.ecommerce.core.exceptions.InvalidMessageException;
import com.handpoint.ecommerce.messages.payment.*;
import com.handpoint.ecommerce.messages.token.TokenRequest;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

/**
 * Class used to create request messages and validate data
 */
public class MessageCreator {

    /**
     * Create and validate E-Commerce authorization message
     *
     * @param paymentScenario
     * @param currency
     * @param amount
     * @param token
     * @param cardNumber
     * @param expiryDateMMYY
     * @param cardVerificationCode
     * @param customerReference
     * @return
     * @throws InvalidMessageException if message fails validation
     */
    protected static AuthorizationRequest authorizationRequest(String paymentScenario, String currency, String amount,
                                                               String token, String cardNumber, String expiryDateMMYY,
                                                               String cardVerificationCode, String customerReference) throws InvalidMessageException {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setPaymentScenario(paymentScenario);
        authorizationRequest.setCurrency(currency);
        authorizationRequest.setAmount(amount);
        authorizationRequest.setToken(token);
        authorizationRequest.setCardNumber(cardNumber);
        authorizationRequest.setExpiryDateMMYY(expiryDateMMYY);
        authorizationRequest.setCardVerificationCode(cardVerificationCode);
        authorizationRequest.setCustomerReference(customerReference);
        Set<ConstraintViolation<AuthorizationRequest>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(authorizationRequest);
        if (!constraintViolations.isEmpty()) {
            throw new InvalidMessageException(getConstraintViolationMessage(constraintViolations));
        } else if (!hasValidCardData(authorizationRequest)) {
            throw new InvalidMessageException("Invalid card data. Must include either token or card number and expiry date");
        }
        return authorizationRequest;
    }

    /**
     * Create and validate E-Commerce payment message
     *
     * @param paymentScenario
     * @param currency
     * @param amount
     * @param token
     * @param cardNumber
     * @param expiryDateMMYY
     * @param cardVerificationCode
     * @param customerReference
     * @param authorizationGuid
     * @return
     * @throws InvalidMessageException if message fails validation
     */
    protected static PaymentRequest paymentRequest(String paymentScenario, String currency, String amount,
                                                   String token, String cardNumber, String expiryDateMMYY,
                                                   String cardVerificationCode, String customerReference, String authorizationGuid) throws InvalidMessageException {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAuthorizationGuid(authorizationGuid);
        paymentRequest.setPaymentScenario(paymentScenario);
        paymentRequest.setCurrency(currency);
        paymentRequest.setAmount(amount);
        paymentRequest.setToken(token);
        paymentRequest.setCardNumber(cardNumber);
        paymentRequest.setExpiryDateMMYY(expiryDateMMYY);
        paymentRequest.setCardVerificationCode(cardVerificationCode);
        paymentRequest.setCustomerReference(customerReference);

        Set<ConstraintViolation<PaymentRequest>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(paymentRequest);

        if (!constraintViolations.isEmpty()) {
            throw new InvalidMessageException(getConstraintViolationMessage(constraintViolations));
        } else if (!hasValidCardData(paymentRequest)) {
            throw new InvalidMessageException("Invalid card data. Must include either token or card number and expiry date");
        }
        return paymentRequest;
    }

    /**
     * Create and validate E-Commerce refund message
     *
     * @param paymentScenario
     * @param currency
     * @param amount
     * @param token
     * @param cardNumber
     * @param expiryDateMMYY
     * @param cardVerificationCode
     * @param customerReference
     * @param paymentGuid
     * @return
     * @throws InvalidMessageException if message fails validation
     */
    protected static RefundRequest refundRequest(String paymentScenario, String currency, String amount,
                                                 String token, String cardNumber, String expiryDateMMYY,
                                                 String cardVerificationCode, String customerReference, String paymentGuid) throws InvalidMessageException {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setPaymentGuid(paymentGuid);
        refundRequest.setPaymentScenario(paymentScenario);
        refundRequest.setCurrency(currency);
        refundRequest.setAmount(amount);
        refundRequest.setToken(token);
        refundRequest.setCardNumber(cardNumber);
        refundRequest.setExpiryDateMMYY(expiryDateMMYY);
        refundRequest.setCustomerReference(customerReference);

        Set<ConstraintViolation<RefundRequest>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(refundRequest);

        if (!constraintViolations.isEmpty()) {
            throw new InvalidMessageException(getConstraintViolationMessage(constraintViolations));
        } else if (!hasValidCardData(refundRequest)) {
            throw new InvalidMessageException("Invalid card data. Must include either token or card number and expiry date");
        }
        return refundRequest;
    }

    /**
     * Create and validate E-Commerce reversal message
     *
     * @param authorizationGuid
     * @param paymentGuid
     * @param refundGuid
     * @param cusomterReference
     * @return
     * @throws InvalidMessageException if message fails validation
     */
    protected static ReversalRequest reversalRequest(String authorizationGuid, String paymentGuid, String refundGuid, String cusomterReference) throws InvalidMessageException {
        ReversalRequest reversalRequest = new ReversalRequest();
        reversalRequest.setAuthorizationGuid(authorizationGuid);
        reversalRequest.setPaymentGuid(paymentGuid);
        reversalRequest.setRefundGuid(refundGuid);
        reversalRequest.setCustomerReference(cusomterReference);
        if (isValidReversalRequest(reversalRequest)) {
            return reversalRequest;
        } else {
            throw new InvalidMessageException("Reversal request must include one of authorizationGuid, paymentGuid or refundGuid");
        }
    }

    /**
     * Create and validate E-Commerce cancellation message
     *
     * @param transactionType
     * @param currency
     * @param amount
     * @param terminalDateTime
     * @return
     * @throws InvalidMessageException if message fails validation
     */
    protected static CancellationRequest cancellationRequest(String transactionType, String currency, String amount, String terminalDateTime) throws InvalidMessageException {
        CancellationRequest cancellationRequest = new CancellationRequest();
        cancellationRequest.setAmount(amount);
        cancellationRequest.setCurrency(currency);
        cancellationRequest.setTransactionType(transactionType);
        cancellationRequest.setTerminalDateTime(terminalDateTime);
        Set<ConstraintViolation<CancellationRequest>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(cancellationRequest);

        if (!constraintViolations.isEmpty()) {
            throw new InvalidMessageException(getConstraintViolationMessage(constraintViolations));
        }
        return cancellationRequest;
    }

    /**
     * Create and validate E-Commerce token message
     *
     * @param cardNumber
     * @param expiryDateMMYY
     * @return
     * @throws InvalidMessageException if message fails validation
     */
    protected static TokenRequest tokenRequest(String cardNumber, String expiryDateMMYY) throws InvalidMessageException {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setCardNumber(cardNumber);
        tokenRequest.setExpiryDateMMYY(expiryDateMMYY);
        Set<ConstraintViolation<TokenRequest>> constraintViolations = Validation.buildDefaultValidatorFactory().getValidator().validate(tokenRequest);

        if (!constraintViolations.isEmpty()) {
            throw new InvalidMessageException(getConstraintViolationMessage(constraintViolations));
        }
        return tokenRequest;
    }

    /**
     * Validates if a reversal requests has all required values.
     *
     * @param message to validate
     * @return true if message is valid, otherwise false
     */
    protected static boolean isValidReversalRequest(ReversalRequest message) {
        return message.getAuthorizationGuid() != null || message.getPaymentGuid() != null || message.getRefundGuid() != null;
    }


    /**
     * Validates if a authorization requests has all required values.
     *
     * @param message to validate
     * @return true if message is valid, otherwise false
     */
    protected static boolean hasValidCardData(AuthorizationRequest message) {
        return (message.getCardNumber() != null && message.getExpiryDateMMYY() != null) || message.getToken() != null;
    }


    /**
     * Validates if a payment requests has all required values.
     *
     * @param message to validate
     * @return true if message is valid, otherwise false
     */
    protected static boolean hasValidCardData(PaymentRequest message) {
        return (message.getCardNumber() != null && message.getExpiryDateMMYY() != null) || message.getToken() != null || message.getAuthorizationGuid() != null;
    }


    /**
     * Validates if a refund requests has all required values.
     *
     * @param message to validate
     * @return true if message is valid, otherwise false
     */
    protected static boolean hasValidCardData(RefundRequest message) {
        return (message.getCardNumber() != null && message.getExpiryDateMMYY() != null) || message.getToken() != null || message.getPaymentGuid() != null;
    }

    /**
     * Method creates an error message from constraint violations.
     *
     * @param violations
     * @return string with violation message.
     */
    private static String getConstraintViolationMessage(Set violations) {
        StringBuilder builder = new StringBuilder();
        for (Object c : violations) {
            if (c instanceof ConstraintViolationImpl) {
                ConstraintViolation violation = (ConstraintViolation) c;
                builder.append(violation.getMessage());
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
