package com.handpoint.ecommerce.core.exceptions;

/**
 * Exception which the client throws for all internal operation exceptions.
 *
 * @author palmithor
 * @since 12/12/12 10:14 AM
 */
public class HpECommerceException extends Exception {


    public HpECommerceException(Throwable e) {
        super(e);
    }

    public HpECommerceException(String message, Throwable e) {
        super(message, e);
    }
}
