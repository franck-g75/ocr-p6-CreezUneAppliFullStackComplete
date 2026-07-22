package com.orion.mdd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.orion.mdd.dto.MyResponseDto;

/**
 * Management of all returned errors in the app
 * ErrorManagement
 */
public class ErrorManagement {

    /**
     * static function
     * @param e the exception raised
     * @return the ResponseEntity adapted to the raised error
     */
    public static ResponseEntity<MyResponseDto> responseError(Exception e){

        MyResponseDto myResponseDto = new MyResponseDto(ErrorCode.SERVER_ERROR.getCode(),ErrorCode.SERVER_ERROR.getMessage(), null);

        if (e instanceof CustomException) {
            switch (((CustomException)e).getErrorCode().getHttpStatus()){
                case HttpStatus.UNAUTHORIZED : 
                    myResponseDto = new MyResponseDto(
                        ((CustomException)e).getErrorCode().getCode(),
                        ((CustomException)e).getErrorCode().getMessage(),
                        null
                    );
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(myResponseDto);
                case HttpStatus.BAD_REQUEST : 
                    myResponseDto = new MyResponseDto(
                        ((CustomException)e).getErrorCode().getCode(),
                        ((CustomException)e).getErrorCode().getMessage(),
                        null
                    );
                    return ResponseEntity.badRequest().body(myResponseDto);
                case HttpStatus.NOT_FOUND :
                    return ResponseEntity.notFound().build();
                case HttpStatus.INTERNAL_SERVER_ERROR : 
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(myResponseDto);
                default :
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(myResponseDto);
            } 
        
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(myResponseDto);
        } 
    }
}
