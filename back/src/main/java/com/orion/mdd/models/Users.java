package com.orion.mdd.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

import org.hibernate.annotations.ManyToAny;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Table(name = "USERS")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(max = 50)
    private String pwd;

    @ManyToMany(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinTable(name="TOPIC_USERS",
    joinColumns = {
        @JoinColumn(name="users_id", referencedColumnName = "id")
    },inverseJoinColumns = {
        @JoinColumn(name="topic_id", referencedColumnName = "id")
    } )
    private Set<Topic> topics;

    @OneToMany(mappedBy = "users")
    private Set<Post> posts;

    @OneToMany(mappedBy = "users")
    private Set<Comment> comments;


}
