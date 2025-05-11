package andrea_freddi.eCellar_backend.controllers;

import andrea_freddi.eCellar_backend.entities.CustomOAuth2User;
import andrea_freddi.eCellar_backend.entities.User;
import andrea_freddi.eCellar_backend.tools.JWT;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/*
 * This class is a Spring Boot REST controller that handles OAuth login requests.
 * It is mapped to the "/login" URL path.
 * The class uses the AuthService to handle Google login.
 */

@RestController
@RequestMapping("/login")
public class OAuthLoginController {

    @Autowired
    private JWT jwt;

    @GetMapping("/success")
    public void loginSuccess(HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomOAuth2User))
            throw new RuntimeException("Authenticated user is not a CustomOAuth2User");

        CustomOAuth2User principal = (CustomOAuth2User) auth.getPrincipal();
        User user = principal.getUser();

        String token = jwt.createToken(user);
        response.sendRedirect("http://localhost:3000/oauth2/redirect?token=" + token);
    }
}
