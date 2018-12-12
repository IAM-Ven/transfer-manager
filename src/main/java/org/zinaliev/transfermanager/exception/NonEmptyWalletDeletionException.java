package org.zinaliev.transfermanager.exception;

import org.eclipse.jetty.http.HttpStatus;

public class NonEmptyWalletDeletionException extends ApplicationException {

    public NonEmptyWalletDeletionException(String message) {
        super(HttpStatus.BAD_REQUEST_400, StatusCode.NON_EMPTY_WALLET_DELETION, message);
    }
}
