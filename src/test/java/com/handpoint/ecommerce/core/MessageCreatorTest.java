package com.handpoint.ecommerce.core;

import com.handpoint.ecommerce.messages.InvalidMessageException;
import com.handpoint.ecommerce.messages.payment.AuthorizationRequest;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: palmithor
 * Date: 3/1/13
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageCreatorTest {

    @Test
    public void testAuthorizationWithCardData() throws Exception {
        AuthorizationRequest authorizationRequest = MessageCreator.authorizationRequest("WEB", Currency.ISK, "100", null, "40000000000000002", "1212", null, null);
        assertEquals("WEB", authorizationRequest.getPaymentScenario());
        assertEquals(Currency.ISK, authorizationRequest.getCurrency());
        assertEquals("100", authorizationRequest.getAmount());
        assertEquals("40000000000000002", authorizationRequest.getCardNumber());
        assertEquals("1212", authorizationRequest.getExpiryDateMMYY());
        assertNull(authorizationRequest.getToken());
        assertNull(authorizationRequest.getCardVerificationCode());
        assertNull(authorizationRequest.getCustomerReference());
    }

    @Test
    public void testAuthorizationWithToken() throws Exception {
        AuthorizationRequest authorizationRequest = MessageCreator.authorizationRequest("WEB", Currency.ISK, "100", "token", null, null, null, null);
        assertEquals("WEB", authorizationRequest.getPaymentScenario());
        assertEquals(Currency.ISK, authorizationRequest.getCurrency());
        assertEquals("100", authorizationRequest.getAmount());
        assertEquals("token", authorizationRequest.getToken());
        assertNull(authorizationRequest.getCardVerificationCode());
        assertNull(authorizationRequest.getCustomerReference());
        assertNull(authorizationRequest.getCardNumber());
        assertNull(authorizationRequest.getExpiryDateMMYY());
    }

    @Test
    public void testAuthorizationWithTokenAndCardData() throws Exception {
        AuthorizationRequest authorizationRequest = MessageCreator.authorizationRequest("WEB", Currency.ISK, "100", "token", "40000000000000002", "1212", null, null);
        assertEquals("WEB", authorizationRequest.getPaymentScenario());
        assertEquals(Currency.ISK, authorizationRequest.getCurrency());
        assertEquals("100", authorizationRequest.getAmount());
        assertEquals("token", authorizationRequest.getToken());
        assertEquals("40000000000000002", authorizationRequest.getCardNumber());
        assertEquals("1212", authorizationRequest.getExpiryDateMMYY());
        assertNull(authorizationRequest.getCardVerificationCode());
        assertNull(authorizationRequest.getCustomerReference());
    }

    @Test
    public void testNeitherTokenOrCardData()  {
        try {
            AuthorizationRequest authorizationRequest = MessageCreator.authorizationRequest("WEB", Currency.ISK, "100", null, null, null, null, null);
        } catch(InvalidMessageException e) {
            assertTrue(e.getLocalizedMessage().equals("Invalid card data. Must include either token or card number and expiry date"));
        }
    }

    @Test
    public void testViolateConstraints() {
        try {
            AuthorizationRequest authorizationRequest = MessageCreator.authorizationRequest(null, null, null, null, null, null, null, null);
        } catch(InvalidMessageException e) {
            assertTrue(e.getLocalizedMessage().contains("paymentScenario is required"));
            assertTrue(e.getLocalizedMessage().contains("currency is required"));
            assertTrue(e.getLocalizedMessage().contains("amount is required"));
        }
    }

    @Test
    public void testCurrencyEnumToXmlMessage() throws InvalidMessageException, JAXBException {
        JaxbMessageConverter messageConverter = new JaxbMessageConverter();
        AuthorizationRequest authorizationRequest = MessageCreator.authorizationRequest("WEB", Currency.ISK, "100", "token", "40000000000000002", "1212", null, null);
        String xml = messageConverter.getMessage(AuthorizationRequest.class, authorizationRequest);
        assertTrue(xml.contains("<authorization>"));
        assertTrue(xml.contains("<paymentScenario>WEB</paymentScenario>"));
        assertTrue(xml.contains("<currency>ISK</currency>"));
        assertTrue(xml.contains("<amount>100</amount>"));
        assertTrue(xml.contains("<cardNumber>40000000000000002</cardNumber>"));
        assertTrue(xml.contains("<expiryDateMMYY>1212</expiryDateMMYY>"));
        assertTrue(xml.contains("</authorization>"));
    }
}
