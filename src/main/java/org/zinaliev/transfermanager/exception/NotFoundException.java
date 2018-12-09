package org.zinaliev.transfermanager.exception;

import org.eclipse.jetty.http.HttpStatus;

public class NotFoundException extends ApplicationException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND_404, StatusCode.NOT_FOUND_DEFAULT, message);
    }
}
