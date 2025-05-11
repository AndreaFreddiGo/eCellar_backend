package andrea_freddi.eCellar_backend.services;

import andrea_freddi.eCellar_backend.entities.CustomOAuth2User;
import andrea_freddi.eCellar_backend.entities.User;
import andrea_freddi.eCellar_backend.exception.UnauthorizedException;
import andrea_freddi.eCellar_backend.payloads.LoginPayload;
import andrea_freddi.eCellar_backend.tools.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    // This method handles Google login
    public String handleGoogleLogin(OAuth2User principal) {
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        String picture = principal.getAttribute("picture");

        User user = usersService.findOrCreateGoogleUser(email, name, picture);
        return jwt.createToken(user);
    }

    // This method handles Google login with a custom OAuth2User
    public String handleGoogleLogin(CustomOAuth2User customUser) {
        return jwt.createToken(customUser.getUser());
    }
}
