package org.zinaliev.transfermanager.exception;

import lombok.Getter;

public enum StatusCode {

    SUCCEED(200),
    BAD_REQUEST_DEFAULT(400),
    WALLET_ALREADY_EXISTS(40001),
    INVALID_WALLET_ID(40002),
    INVALID_CURRENCY(40003),
    INVALID_AMOUNT(40004),
    INSUFFICIENT_MONEY(40005),
    NOT_FOUND_DEFAULT(404);

    @Getter
    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

}
