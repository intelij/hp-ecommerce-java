package com.handpoint.ecommerce.core;

import com.handpoint.ecommerce.messages.ErrorMessage;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests for error messages
 */
public class ErrorMessageTest {

    private static JaxbMessageConverter messageConverter = new JaxbMessageConverter();

    @Test
    public void testMarshallErrorMessage() throws JAXBException {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setReason("Error reason");
        List<String> errorDetails = new ArrayList<>();
        errorDetails.add("detail1");
        errorDetails.add("detail2");

        errorMessage.setDetails(errorDetails);

        String errorMessageStr = messageConverter.getMessage(ErrorMessage.class, errorMessage);
        assertTrue(errorMessageStr.contains("<error>"));
        assertTrue(errorMessageStr.contains("<reason>Error reason</reason>"));
        assertTrue(errorMessageStr.contains("<details>"));
        assertTrue(errorMessageStr.contains("<detail>detail1</detail>"));
        assertTrue(errorMessageStr.contains("<detail>detail2</detail>"));
        assertTrue(errorMessageStr.contains("</details>"));
        assertTrue(errorMessageStr.contains("</error>"));

        ErrorMessage errorMessageConvertedFromString = messageConverter.convert(ErrorMessage.class, errorMessageStr);
        assertThat(errorMessageConvertedFromString.getReason(), is("Error reason"));
        assertThat(errorMessageConvertedFromString.getDetails().get(0), is("detail1"));
        assertThat(errorMessageConvertedFromString.getDetails().get(1), is("detail2"));

    }
}
