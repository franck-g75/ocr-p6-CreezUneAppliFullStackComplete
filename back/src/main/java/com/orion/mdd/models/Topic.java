package com.orion.mdd.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


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
@ToString
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotNull
    @Size(max = 2500)
    private String content;

    @ManyToMany(mappedBy="topics",fetch=FetchType.LAZY)
    private Set<Users> Users;

    @OneToMany(mappedBy = "topic")
    private Set<Post> posts;

}

