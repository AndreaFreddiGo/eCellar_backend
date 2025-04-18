package andrea_freddi.CAPSTONE_PROJECT.services;

import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.UnauthorizedException;
import andrea_freddi.CAPSTONE_PROJECT.payloads.LoginPayload;
import andrea_freddi.CAPSTONE_PROJECT.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// This class is responsible for handling user authentication
// and generating JWT tokens for authenticated users

@Service
public class AuthService {
    // Injecting UsersService to find the user in the database
    @Autowired
    private UsersService usersService;
    // Injecting JWT to generate the token
    @Autowired
    private JWT jwt;
    // Injecting PasswordEncoder to compare passwords
    @Autowired
    private PasswordEncoder bcrypt;

    // This method checks the user's credentials and generates a JWT token if they are valid
    public String checkCredentialsAndGenerateToken(LoginPayload body) {
        // Searching for a user in the database with the provided email
        User found = this.usersService.findByEmail(body.email());
        // If the user exists, comparing the provided password with the one saved in the database
        // and generating the token if they match
        if (bcrypt.matches(body.password(), found.getPassword())) {
            String token = jwt.createToken(found);
            return token;
        } else {
            // If the password does not match, throwing an exception
            throw new UnauthorizedException("Wrong password!");
        }
    }
}
