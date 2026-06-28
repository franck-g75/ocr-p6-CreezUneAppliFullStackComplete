package com.orion.mdd.dto;

import com.orion.mdd.security.Password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private Long id;

    @NotBlank(message="Le nom d'utilisateur ne doit pas être vide.")
    @Size(max = 20, min = 2, message="La taille du nom d'utilisateur doit être comprise entre 2 et 20 caractères.")
    private String username;

    @NotBlank(message="L'email ne doit pas être vide.")
    @Email(message= "L'email doit être corectement formatté.")
    @Size(max = 50, min = 6, message="La taille de l'email doit être comprise entre 6 et 50 caractères.")
    private String email;

    @NotBlank(message = "Le mot de passe ne doit pas être vide.")
    @Password(message="Le mot de passe doit contenir au moins 1 chiffre, 1 majuscule, 1 minuscule, 1 caractère spécial. et faire 8 caractères mini.")
    @Size(max = 50, min = 8, message="La taille du mot de passe doit être comprise entre 8 et 50 caractères.")
    private String pwd;

    //too hard to maintain and the minus character can't be in
    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+<>}{=])(?=\b+$).{8,}$", 
    //         message = "doit contenir au moins 1 chiffre, 1 majuscule, 1 minuscule, 1 caractère spécial. et faire 8 caractères mini")

}
