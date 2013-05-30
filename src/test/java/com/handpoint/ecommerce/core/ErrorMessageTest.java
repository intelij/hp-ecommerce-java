package com.handpoint.ecommerce.core;

import com.handpoint.ecommerce.messages.ErrorMessage;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palmithor
 * Date: 3/1/13
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
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

        System.out.println(messageConverter.getMessage(ErrorMessage.class, errorMessage));
    }
}
