package com.orion.mdd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * class MyRequestLoginDto is a class to login the user
 * str can contain a email or the username.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyRequestLoginDto {
    
    /**
     * str : email or username of the user to log in
     */
    @NotBlank(message="Le nom d'utilisateur ou le mail ne doit pas être vide.")
    @Size(max = 50, min = 2, message="La taille du nom d'utilisateur doit être comprise entre 2 et 20 caractères.")
    private String str;

    /**
     * pwd : password of the user to log in
     */
    @NotBlank(message = "Le mot de passe ne doit pas être vide.")
    @Size(max = 500, min = 8, message="La taille du mot de passe doit être comprise entre 8 et 50 caractères.")
    private String pwd;

}