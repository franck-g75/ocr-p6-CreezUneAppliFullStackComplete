package com.orion.mdd.exception;

/**
 * class used to centralize all the exceptions
 * CustomException is extending RuntimeExceptions
 */
public class CustomException extends RuntimeException {

    /**
     * ErrorCode is an enum field
     */
    private final ErrorCode errorCode;

    /**
     * constructor
     * @param errorCode the enum error code
     */
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /**
     * getter of the enum error code
     * @return the enum error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

}      

