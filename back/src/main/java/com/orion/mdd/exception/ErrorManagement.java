package com.orion.mdd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

import com.orion.mdd.exception.MyErrorResponse;


/**
 * 
 * ErrorManagement
 */
public class ErrorManagement {

    public static ResponseEntity<MyErrorResponse> responseError(Exception e){

        MyErrorResponse myErrorResponse = new MyErrorResponse(ErrorCode.SERVER_ERROR.getCode(),ErrorCode.SERVER_ERROR.getMessage());
        //myResponse.setData(null);
        //myResponse.setCode(ErrorCode.SERVER_ERROR.getCode());
        //myResponse.setMessage(ErrorCode.SERVER_ERROR.getMessage());

        if (e instanceof CustomException) {
            switch (((CustomException)e).getErrorCode().getHttpStatus()){
                case HttpStatus.BAD_REQUEST : 
                    myErrorResponse = new MyErrorResponse(
                        ((CustomException)e).getErrorCode().getCode(),
                        ((CustomException)e).getErrorCode().getMessage()
                    );
                    return ResponseEntity.badRequest().body(myErrorResponse);
                case HttpStatus.NOT_FOUND :
                    return ResponseEntity.notFound().build();
                case HttpStatus.INTERNAL_SERVER_ERROR : 
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(myErrorResponse);
                default :
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(myErrorResponse);
            } 
        
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(myErrorResponse);
        } 
    }
}
