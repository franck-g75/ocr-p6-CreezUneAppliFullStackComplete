package com.orion.mdd.exception;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * enum ErrorCode
 * used to identify the CustomException
 */
public enum ErrorCode {

    /**
     * when all other code don't match
     */
    SERVER_ERROR("ERREUR_INTERNE_DE_SERVEUR", "Erreur interne de serveur : veuillez réessayer plus tard.", HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * when the input is invalid due to user mistake
     */
    INVALID_INPUT("SAISIE_INVALIDE", "Erreur de saisie : veuillez recommencer.", HttpStatus.BAD_REQUEST),
    /**
     * when the user make a mistake in his / her credentials
     */
    INVALID_CREDENTIALS("LOGIN_EMAIL_OU_MOTDEPASSE_INVALIDE", "Erreur de saisie dans les identifiants : veuillez recommencer.", HttpStatus.BAD_REQUEST),
    /**
     * when the username is invalid (for the user creation utility)
     */
    INVALID_USERNAME("NOM_UTILISATEUR_INVALIDE", "Erreur de saisie : nom d'utilisateur invalide, veuillez recommencer.", HttpStatus.BAD_REQUEST),
    /**
     * when the email is invalid (for user creation utility)
     */
    INVALID_EMAIL("EMAIL_UTILISATEUR_INVALIDE", "Erreur de saisie : email d'utilisateur invalide, veuillez recommencer.", HttpStatus.BAD_REQUEST),
    /**
     * when data is not found (various usage)
     */
    DATA_NOT_FOUND("DONNEE_NON_TROUVEE", "Donnée(s) non trouvée(s).", HttpStatus.NOT_FOUND),
    /**
     * when a user is not authorized to do something (for example user A try to change user B credentails)
     */
    NOT_AUTHORIZED("ACTION_NON_AUTORISEE", "Action non autorisée...", HttpStatus.UNAUTHORIZED);
    
    /**
     * the internal code of the Error Code object 
     */
    private final String code;

    /**
     * the detailed message of ErrorCode
     */
    private final String message;

    /**
     * status of the error returned to user
     */
    @NonNull
    private final HttpStatus httpStatus;

    /**
     * ErrorCode Constructor
     * @param code the internal code of the Enum Error Code  
     * @param message the detailed message of ErrorCode
     * @param httpStatus status of the error returned to user
     */
    ErrorCode(String code, String message, @NonNull HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * the internal code of the Enum Error Code 
     * @return the code string
     */
    public String getCode() {
        return code;
    }

    /**
     * getter for the message 
     * @return the message String
     */
    public String getMessage() {
        return message;
    }

    /**
     * getter for the http status
     * @return the HttpStatus object
     */
    @NonNull
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}