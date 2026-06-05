package com.orion.mdd.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post {//article

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy="id")
    private Set<Comment> comments;

    @ManyToOne
    @JoinColumn(name="topic_id", nullable=false)
    private Topic topic;

    @ManyToOne
    @JoinColumn(name="users_id", nullable=false)
    private Users users;

    @NotNull
    @Size(max = 5000)
    private String content;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotNull
    private Date created_at;
    
}
