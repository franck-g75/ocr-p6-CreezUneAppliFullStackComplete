package com.orion.mdd.dto;

import com.orion.mdd.security.Password;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {

    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Email
    @Size(max = 21, min=6)
    private String email;

    @NotBlank
    @Password
    @Size(max = 20, min=8)
    private String pwd;

    //too hard to maintain and the - character can't be in
    //@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+<>}{=])(?=\b+$).{8,}$", 
    //         message = "doit contenir au moins 1 chiffre, 1 majuscule, 1 minuscule, 1 caractère spécial. et faire 8 caractères mini")
}
