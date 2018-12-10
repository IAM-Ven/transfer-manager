package org.zinaliev.transfermanager.exception;

import org.eclipse.jetty.http.HttpStatus;

public class UnsupportedCurrencyException extends ApplicationException {

    public UnsupportedCurrencyException(String message) {
        super(HttpStatus.BAD_REQUEST_400, StatusCode.INVALID_CURRENCY, message);
    }
}
