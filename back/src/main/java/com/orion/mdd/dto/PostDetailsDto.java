package com.orion.mdd.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailsDto {

    @NotNull
    private Long post_id;

    @NotBlank
    @Size(max = 50)
    private String post_title;

    @NotBlank
    @Size(max = 5000)
    private String post_content;

    @NotNull
    @DateTimeFormat
    private Date post_created_at;

    @NotBlank
    @Size(max=20)
    private String post_username;

    @NotBlank
    @Size(max=45)
    private String topic;

    @NotNull
    @Size(max=1500)
    private String comment;

    @NotNull
    @Size(max=20)
    private String comment_username;

    @NotNull
    private Long comment_id;

}
