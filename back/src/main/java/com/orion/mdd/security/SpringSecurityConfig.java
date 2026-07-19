package com.orion.mdd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration of spring security
 * SpringSecurityConfig
 */
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("entering the SecurityChainFilter");
        return http
            .csrf((csrf) -> csrf.ignoringRequestMatchers("/api/auth/login","/api/auth/register")) 	//site with no CSRF protection only on these endpoint
    	   	.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))				//token OAuth2 filter
            .authorizeHttpRequests((auth) -> auth

                .requestMatchers(HttpMethod.GET,  "/api/post/user","/api/post/**", "/api/topic/user", "/api/topic", "/api/post/comments/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/users/subscribe/topic/**", "/api/users/unsubscribe/topic/**", "/api/post", "/api/post/comment/**").authenticated()
                .requestMatchers(HttpMethod.PUT,  "/api/users").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/register").permitAll()

            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling((exceptions) -> exceptions											      // popups //found on gitHub https://github.com/spring-projects/spring-security-samples
				.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())			          // popups protection
				.accessDeniedHandler(new BearerTokenAccessDeniedHandler())
			)
            .build();

    }

}