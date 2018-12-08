package org.zinaliev.transfermanager.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.zinaliev.transfermanager.api.model.ResponseModel;
import org.zinaliev.transfermanager.exception.JsonReadException;
import org.zinaliev.transfermanager.util.JsonMapper;
import spark.Request;
import spark.Response;

@Slf4j
@Singleton
public class ExceptionHandler implements spark.ExceptionHandler<Exception> {

    private final JsonMapper jsonMapper;

    @Inject
    public ExceptionHandler(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override
    public void handle(Exception exc, Request request, Response response) {
        ResponseModel body = new ResponseModel();

        body.setMessage(exc.getMessage());

        if (exc instanceof IllegalArgumentException || exc instanceof JsonReadException) {
            response.status(HttpStatus.BAD_REQUEST_400);
            body.setCodeEx(HttpStatus.BAD_REQUEST_400);
        } else {
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            body.setCodeEx(HttpStatus.INTERNAL_SERVER_ERROR_500);
        }

        try {
            response.body(jsonMapper.toJson(body));
        } catch (Exception e) {
            log.warn("Failed to set response body from " + body, e);
        }
    }
}
