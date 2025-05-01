package andrea_freddi.CAPSTONE_PROJECT.controllers;

import andrea_freddi.CAPSTONE_PROJECT.entities.CellarWine;
import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import andrea_freddi.CAPSTONE_PROJECT.payloads.CellarWineDTO;
import andrea_freddi.CAPSTONE_PROJECT.payloads.CellarWinePayload;
import andrea_freddi.CAPSTONE_PROJECT.services.CellarWinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

/*
 * This class is a Spring Boot REST controller that handles requests related to cellar wines.
 * It is currently empty and does not contain any request handling methods.
 */

/*
1. GET http://localhost:3001/cellarWines
2. POST http://localhost:3001/cellarWines (+ req.body) --> 201 // this is the endpoint to create a new cellarWine
3. GET http://localhost:3001/cellarWines/{cellarWineId}
4. PUT http://localhost:3001/cellarWines/{cellarWineId} (+ req.body)
5. DELETE http://localhost:3001/cellarWines/{cellarWineId} --> 204
*/

@RestController
@RequestMapping("/cellarWines")
public class CellarWinesController {
    @Autowired
    private CellarWinesService cellarWinesService; // Injecting the CellarWinesService to handle cellar wine-related operations

    // this method is used to get all the cellar wines from the database
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public Page<CellarWineDTO> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id") String sortBy) {
        // inserted the default values for page, size and sortBy
        return this.cellarWinesService.findAll(page, size, sortBy);
    }

    // this method is used to get a cellar wine by id from the database
    @GetMapping("/{cellarWineId}")
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public CellarWine findById(@PathVariable UUID cellarId) {
        return this.cellarWinesService.findById(cellarId);
    }

    // all the following endpoints are used to manage a cellar wine by the current authenticated user
    // this method is used to create a new cellar wine in the database
    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED) // this status code indicates that a new resource has been created: 201
    public CellarWineDTO save(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated CellarWinePayload body, BindingResult validationResult) {
        // It receives a CellarWinePayload object containing the cellar wine data and validates it using the @Validated annotation
        // If there are validation errors, it throws a BadRequestException with the error messages
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("There were errors in the payload! " + message);
        }
        return this.cellarWinesService.save(body, currentAuthenticatedUser);
    }

    // this method is used to get a cellar wine by id from the database by the current authenticated user
    @GetMapping("/me/{cellarWineId}")
    public CellarWineDTO findByIdAndUser(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID cellarWineId) {
        return this.cellarWinesService.findByIdAndUser(cellarWineId, currentAuthenticatedUser);
    }

    // this method is used to update a cellar wine in the database by the current authenticated user
    @PutMapping("/me/{cellarWineId}")
    public CellarWineDTO findByIdAndUserAndUpdate(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID cellarWineId, @RequestBody @Validated CellarWinePayload body, BindingResult validationResult) {
        // It receives a CellarWinePayload object containing the updated cellar wine data and validates it using the @Validated annotation
        // If there are validation errors, it throws a BadRequestException with the error messages
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("There were errors in the payload! " + message);
        }
        return this.cellarWinesService.findByIdAndUserAndUpdate(cellarWineId, body, currentAuthenticatedUser);
    }

    // this method is used to delete a cellar wine in the database by the current authenticated user
    @DeleteMapping("/me/{cellarWineId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // this status code indicates that the request was successful and the resource was deleted: 204
    // this status code indicates that the request was successful and there is no content to return: 204
    public void findByIdAndUserAndDelete(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID cellarWineId) {
        this.cellarWinesService.findByIdAndUserAndDelete(cellarWineId, currentAuthenticatedUser);
    }
}
