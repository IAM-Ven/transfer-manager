package org.zinaliev.transfermanager.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ResponseModel<T> {

    @Getter
    @Setter
    private String message = "OK";

    @Getter
    @Setter
    private int codeEx = 200;

    @Getter
    @Setter
    private T data;

    public static <T> ResponseModel<T> ok(T data) {
        ResponseModel<T> result = new ResponseModel<>();
        result.setData(data);

        return result;
    }
}
