package org.zinaliev.transfermanager.exception;

import lombok.Getter;
import org.eclipse.jetty.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    @Getter
    protected final int code;

    @Getter
    protected final int codeEx;

    public ApplicationException(String message) {
        this(HttpStatus.INTERNAL_SERVER_ERROR_500, StatusCode.SERVER_ERROR_DEFAULT, message);
    }

    public ApplicationException(int code, StatusCode codeEx, String message) {
        super(message);
        this.code = code;
        this.codeEx = codeEx.getCode();
    }
}
