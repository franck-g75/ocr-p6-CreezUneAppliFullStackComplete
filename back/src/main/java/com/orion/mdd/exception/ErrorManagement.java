package com.orion.mdd.exception;

import org.springframework.http.ResponseEntity;

public class ErrorManagement {

    public static ResponseEntity<?> responseError(Exception e){
        if (e instanceof RuntimeException){
            return ResponseEntity.internalServerError().build();
        } else if (e instanceof CustomException) {
            switch (((CustomException)e).getErrorCode()){
                case INVALID_INPUT : 
                    return ResponseEntity.badRequest().build();
                case DATA_NOT_FOUND :
                    return ResponseEntity.notFound().build();
                case SERVER_ERROR : 
                    return ResponseEntity.internalServerError().build();
                default :
                    return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
