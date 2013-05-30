package com.handpoint.ecommerce.core;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Generic class used for message conversion when using jaxb
 *
 * @author fridrik
 * @since 12/12/12 10:53 AM
 */
public class JaxbMessageConverter {
    protected <T> T convert(Class<T> messageType, String message) throws JAXBException {
        return convert(messageType, new StringReader(message));
    }

    protected  <T> T convert(Class<T> messageType, Reader reader) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(messageType);
        Unmarshaller u = jc.createUnmarshaller();
        return (T) u.unmarshal(reader);
    }

    protected <T> String getMessage(Class<T> messageType, Object object) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(messageType);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        m.marshal(object, writer);
        return writer.toString();
    }

    protected <T> String getMessageWithoutHeader(Class<T> messageType, Object object) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(messageType);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        m.marshal(object, writer);
        return writer.toString();
    }

}
