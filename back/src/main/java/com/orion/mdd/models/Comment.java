package com.orion.mdd.models;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Table(name = "COMMENT")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name="user_info_id", nullable=false)
    @Size(max = 20)
    private UserInfo userInfo;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name="post_id", nullable=false )
    private Post post;

    @NotBlank(message="Le contenu du message ne doit pas être vide.")
    @Size(max = 2000, message="Le contenu du message ne doit pas dépasser 2000 caractères.")
    private String content;

    //champs technique pour la descente vers le client.
    @NotNull
    @DateTimeFormat
    private Date created_at;
}
