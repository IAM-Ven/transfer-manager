package org.zinaliev.transfermanager.exception;

import lombok.Getter;

public class ApplicationException extends RuntimeException {

    @Getter
    protected final int code;

    @Getter
    protected final int codeEx;

    public ApplicationException(int code, StatusCode codeEx, String message) {
        super(message);
        this.code = code;
        this.codeEx = codeEx.getCode();
    }
}
