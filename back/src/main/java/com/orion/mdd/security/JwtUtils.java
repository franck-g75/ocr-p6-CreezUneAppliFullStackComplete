package com.orion.mdd.security;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.orion.mdd.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class JwtUtils {
    
  @Value("${orion.app.jwtSecret}")
  private String jwtSecret;

  @Value("${orion.app.jwtExpirationMs}")
  private int jwtExpirationMs;


    public String getJwtTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Remove "Bearer " prefix
        }
        return null; // Return null if no valid JWT token is found
    }







  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    String chaine= new String(convertStringToSecretKey(jwtSecret).getEncoded());
    log.info("decode string :" + Base64.getDecoder().decode(jwtSecret).toString() + "  -  " + convertStringToSecretKey(jwtSecret).getEncoded() + "  -  "  + chaine);
    log.info("encode string MyOrionSecret2026openClassRoomLesson : " + Base64.getEncoder().encodeToString("MyOrionSecret2026openClassRoomLesson".getBytes() ) );
    log.info("JwtUtils " + jwtSecret + " " + jwtExpirationMs + " " + userPrincipal.getId() + " " + userPrincipal.getUsername() + " " + userPrincipal.getEmail() + " " + userPrincipal.getPassword());

    try{
      String retour = Jwts.builder()
        .header().add("IssuedAt",new Date()).add("Expiration",new Date((new Date()).getTime() + jwtExpirationMs))
        .and().subject(userPrincipal.getUsername())
        .signWith(convertStringToSecretKey(jwtSecret),Jwts.SIG.HS256)
        .compact();
        log.info("retour " + retour);
    } catch (Exception e) {
      log.info("escption e : " + e.toString());
    }    

    return Jwts.builder()
      .header().add("IssuedAt",new Date()).add("Expiration",new Date((new Date()).getTime() + jwtExpirationMs))
      .and().subject(userPrincipal.getUsername().toString())
      .signWith(convertStringToSecretKey(jwtSecret),Jwts.SIG.HS256).compact();
        
  }

/*
  public String getUserIdFromJwtToken(String token) {
    //SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    return Jwts.parser().verifyWith(convertStringToSecretKey(jwtSecret)).build().parseSignedClaims(token).getPayload().getSubject();
  }
*/

  public String getUsernameFromToken(String token) {
    //SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    return Jwts.parser().verifyWith(convertStringToSecretKey(jwtSecret)).build().parseSignedClaims(token).getPayload().getSubject();
  }
  public boolean validateJwtToken(String authToken) {

    try{
      Jwts.parser().verifyWith(convertStringToSecretKey(jwtSecret)).build().parseSignedClaims(authToken).getPayload();
      return true;
    } catch (SecurityException e) {//deprecated
      log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;

  }
  
  public static SecretKey convertStringToSecretKey(String encodedKey) {
    byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length,"HmacSHA256");
    return originalKey;
  }

  public static String convertSecretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
    byte[] rawData = secretKey.getEncoded();
    String encodedKey = Base64.getEncoder().encodeToString(rawData);
    return encodedKey;
  }
}
