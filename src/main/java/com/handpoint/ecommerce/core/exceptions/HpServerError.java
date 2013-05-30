package com.handpoint.ecommerce.core.exceptions;

import com.handpoint.ecommerce.messages.ErrorMessage;

/**
 *
 * @author palmithor
 * @since 12/12/12 10:14 AM
 */
public class HpServerError extends Exception {

    private ErrorMessage errorMessage;

    public HpServerError(ErrorMessage errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    public HpServerError(Throwable e) {
        super(e);
    }

    public HpServerError(String message, ErrorMessage errorMessage) {
        super(message);
        this.errorMessage = errorMessage;
    }

    public HpServerError(String message, Throwable e) {
        super(message, e);
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
