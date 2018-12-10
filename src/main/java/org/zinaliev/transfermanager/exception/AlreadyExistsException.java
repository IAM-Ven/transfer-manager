package org.zinaliev.transfermanager.exception;

import org.eclipse.jetty.http.HttpStatus;

public class AlreadyExistsException extends ApplicationException {

    public AlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST_400, StatusCode.WALLET_ALREADY_EXISTS, message);
    }
}
