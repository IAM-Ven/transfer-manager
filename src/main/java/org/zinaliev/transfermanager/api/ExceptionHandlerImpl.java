package org.zinaliev.transfermanager.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.zinaliev.transfermanager.api.model.ResponseModel;
import org.zinaliev.transfermanager.exception.ApplicationException;
import org.zinaliev.transfermanager.exception.JsonReadException;
import org.zinaliev.transfermanager.util.JsonMapper;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

@Slf4j
@Singleton
public class ExceptionHandlerImpl implements ExceptionHandler<Exception> {

    private final JsonMapper jsonMapper;

    @Inject
    public ExceptionHandlerImpl(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void handle(Exception e, Request request, Response response) {
        ResponseModel body = new ResponseModel();

        body.setMessage(e.getMessage());

        if (e instanceof IllegalArgumentException || e instanceof JsonReadException) {
            response.status(HttpStatus.BAD_REQUEST_400);
            body.setCodeEx(HttpStatus.BAD_REQUEST_400);
        } else if (e instanceof ApplicationException) {
            ApplicationException exc = (ApplicationException) e;
            response.status(exc.getCode());
            body.setCodeEx(exc.getCodeEx());
        } else {
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            body.setCodeEx(HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        if (response.status() == HttpStatus.INTERNAL_SERVER_ERROR_500)
            log.warn("Unhandled exception intercepted", e);
        else
            log.info("Mapped {} -> code {}, codeEx: {}", e.toString(), response.status(), body.getCodeEx());

        try {
            response.body(jsonMapper.toJson(body));
        } catch (Exception exc) {
            log.warn("Failed to set response body from " + body, exc);
        }
    }
}
