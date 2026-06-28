package com.orion.mdd.dto;

import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @NotNull
    private Long id;

    @NotBlank(message="Le contenu du message ne doit pas être vide.")
    @Size(max = 2000, message="Le contenu du message ne doit pas dépasser 2000 caractères.")
    private String content;

    @NotBlank(message="Le nom d'utilisateur ne doit pas être vide.")
    @Size(max = 20, min=2, message="La taille du nom d'utilisateur doit être comprise entre 2 et 20 caractères.")
    private String username;

}
