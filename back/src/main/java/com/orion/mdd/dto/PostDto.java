package com.orion.mdd.dto;

import java.util.Date;

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

    @NotBlank
    @Size(max = 15)
    private String title;

    @NotBlank
    @Size(max = 15)
    private String content;

    @NotNull
    private Date created_at;

    @NotBlank
    private String username;

    @Nullable
    private Long id_topic;
}
