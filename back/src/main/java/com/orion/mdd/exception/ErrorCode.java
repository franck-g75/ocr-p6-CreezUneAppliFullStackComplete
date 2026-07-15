package com.orion.mdd.exception;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public enum ErrorCode {

    //java/com/orion/security/password.java contains the invalid pasword message (need a const string)

    SERVER_ERROR("ERREUR_INTERNE_DE_SERVEUR", "Erreur interne de serveur : veuillez réessayer plus tard.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("SAISIE_INVALIDE", "Erreur de saisie : veuillez recommencer.", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("LOGIN_EMAIL_OU_MOTDEPASSE_INVALIDE", "Erreur de saisie dans les identifiants : veuillez recommencer.", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME("NOM_UTILISATEUR_INVALIDE", "Erreur de saisie : nom d'utilisateur invalide, veuillez recommencer.", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL("EMAIL_UTILISATEUR_INVALIDE", "Erreur de saisie : email d'utilisateur invalide, veuillez recommencer.", HttpStatus.BAD_REQUEST),
    DATA_NOT_FOUND("DONNEE_NON_TROUVEE", "Donnée(s) non trouvée(s).", HttpStatus.NOT_FOUND),
    NOT_AUTHORIZED("ACTION_NON_AUTORISEE", "Action non autorisée...", HttpStatus.UNAUTHORIZED);
    
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