package com.orion.mdd.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

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

    @NotNull
    @Size(max = 5000)
    private String content;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotNull
    @DateTimeFormat
    private Date created_at;
    
}
