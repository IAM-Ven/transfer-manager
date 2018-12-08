package org.zinaliev.transfermanager.exception;

public class ApplicationStartupException extends RuntimeException {

    public ApplicationStartupException(String message) {
        super(message);
    }

    public ApplicationStartupException(String message, Throwable cause) {
        super(message, cause);
    }

}
