package org.zinaliev.transfermanager.exception;

import lombok.Getter;

public enum StatusCode {

    BAD_REQUEST_DEFAULT(400),
    BAD_REQUEST_WALLET_EXISTS(40001),
    NOT_FOUND_DEFAULT(404)
    ;


    @Getter
    private final int code;

    StatusCode(int code) {
        this.code = code;
    }
}
