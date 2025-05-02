package andrea_freddi.eCellar_backend.controllers;

import andrea_freddi.eCellar_backend.entities.User;
import andrea_freddi.eCellar_backend.exception.BadRequestException;
import andrea_freddi.eCellar_backend.payloads.LoginDTO;
import andrea_freddi.eCellar_backend.payloads.LoginPayload;
import andrea_freddi.eCellar_backend.payloads.UserPayload;
import andrea_freddi.eCellar_backend.services.AuthService;
import andrea_freddi.eCellar_backend.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/*
 * This class is a Spring Boot REST controller that handles authentication-related requests
 * It provides endpoints for user login and registration
 * The class is annotated with @RestController, which indicates that it is a controller
 * and that the return values of its methods will be serialized to JSON and sent in the HTTP response
 */

@RestController
@RequestMapping("/auth") // this is the base URL for all endpoints in this controller
public class AuthController {
    // It injects the AuthService and UsersService classes to handle authentication and user management
    @Autowired
    private AuthService authService;
    @Autowired
    private UsersService usersService; // inietto il servizio di gestione utenti

    // this method handles the login request
    @PostMapping("/login")
    public LoginDTO login(@RequestBody LoginPayload body) {
        // It receives a LoginPayload object containing the user's email and password
        // and returns a LoginDTO object containing the generated JWT token
        return new LoginDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    // this method handles the registration request
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) // this status code indicates that a new resource has been created: 201
    public User save(@RequestBody @Validated UserPayload body, BindingResult validationResult) {
        // It receives a UserPayload object containing the user's details
        // and validates it using the @Validated annotation
        // If there are validation errors, it throws a BadRequestException with the error messages
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("There were errors in the payload! " + message);
        }
        return this.usersService.save(body);
    }

}


