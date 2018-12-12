package org.zinaliev.transfermanager.exception;

import org.eclipse.jetty.http.HttpStatus;

public class WalletException extends ApplicationException {

    public WalletException(StatusCode codeEx, String message) {
        super(HttpStatus.BAD_REQUEST_400, codeEx, message);
    }
}
