package com.snsrestapi.exception;

import com.snsrestapi.domain.dto.response.Response;
import com.snsrestapi.domain.dto.response.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> exceptionHandler(AppException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpsStatus())
                .body(Response.error("ERROR", errorResponse));
    }

}
