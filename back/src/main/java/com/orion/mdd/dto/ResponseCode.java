package com.orion.mdd.dto;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public enum ResponseCode {

    //java/com/orion/security/password.java contains the invalid pasword message (need a const string)

    USER_CREATION_SUCCESS("OPERATION_REUSSIE", "Création de l'utilisateur réussie", HttpStatus.CREATED),
    USER_UPDATE_SUCCESS("OPERATION_REUSSIE", "Mise à jour de l'utilisateur réussie", HttpStatus.OK),
    USER_CONNEXION_SUCCESS("CONNEXION_REUSSIE", "Connexion de l'utilisateur réussie", HttpStatus.OK),
    OPERATION_SUCCESS("OPERATION_REUSSIE", "Opération réussie", HttpStatus.OK),
    DATA_FOUND("DONNEES_TROUVEES", "données trouvées", HttpStatus.OK);
    //INVALID_INPUT("SAISIE_INVALIDE", "Erreur de saisie : veuillez recommencer.", HttpStatus.BAD_REQUEST),
    //DATA_NOT_FOUND("DONNEE_NON_TROUVEE", "Donnée(s) non trouvée(s).", HttpStatus.NOT_FOUND);
    
    private final String code;

    private final String message;

    @NonNull
    private final HttpStatus httpStatus;

    ResponseCode(String code, String message, @NonNull HttpStatus httpStatus) {
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