package com.orion.mdd.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * used to manage TOPICS (there is only the read functionnality at the moment (MVP))
 * Topic
 */
@Table(name = "TOPIC")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"userInfos","posts"})
public class Topic {
    
    /**
     * The identifier incremental id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * the title of the topic 
     */
    @NotBlank(message="le titre du message ne doit pas être vide.")
    @Size(max = 45, message="le titre du message ne doit pas dépasser 45 caractères.")
    private String title;

    /**
     * the content of the topic
     */
    @NotBlank(message="le contenu du theme ne doit pas être vide.")
    @Size(max = 1000, message = "Le contenu du theme ne doit pas dépasser 1000 caractères.")
    private String content;

    /**
     * the users subscribed to the topic
     * bi directionnal relationship with userinfo entity n - m
     */
    @ManyToMany(mappedBy="topics", fetch=FetchType.LAZY)
    private Set<UserInfo> userInfos;

    /**
     * The posts related to the topic
     * bi directionnal relationship with Post entity 1-n
     */
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts;

}

