package com.orion.mdd.dto;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * enum 
 * ResponseCode
 */
public enum ResponseCode {

    //java/com/orion/security/password.java contains the invalid pasword message (need a const string)

    /**
     * when successfull user creation happen
     */
    USER_CREATION_SUCCESS("OPERATION_REUSSIE", "Création de l'utilisateur réussie", HttpStatus.CREATED),
    /**
     * when successfull user update happen
     */
    USER_UPDATE_SUCCESS("OPERATION_REUSSIE", "Mise à jour de l'utilisateur réussie", HttpStatus.OK),
    /**
     * when successfull user connection occurs
     */
    USER_CONNEXION_SUCCESS("CONNEXION_REUSSIE", "Connexion de l'utilisateur réussie", HttpStatus.OK),
    /**
     * when a diverse successfull operation occurs
     */
    OPERATION_SUCCESS("OPERATION_REUSSIE", "Opération réussie", HttpStatus.OK),
    /**
     * when data is found (for a list or an object)
     */
    DATA_FOUND("DONNEES_TROUVEES", "données trouvées", HttpStatus.OK);
    //INVALID_INPUT("SAISIE_INVALIDE", "Erreur de saisie : veuillez recommencer.", HttpStatus.BAD_REQUEST),
    //DATA_NOT_FOUND("DONNEE_NON_TROUVEE", "Donnée(s) non trouvée(s).", HttpStatus.NOT_FOUND);
    
    /**
     * a code string  for the response message
     */
    private final String code;

    /**
     * the detailed message given to the user
     */
    private final String message;

    /**
     * the httpStatus (in 200 or 201 or 20x ....)
     */
    @NonNull
    private final HttpStatus httpStatus;

    /**
     * constructor
     * @param code  a code string  for the response message
     * @param message the detailed message given to the user
     * @param httpStatus the httpStatus (in 200 or 201 or 20x ....)
     */
    ResponseCode(String code, String message, @NonNull HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * getter for the code field
     * @return _
     */
    public String getCode() {
        return code;
    }

    /**
     * getter for the message field
     * @return _
     */
    public String getMessage() {
        return message;
    }

    /**
     * getter for the httpStatus field
     * @return _
     */
    @NonNull
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}