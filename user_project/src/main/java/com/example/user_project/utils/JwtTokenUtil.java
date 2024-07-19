package com.example.user_project.utils;

import com.example.user_project.serviceimpl.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

//this annotation allows to detect this class as spring component and be registered in spring application context
@Component
public class JwtTokenUtil {

  @Value("${jwt.expiration}")
  private int expiration;

  private static final String SECRET_KEY = "jkdfbhjrbguitbshfjkwuertuygfkjbgvuiowieffoniwoeigjnuithgiorejnglktywersfgfgjyfghv";


  public String generateToken(String userName)
  {

      Date now = new Date();

      Date expiryDate = new Date(now.getTime()+expiration);

      //this 'Jwts' class and its methods is part of 'Jjwts' library which is 'Java Jwt' this class have 'builder()' method
      //which returns instance of builder(Jwtbuilder) which helps in constructing jwt's.
      //All below methods are methods of this Jwt builder instance.you start building JWT by calling 'builder()' method
      //three methods below builder() methods sets claims for JWT claims is notjing but metadata abt JWT token
      return Jwts.builder()
                 .setSubject(userName)//sets the subject claim of Jwt.Representing 'username'
                 .setIssuedAt(now)//sets the time at which token was created
                 .setExpiration(expiryDate)//sets expiration time of token
                 .signWith(SignatureAlgorithm.HS256,SECRET_KEY)//signs the Jwt token with specified signature algorithn and
                                                            // 'SECRET_KEY' so JWT should not be tampered with

                 .compact();//compacts the JWT into its final string representation.this string representation is url safe
                            //and can be saved in db

  }


    public boolean validate(String token) {
        try {
 //'Jwts' is the class provided by 'Jjwt' library.'parserBuilder()' method returns 'ParserBuilder' instance it allows
 //you to build and configure JWT parser.'setSigningKey()' method configures the 'JwtParser' to use a specific secret
 //key to verify Jwt token's signaturehere,'SECRETKEY' must match to validate token."build" method finalizes the
 //configuration of JwtParserBuilder() and creates JwtParser instance that can parse Jwt's based on configuration set
 //'parseClaimsJws()'method parses provided 'token' string and verifies the signature according to configuration set
 //'parserClaimsJws' returns 'Jws<Claims>' object which contains claims abt JWT which is payload data of token
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            // Log success or any relevant information
            System.out.println("Token validation successful for subject: " + claimsJws.getBody().getSubject());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Log the error for debugging
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }


    //Claims represents the payload (claims) of the JWT, which typically include standard and custom claims like
    // subject, issuer, expiration, etc.
  //  These claims are accessed through methods like getSubject(), getIssuer(), getExpiration(), etc.,
    //  depending on what information was encoded into the JWT during its creation.

       public String getUsername(String token)
       {

       Claims claims = Jwts.parserBuilder()
                           .setSigningKey(SECRET_KEY)
                           .build()
                           .parseClaimsJws(token)
                           .getBody();

       return claims.getSubject();

       }






}
