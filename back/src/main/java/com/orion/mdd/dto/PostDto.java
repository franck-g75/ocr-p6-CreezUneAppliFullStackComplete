package com.orion.mdd.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
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
public class PostDto {

    private Long id;

    @NotBlank(message="Le titre d'un article doit être saisi.")
    @Size(max=30, message="La taille d'un titre d'article doit être comprise entre 1 et 30 caractères.")
    private String title;

    @NotBlank(message="Le contenu d'un article doit être saisi.")
    @Size(max=4000, message="La taille d'un article doit être comprise entre 1 et 4000 caractères.")
    private String content;

    @NotNull
    @DateTimeFormat()
    private Date created_at;

    @NotBlank
    @Size(min=2, max=20, message="La taille d'un nom d'utilisateur doit être comprise entre 2 et 20 caractères.")
    private String username;

    @Nullable
    @NumberFormat()
    private Long id_topic;

    @Nullable
    //@Size(max=50, message = "Le titre du theme doit avoir une taille comprise entre 0 et 50 caractères.")
    private String topic_title;
}
