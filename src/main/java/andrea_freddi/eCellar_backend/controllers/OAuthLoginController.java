package andrea_freddi.eCellar_backend.controllers;

import andrea_freddi.eCellar_backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * This class is a Spring Boot REST controller that handles OAuth login requests.
 * It is mapped to the "/login" URL path.
 * The class uses the AuthService to handle Google login.
 */

@RestController
@RequestMapping("/login")
public class OAuthLoginController {

    @Autowired
    private AuthService authService;

    @GetMapping("/success")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User principal) {
        return authService.handleGoogleLogin(principal);
    }
}

