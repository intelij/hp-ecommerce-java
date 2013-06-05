package com.handpoint.ecommerce.core.exceptions;

/**
 * Exception used when message fails validation
 *
 * @author palmithor
 * @since 3/1/13 10:43 AM
 */
public class InvalidMessageException extends Exception {

    public InvalidMessageException() {
        super();
    }

    public InvalidMessageException(String message) {
        super(message);
    }
}
