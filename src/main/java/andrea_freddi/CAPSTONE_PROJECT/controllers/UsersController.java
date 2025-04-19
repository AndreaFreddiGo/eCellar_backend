package andrea_freddi.CAPSTONE_PROJECT.controllers;

import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import andrea_freddi.CAPSTONE_PROJECT.payloads.UserUpdatePayload;
import andrea_freddi.CAPSTONE_PROJECT.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/*
 * UsersController is a REST controller that handles requests related to users
 * It uses the UsersService to perform operations on user data
 * This class is annotated with @RestController, which indicates that it is a controller
 */

/*
1. GET http://localhost:3001/users
2. POST http://localhost:3001/users (+ req.body) --> 201 // this is the endpoint to create a new user
3. GET http://localhost:3001/users/{userId}
4. PUT http://localhost:3001/users/{userId} (+ req.body)
5. DELETE http://localhost:3001/users/{userId} --> 204
*/

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;

    // this method is used to get all users from the database
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public Page<User> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String sortBy) {
        // inserted the default values for page, size and sortBy
        return this.usersService.findAll(page, size, sortBy);
    }

    // this method is used to get a user by id from the database
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public User findById(@PathVariable UUID userId) {
        return this.usersService.findById(userId);
    }

    // this method is used to update a user by id in the database
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public User findByIdAndUpdate(@PathVariable UUID userId, @RequestBody @Validated UserUpdatePayload body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            validationResult.getAllErrors().forEach(System.out::println);
            throw new BadRequestException("Invalid request body!");
        }
        return this.usersService.findByIdAndUpdate(userId, body);
    }

    // this method is used to delete a user by id from the database
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void findByIdAndDelete(@PathVariable UUID userId) {
        this.usersService.findByIdAndDelete(userId);
    }

    // all the following endpoints are used to manage the current authenticated user
    // this method is used to get the current authenticated user from the database
    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    // this method is used to update the current authenticated user in the database
    @PutMapping("/me")
    public User updateProfile(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated UserUpdatePayload body) {
        return this.usersService.findByIdAndUpdate(currentAuthenticatedUser.getId(), body);
    }

    // this method is used to delete the current authenticated user from the database
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        this.usersService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }

}
