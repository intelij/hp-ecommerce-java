package com.handpoint.ecommerce.messages;

/**
 * Created with IntelliJ IDEA.
 * User: palmithor
 * Date: 3/1/13
 * Time: 10:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidMessageException extends Exception {

    public InvalidMessageException() {
        super();
    }

    public InvalidMessageException(String message) {
        super(message);
    }
}
