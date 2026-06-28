package com.orion.mdd.models;

import lombok.*;

import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.orion.mdd.security.Password;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Table(name = "USER_INFO")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"posts","comments","topics"})
public class UserInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message="Le nom d'utilisateur ne doit pas être vide.")
    @Size(max = 20, min=2, message="La taille du nom d'utilisateur doit être comprise entre 2 et 20 caractères.")
    private String username;

    @NotBlank(message="L'email ne doit pas être vide.")
    @Email(message= "L'email doit être corectement formatté.")
    @Size(max = 50, min=6, message="La taille de l'email doit être comprise entre 6 et 50 caractères.")
    private String email;

    @NotBlank(message = "Le mot de passe ne doit pas être vide.")
    @Size(max = 50, min=8, message="La taille du mot de passe doit être comprise entre 8 et 50 caractères.")
    @Password(message="Le mot de passe doit contenir au moins 1 chiffre, 1 majuscule, 1 minuscule, 1 caractère spécial. et faire 8 caractères mini.")
    private String pwd;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="TOPIC_USER",
    joinColumns = {
        @JoinColumn(name="user_info_id", referencedColumnName = "id")
    },inverseJoinColumns = {
        @JoinColumn(name="topic_id", referencedColumnName = "id")
    } )
    private Set<Topic> topics;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts;

    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

}
