package com.orion.mdd;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * main function class
 * every Bean of the application are declared inside
 * and public and private key also   
 * MddApplication
 */
@SpringBootApplication
public class MddApplication {

	/**
	 * public key
	 */
	@Value("${orion.jwt.public.key}")
	RSAPublicKey key;

	/**
	 * private key
	 */
	@Value("${orion.jwt.private.key}")
	RSAPrivateKey priv;

	/**
	 * the main function
	 * @param args given by the command line
	 */
	public static void main(String[] args) {
		SpringApplication.run(MddApplication.class, args);
	}

	/**
	 * Bean used to decode
	 * from https://github.com/spring-projects/spring-security-samples/blob/main/servlet/spring-boot/java/jwt/login/src/main/java/example/RestConfig.java
	 * @return 
	 */
	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(this.key).build();
	}

	/**
	 * Bean used to encode
	 * from https://github.com/spring-projects/spring-security-samples/blob/main/servlet/spring-boot/java/jwt/login/src/main/java/example/RestConfig.java
	 */ 
	@Bean
	JwtEncoder jwtEncoder() {
		JWK jwk = new RSAKey.Builder(this.key).privateKey(this.priv).build();
		JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
		return new NimbusJwtEncoder(jwks);
	}

	/**
	 * password encoder
	 * @return the encrypted password
	 */
	@Bean//https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	/**
	 * AuthenticationProvider Bean used by the spring security
	 * @param userDetailsService class used to connect a user (with implementation in UserDetailsServiceImpl.java )
	 * @param passwordEncoder  the encoder used to encode passwords (see above)
	 * @return a AnthenticationProvider bean 
	 */
	@Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

	/**
	 * the authentication manager Bean used by spring security
	 * @param authenticationProvider ???
	 * @return an AuthenticationManager object
	 */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

}
