package andrea_freddi.CAPSTONE_PROJECT.tools;

// creo la classe JWT per gestire la creazione e la validazione dei token JWT

import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

// This class is responsible for creating and validating JWT tokens
// It uses the io.jsonwebtoken library to create and parse JWT tokens
// The class is annotated with @Component, which means it is a Spring component and can be injected into other classes

@Component
public class JWT {

    // @Value annotation is used to inject the value of the jwt.secret property from the application.properties file
    @Value("${jwt.secret}")
    private String secret;

    // This method creates a JWT token for the given user
    public String createToken(User user) {
        // The token is created using the Jwts.builder() method
        // The issuedAt and expiration dates are set to the current time and 24 hours from now, respectively
        // The subject of the token is set to the user's ID
        return String.valueOf(Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // Emission date of the token
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expiration date of the token (24 hours)
                .subject(String.valueOf(user.getId())) // ID of the user
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // Signing the token with the secret key
                .compact()); // Compacting the token to a string
    }

    // This method verifies the validity of the given token
    public void verifyToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes())) // verifico il token con la secret
                    .build().parse(token);
        } catch (Exception e) {
            // If the token is invalid, an UnauthorizedException is thrown
            throw new UnauthorizedException("Not valid token! Please login again!");
        }
    }

    // This method extracts the user ID from the given token
    public String getIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes())) // Token verification with the secret
                .build().parseSignedClaims(token) // Parsing the token
                .getPayload().getSubject(); // Extracting the subject (user ID) from the token
    }
}
