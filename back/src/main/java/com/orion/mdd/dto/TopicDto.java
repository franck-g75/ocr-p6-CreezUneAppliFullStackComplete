package com.orion.mdd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    private Long id;

    @NotBlank(message="le titre du message ne doit pas être vide.")
    @Size(max = 45, message="le titre du message ne doit pas dépasser 45 caractères.")
    private String title;

    @NotBlank(message="le contenu du theme ne doit pas être vide.")
    @Size(max = 1000, message = "Le contenu du theme ne doit pas dépasser 1000 caractères.")
    private String content;

    //champ technique pour la descente vers le client
    @NotNull
    private Boolean read = false;
}