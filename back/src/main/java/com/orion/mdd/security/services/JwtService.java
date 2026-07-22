package com.orion.mdd.security.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;


/**
 * used to manage the bearer token
 */
@Service
@Log4j2
public class JwtService {
	
    /**
     * no arg constructor
     */
    public JwtService() {}
    
	@Autowired
	JwtEncoder jwtEncoder;
	
	@Autowired
	JwtDecoder jwtDecoder;
    
    /**
     * The time in milisseconds in witch the jtw expire
     */
    @Value("${orion.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * alalyse the request passed in parameter and extract the token from the autorization header without the prefix Bearer
     * @param request the request to analyse
     * @return a string containing the token or null if not found.
     */
    public String getJwtTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Remove "Bearer " prefix
        }
        return null; // Return null if no valid JWT token is found
    }

    /**
     * generate the token
     * @param authentication an Authentication of spring sécurity object is passed as argument to generate the token
     * @return the token encoded with private key
     */
    public String generateToken(Authentication authentication) {
	    Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                   .issuer("self")
                   .issuedAt(now)
                   .expiresAt(now.plus(jwtExpirationMs, ChronoUnit.MILLIS))
                   .subject(authentication.getName())
                   .build();
        log.info("Token generated at " + now.toString());
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    /**
     * getUsernameFromToken
     * @param token the token without Bearer name
     * @return the username in the token
     */
    public String getUsernameFromToken(String token) {
        log.info("getUsernameFromToken...");
        String retour =  this.jwtDecoder.decode(token).getSubject();
        log.info(retour);
        return retour;
    }
	 
    /**
     * getExpirationDateFromToken
     * @param token token to analyse
     * @return the Instant ExpiresAt
     */
    private Instant getExpirationDateFromToken(String token) {
        log.info("getExpirationDateFromToken...");
        Instant retour =  this.jwtDecoder.decode(token).getExpiresAt();
        return retour;
    }
	 
    /**
     * validateToken
     * @param token to analyse
     * @param userDetails user of the 
     * @return true if token is validated false else
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
	 
    /**
     * isTokenExpired
     * @param token token to analyse
     * @return true if token is expired else false 
     */
    private boolean isTokenExpired(String token) {
        final Instant expiration = getExpirationDateFromToken(token);
        return expiration.isBefore(Instant.now());
    }

}
    
    