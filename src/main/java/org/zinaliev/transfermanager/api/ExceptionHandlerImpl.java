package org.zinaliev.transfermanager.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.zinaliev.transfermanager.api.model.ResponseModel;
import org.zinaliev.transfermanager.exception.ApplicationException;
import org.zinaliev.transfermanager.exception.JsonReadException;
import org.zinaliev.transfermanager.exception.StatusCode;
import org.zinaliev.transfermanager.util.JsonSerializer;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

@Slf4j
@Singleton
public class ExceptionHandlerImpl implements ExceptionHandler<Exception> {

    private final JsonSerializer serializer;

    @Inject
    public ExceptionHandlerImpl(JsonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void handle(Exception e, Request request, Response response) {
        ResponseModel body = new ResponseModel();

        body.setMessage(e.getMessage());

        if (e instanceof IllegalArgumentException) {
            response.status(HttpStatus.BAD_REQUEST_400);
            body.setCodeEx(StatusCode.BAD_REQUEST_DEFAULT.getCode());
        } else if (e instanceof JsonReadException) {
            response.status(HttpStatus.BAD_REQUEST_400);
            body.setCodeEx(StatusCode.BAD_REQUEST_DEFAULT.getCode());
            body.setMessage("Invalid request body (JSON model mismatch)");
        } else if (e instanceof ApplicationException) {
            ApplicationException exc = (ApplicationException) e;
            response.status(exc.getCode());
            body.setCodeEx(exc.getCodeEx());
        } else {
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            body.setCodeEx(HttpStatus.INTERNAL_SERVER_ERROR_500);
            body.setMessage("Internal server error. See logs for details");
        }

        if (response.status() == HttpStatus.INTERNAL_SERVER_ERROR_500)
            log.warn("Unhandled exception intercepted", e);
        else
            log.info("Mapped {} -> code: {}, codeEx: {}, message: {}",
                    e.getClass().getSimpleName(),
                    response.status(),
                    body.getCodeEx(),
                    body.getMessage()
            );

        try {
            response.body(serializer.toJson(body));
        } catch (Exception exc) {
            log.error("Failed to set response body from " + body, exc);
        }
    }
}
