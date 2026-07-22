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

/**
 * The data transfer objet for posts
 * used for Back End to front End and Front End to Back End
 * PostDto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    /**
     * identifier of the post (0 if new post)
     */
    private Long id;

    /**
     * post title
     */
    @NotBlank(message="Le titre d'un article doit être saisi.")
    @Size(max=30, message="La taille d'un titre d'article doit être comprise entre 1 et 30 caractères.")
    private String title;

    /**
     * post content
     */
    @NotBlank(message="Le contenu d'un article doit être saisi.")
    @Size(max=4000, message="La taille d'un article doit être comprise entre 1 et 4000 caractères.")
    private String content;

    /**
     * the date of creation of the post
     */
    @NotNull
    @DateTimeFormat()
    private Date created_at;

    /**
     * the author of the post
     */
    @NotBlank(message = "le username ne doit pas etre vide")
    @Size(min=2, max=20, message="La taille d'un nom d'utilisateur doit être comprise entre 2 et 20 caractères.")
    private String username;

    /**
     * topic id 
     */
    @Nullable
    @NumberFormat()
    private Long id_topic;

    /**
     * used to display the topic title in the detail page
     * it is nullable because this field is used only in detail page
     */
    @Nullable
    private String topic_title;
}
