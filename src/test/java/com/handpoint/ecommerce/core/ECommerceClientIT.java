package com.handpoint.ecommerce.core;

import com.handpoint.ecommerce.core.exceptions.HpECommerceException;
import com.handpoint.ecommerce.core.exceptions.HpServerError;
import com.handpoint.ecommerce.core.exceptions.InvalidMessageException;
import com.handpoint.ecommerce.messages.payment.*;
import com.handpoint.ecommerce.messages.token.Token;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author palmithor
 * @since 2013-03
 */
@Category(IntegrationTest.class)
public class ECommerceClientIT {

    // Test Environment
    public static final String SHARED_SECRET = "8F10C8AD35B7AEC11675B50DBF6ACEAA0B4EC280B92500E51A02F7BBBE7B07C6";
    public static final String CARD_ACCEPTOR = "7f6451e8314defbb50d0";

    public static final Environment ENVIRONMENT = Environment.TEST;

    // Note all test card numbers are retrieved from http://www.paypalobjects.com/en_US/vhelp/paypalmanager_help/credit_card_numbers.htm
    public static final String VISA_TEST_CARD = "4222222222222";
    public static final String MASTERCARD_TEST_CARD = "5105105105105100";
    public static final String AMERICAN_EXPRESS_TEST_CARD = "371449635398431";
    public static final String EXPIRY_DATE_DECEMBER_2015 = "1215";
    public static final String AMOUNT_70_APPROVE_AMOUNT = "70.00";
    public static final String AMOUNT_120_DECLINE_AMOUNT = "120.00";
    public static final String VISA_CARD_NAME = "VISA";
    public static final String AMERICAN_EXPRESS_CARD_NAME = "American Express";
    public static final String TEST_TOKEN_01 = "TEST_TOKEN_01";


    @Test
    public void testAuthorization() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);

        // Authorization VISA
        Authorization authorization = client.authorize(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        assertThat(authorization.getIssuerResponseText(), is("Yay ! Authorized by APDUMMY :-)"));
        assertThat(authorization.getAgreementNumber(), is("123456789"));
        assertThat(authorization.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(authorization.getCurrency(), is(Currency.ISK.alpha));
        assertThat(authorization.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(authorization.getExpiryDateMMYY());
        assertNotNull(authorization.getAuthorizationGuid());
        assertNotNull(authorization.getApprovalCode());

        // Authorization using MasterCard
        authorization = client.authorize(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, MASTERCARD_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        assertThat(authorization.getIssuerResponseText(), is("Yay ! Authorized by APDUMMY :-)"));
        assertThat(authorization.getAgreementNumber(), is("123456789"));
        assertThat(authorization.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(authorization.getCurrency(), is(Currency.ISK.alpha));
        assertThat(authorization.getCardTypeName(), is("MasterCard"));
        assertNotNull(authorization.getExpiryDateMMYY());
        assertNotNull(authorization.getAuthorizationGuid());
        assertNotNull(authorization.getApprovalCode());

        // Authorization using Amex
        authorization = client.authorize(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, AMERICAN_EXPRESS_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        assertThat(authorization.getIssuerResponseText(), is("Yay ! Authorized by APDUMMY :-)"));
        assertThat(authorization.getAgreementNumber(), is("123456789"));
        assertThat(authorization.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(authorization.getCurrency(), is(Currency.ISK.alpha));
        assertThat(authorization.getCardTypeName(), is("American Express"));
        assertNotNull(authorization.getExpiryDateMMYY());
        assertNotNull(authorization.getAuthorizationGuid());
        assertNotNull(authorization.getApprovalCode());

        // Authorization using Amex and CVC
        authorization = client.authorize(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, AMERICAN_EXPRESS_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        assertThat(authorization.getIssuerResponseText(), is("Yay ! Authorized by APDUMMY :-)"));
        assertThat(authorization.getAgreementNumber(), is("123456789"));
        assertThat(authorization.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(authorization.getCurrency(), is(Currency.ISK.alpha));
        assertThat(authorization.getCardTypeName(), is(AMERICAN_EXPRESS_CARD_NAME));
        assertNotNull(authorization.getExpiryDateMMYY());
        assertNotNull(authorization.getAuthorizationGuid());
        assertNotNull(authorization.getApprovalCode());


        // Authorization decline test
        authorization = client.authorize(Currency.ISK.alpha, AMOUNT_120_DECLINE_AMOUNT, AMERICAN_EXPRESS_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        assertThat(authorization.getIssuerResponseText(), is("Declined by APDUMMY :-("));
        assertThat(authorization.getReason(), is("Declined, see issuerReponseText"));
        assertThat(authorization.getAgreementNumber(), is("123456789"));
        assertThat(authorization.getAmount(), is(AMOUNT_120_DECLINE_AMOUNT));
        assertThat(authorization.getCurrency(), is(Currency.ISK.alpha));
        assertThat(authorization.getCardTypeName(), is(AMERICAN_EXPRESS_CARD_NAME));
        assertNotNull(authorization.getExpiryDateMMYY());
        assertNotNull(authorization.getAuthorizationGuid());
        assertThat(authorization.getApprovalCode(), is(""));
    }

    @Test
    public void testAuthorizationWithToken() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        // Authorization using token
        Authorization authorization = client.authorizeWithToken(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, getToken(client).getToken());
        assertThat(authorization.getIssuerResponseText(), is("Yay ! Authorized by APDUMMY :-)"));
        assertThat(authorization.getAgreementNumber(), is("123456789"));
        assertThat(authorization.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(authorization.getCurrency(), is(Currency.ISK.alpha));
        assertThat(authorization.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(authorization.getExpiryDateMMYY());
        assertNotNull(authorization.getAuthorizationGuid());
        assertNotNull(authorization.getApprovalCode());
    }

    @Test
    public void testCancelAuth() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        Authorization authorization = client.authorize(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        Cancellation cancellation = client.cancelAuthorization(authorization.getCurrency(), authorization.getAmount(), authorization.getTerminalDateTime());
        assertNotNull(cancellation.getCancellationGuid());
        assertNotNull(cancellation.getAuthorizationGuid());
        assertThat(cancellation.getExpiryDateMMYY(), is(EXPIRY_DATE_DECEMBER_2015));
        assertThat(cancellation.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(cancellation.getCurrency(), is(Currency.ISK.alpha));
        assertNotNull(cancellation.getApprovalCode());
        assertThat(cancellation.getCardTypeName(), is(VISA_CARD_NAME));
    }

    @Test
    public void testReversalAuthorization() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        Authorization authorization = client.authorize(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        Reversal reversal = client.reverseAuthorization(authorization.getAuthorizationGuid());
        assertNotNull(reversal.getReversalGuid());
        assertNotNull(reversal.getAuthorizationGuid());
        assertThat(reversal.getExpiryDateMMYY(), is(EXPIRY_DATE_DECEMBER_2015));
        assertThat(reversal.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(reversal.getCurrency(), is(Currency.ISK.alpha));
        assertNotNull(reversal.getApprovalCode());
        assertThat(reversal.getCardTypeName(), is(VISA_CARD_NAME));
    }


    @Test
    public void testCaptureAuth() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        Authorization authorization = client.authorize(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        Payment payment = client.captureAuthorization(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, authorization.getAuthorizationGuid());
        assertNotNull(payment.getPaymentGuid());
        assertThat(payment.getExpiryDateMMYY(), is(EXPIRY_DATE_DECEMBER_2015));
        assertThat(payment.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(payment.getCurrency(), is(Currency.ISK.alpha));
        assertNotNull(payment.getApprovalCode());
        assertThat(payment.getCardTypeName(), is(VISA_CARD_NAME));
    }

    @Test
    public void testSendPayment() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        Payment payment = client.payment(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        assertThat(payment.getIssuerResponseText(), is("Yay ! Authorized by APDUMMY :-)"));
        assertThat(payment.getAgreementNumber(), is("123456789"));
        assertThat(payment.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(payment.getCurrency(), is(Currency.ISK.alpha));
        assertThat(payment.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(payment.getExpiryDateMMYY());
        assertNotNull(payment.getPaymentGuid());
        assertNotNull(payment.getApprovalCode());
    }

    @Test
    public void refundPaymentWithGuid() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        Payment payment = client.payment(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        assertThat(payment.getIssuerResponseText(), is("Yay ! Authorized by APDUMMY :-)"));
        assertThat(payment.getAgreementNumber(), is("123456789"));
        assertThat(payment.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(payment.getCurrency(), is(Currency.ISK.alpha));
        assertThat(payment.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(payment.getExpiryDateMMYY());
        assertNotNull(payment.getPaymentGuid());
        assertNotNull(payment.getApprovalCode());
        Refund refund = client.refundPayment(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, payment.getPaymentGuid());
        assertThat(refund.getIssuerResponseText(), is("TPOS 0000-Success"));
        assertThat(refund.getAgreementNumber(), is("123456789"));
        assertThat(refund.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(refund.getCurrency(), is(Currency.ISK.alpha));
        assertThat(refund.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(refund.getExpiryDateMMYY());
        assertNotNull(refund.getPaymentGuid());
        assertNotNull(refund.getApprovalCode());
    }


    @Test
    public void testReversePayment() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        Payment payment = client.payment(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        assertThat(payment.getIssuerResponseText(), is("Yay ! Authorized by APDUMMY :-)"));
        assertThat(payment.getAgreementNumber(), is("123456789"));
        assertThat(payment.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(payment.getCurrency(), is(Currency.ISK.alpha));
        assertThat(payment.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(payment.getExpiryDateMMYY());
        assertNotNull(payment.getPaymentGuid());
        assertNotNull(payment.getApprovalCode());
        Reversal reversal = client.reversePayment(payment.getPaymentGuid());
        assertThat(reversal.getIssuerResponseText(), is("Voided by APDUMMY"));
        assertThat(reversal.getAgreementNumber(), is("123456789"));
        assertThat(reversal.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(reversal.getCurrency(), is(Currency.ISK.alpha));
        assertThat(reversal.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(reversal.getExpiryDateMMYY());
        assertNotNull(reversal.getPaymentGuid());
        assertNotNull(reversal.getApprovalCode());
    }

    @Test
    public void testSendPaymentWithToken() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        Token token = getToken(client);
        Payment payment = client.paymentWithToken(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, token.getToken());
        assertThat(payment.getIssuerResponseText(), is("Yay ! Authorized by APDUMMY :-)"));
        assertThat(payment.getAgreementNumber(), is("123456789"));
        assertThat(payment.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(payment.getCurrency(), is(Currency.ISK.alpha));
        assertThat(payment.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(payment.getExpiryDateMMYY());
        assertNotNull(payment.getPaymentGuid());
        assertNotNull(payment.getApprovalCode());
    }

    @Test
    public void testSendRefund() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        Refund refund = client.refund(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        assertThat(refund.getIssuerResponseText(), is("TPOS 0000-Success"));
        assertThat(refund.getAgreementNumber(), is("123456789"));
        assertThat(refund.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(refund.getCurrency(), is(Currency.ISK.alpha));
        assertThat(refund.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(refund.getExpiryDateMMYY());
        assertNotNull(refund.getRefundGuid());
        assertNotNull(refund.getApprovalCode());
    }

    @Test
    public void testSendRefundWithToken() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        Refund refund = client.refundWithToken(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, getToken(client).getToken());
        assertThat(refund.getIssuerResponseText(), is("TPOS 0000-Success"));
        assertThat(refund.getAgreementNumber(), is("123456789"));
        assertThat(refund.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(refund.getCurrency(), is(Currency.ISK.alpha));
        assertThat(refund.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(refund.getExpiryDateMMYY());
        assertNotNull(refund.getRefundGuid());
        assertNotNull(refund.getApprovalCode());

    }

    @Test
    public void testReverseRefund() throws HpECommerceException, HpServerError, InvalidMessageException {
        ECommerceClient client = new ECommerceClient(new ECommerceUser(CARD_ACCEPTOR, SHARED_SECRET, ENVIRONMENT), true);
        Refund refund = client.refund(Currency.ISK.alpha, AMOUNT_70_APPROVE_AMOUNT, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        assertThat(refund.getIssuerResponseText(), is("TPOS 0000-Success"));
        assertThat(refund.getAgreementNumber(), is("123456789"));
        assertThat(refund.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(refund.getCurrency(), is(Currency.ISK.alpha));
        assertThat(refund.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(refund.getExpiryDateMMYY());
        assertNotNull(refund.getRefundGuid());
        assertNotNull(refund.getApprovalCode());
        Reversal reversal = client.reverseRefund(refund.getRefundGuid());
        assertThat(reversal.getIssuerResponseText(), is("TPOS 0000-Success"));
        assertThat(reversal.getAgreementNumber(), is("123456789"));
        assertThat(reversal.getAmount(), is(AMOUNT_70_APPROVE_AMOUNT));
        assertThat(reversal.getCurrency(), is(Currency.ISK.alpha));
        assertThat(reversal.getCardTypeName(), is(VISA_CARD_NAME));
        assertNotNull(reversal.getExpiryDateMMYY());
        assertNotNull(reversal.getReversalGuid());
        assertNotNull(reversal.getRefundGuid());
        assertNotNull(reversal.getApprovalCode());
    }

    private Token getToken(ECommerceClient client) throws HpECommerceException, HpServerError, InvalidMessageException {
        Token token = null;
        try {
            token = client.getToken(TEST_TOKEN_01);
        } catch (HpServerError e) {
            token = client.createToken(TEST_TOKEN_01, VISA_TEST_CARD, EXPIRY_DATE_DECEMBER_2015);
        }
        return token;
    }
}
