package com.orion.mdd.security.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * implementation of Spring userDetail class
 * UserDetailsImpl
 */
@Builder
@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {
  private static final long serialVersionUID = 1L;

  /**
   * id of user
   */
  private Long id;

  /**
   * username
   */
  private String username;

  /**
   * email
   */
  private String email;

  /**
   * pwd
   */
  @JsonIgnore
  private String password;  
  
  /**
   * authorities
   * 
   */
  public Collection<? extends GrantedAuthority> getAuthorities() {        
      return new HashSet<GrantedAuthority>();
  }

  /**
   * must override something there retrun true because no user management is done here (MVP)
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * must override something there retrun true because no user management is done here (MVP)
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * must override something there retrun true because no user management is done here (MVP)
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * must override something there retrun true because no user management is done here (MVP)
   */
  @Override
  public boolean isEnabled() {
    return true;
  }

  /**
   * equals some other object ?
   */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  } 
}
