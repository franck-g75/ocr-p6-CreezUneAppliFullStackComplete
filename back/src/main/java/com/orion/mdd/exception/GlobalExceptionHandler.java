package com.orion.mdd.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<MyErrorResponse> handleCustomException(CustomException ex, WebRequest request) {

        ErrorCode errorCode = ex.getErrorCode();
        MyErrorResponse errorResponse = new MyErrorResponse(errorCode.getCode(), errorCode.getMessage());

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

        MyErrorResponse errorResponse = new MyErrorResponse(ErrorCode.SERVER_ERROR.getCode(), ErrorCode.SERVER_ERROR.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(  @NonNull MethodArgumentNotValidException ex, 
                                                                    @NonNull HttpHeaders headers, 
                                                                    @NonNull HttpStatusCode status, 
                                                                    @NonNull WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> 

            errors.put(error.getField(), error.getDefaultMessage())
        
        );

        MyErrorResponse errorResponse = new MyErrorResponse(ErrorCode.INVALID_INPUT.getCode(), ErrorCode.INVALID_INPUT.getMessage());
        errorResponse.setData(errors);

        return new ResponseEntity<Object>(errorResponse, ErrorCode.INVALID_INPUT.getHttpStatus());

    }

}        