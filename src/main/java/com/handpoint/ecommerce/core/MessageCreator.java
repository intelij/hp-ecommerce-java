package com.handpoint.ecommerce.core;

import com.handpoint.ecommerce.messages.InvalidMessageException;
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

    protected static AuthorizationRequest authorizationRequest(String paymentScenario, Currency currency, String amount,
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

    protected static PaymentRequest paymentRequest(String paymentScenario, Currency currency, String amount,
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


    protected static RefundRequest refundRequest(String paymentScenario, Currency currency, String amount,
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

    protected static CancellationRequest cancellationRequest(String transactionType, Currency currency, String amount, String terminalDateTime) throws InvalidMessageException {
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

    protected static boolean isValidReversalRequest(ReversalRequest message) {
        return message.getAuthorizationGuid() != null || message.getPaymentGuid() != null || message.getRefundGuid() != null;
    }


    protected static boolean hasValidCardData(AuthorizationRequest message) {
        return (message.getCardNumber() != null && message.getExpiryDateMMYY() != null) || message.getToken() != null;
    }

    protected static boolean hasValidCardData(PaymentRequest message) {
        return (message.getCardNumber() != null && message.getExpiryDateMMYY() != null) || message.getToken() != null || message.getAuthorizationGuid() != null;
    }

    protected static boolean hasValidCardData(RefundRequest message) {
        return (message.getCardNumber() != null && message.getExpiryDateMMYY() != null) || message.getToken() != null || message.getPaymentGuid() != null;
    }

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
