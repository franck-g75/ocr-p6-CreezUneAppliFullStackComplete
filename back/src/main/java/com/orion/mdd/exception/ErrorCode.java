package com.orion.mdd.exception;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;


public enum ErrorCode {

    SERVER_ERROR("E001", "erreur de serveur interne.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("E002", "Saisie invalide.", HttpStatus.BAD_REQUEST),
    DATA_NOT_FOUND("E003", "Donnée(s) non trouvée(s).", HttpStatus.NOT_FOUND);
    
    
    private final String code;

    private final String message;

    @NonNull
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, @NonNull HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @NonNull
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}