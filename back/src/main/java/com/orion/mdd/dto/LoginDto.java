package com.orion.mdd.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    
    @NotBlank(message="Le nom d'utilisateur ou le mail ne doit pas être vide.")
    @Size(max = 50, min = 2, message="La taille du nom d'utilisateur doit être comprise entre 2 et 20 caractères.")
    private String str;
/*
    @NotBlank(message="L'email ne doit pas être vide.")
    @Email(message= "L'email doit être corectement formatté.")
    @Size(max = 50, min = 6, message="La taille de l'email doit être comprise entre 6 et 50 caractères.")
    private String str;
*/
    @NotBlank(message = "Le mot de passe ne doit pas être vide.")
    @Size(max = 500, min = 8, message="La taille du mot de passe doit être comprise entre 8 et 50 caractères.")
    private String pwd;

}