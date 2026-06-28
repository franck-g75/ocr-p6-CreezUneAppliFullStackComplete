package com.orion.mdd.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Table(name = "POST")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"comments"})
public class Post {//article

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy="post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Comment> comments;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name="topic_id", nullable=false)
    private Topic topic;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name="user_info_id", nullable=false)
    private UserInfo userInfo;

    @NotBlank(message="Le contenu d'un article doit être saisi.")
    @Size(max=4000, message="La taille d'un article doit être comprise entre 1 et 4000 caractères.")
    private String content;

    @NotBlank(message="Le titre d'un article doit être saisi.")
    @Size(max=50, message = "Le titre du theme doit avoir une taille comprise entre 1 et 50 caractères.")
    private String title;

    @NotNull
    @DateTimeFormat
    private Date created_at;
    
}
