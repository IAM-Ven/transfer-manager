package org.zinaliev.transfermanager.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import static org.zinaliev.transfermanager.exception.StatusCode.SUCCEED;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> {

    @Getter
    @Setter
    private String message = "OK";

    @Getter
    @Setter
    private int codeEx = SUCCEED.getCode();

    @Getter
    @Setter
    private T data;

    public static <T> ResponseModel<T> ok(T data) {
        ResponseModel<T> result = new ResponseModel<>();
        result.setData(data);

        return result;
    }

    public static ResponseModel ok(){
        return ok(null);
    }
}
