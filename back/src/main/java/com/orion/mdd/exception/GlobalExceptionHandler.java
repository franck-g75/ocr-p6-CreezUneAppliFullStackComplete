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

import com.orion.mdd.dto.MyResponseDto;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * Can handle every excption of the project
 * GlobalExceptionHandler
 */
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * handling of custom exception raised in the application
     * @param ex the custom exception
     * @param request the request
     * @return an adapted response entity
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<MyResponseDto> handleCustomException(CustomException ex, WebRequest request) {

        ErrorCode errorCode = ex.getErrorCode();
        MyResponseDto errorResponse = new MyResponseDto(errorCode.getCode(), errorCode.getMessage(), null);

        return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());

    }

    /**
     * handling global exception
     * @param ex the exception
     * @param request the request
     * @return an adapted response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyResponseDto> handleGlobalException(Exception ex, WebRequest request) {

        MyResponseDto errorResponse = new MyResponseDto(ErrorCode.SERVER_ERROR.getCode(), ErrorCode.SERVER_ERROR.getMessage(), null);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    /**
     * Argument not valid occurs when a user doesn't respect the validity of the form
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(  @NonNull MethodArgumentNotValidException ex, 
                                                                    @NonNull HttpHeaders headers, 
                                                                    @NonNull HttpStatusCode status, 
                                                                    @NonNull WebRequest request) {
        
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> 

            errors.put(error.getField(), error.getDefaultMessage())
        
        );

        MyResponseDto errorResponse = new MyResponseDto(ErrorCode.INVALID_INPUT.getCode(), ErrorCode.INVALID_INPUT.getMessage(), errors);

        log.error("MethodArgumentNotValid" + errors.toString());
        return new ResponseEntity<Object>(errorResponse, ErrorCode.INVALID_INPUT.getHttpStatus());

    }

}        