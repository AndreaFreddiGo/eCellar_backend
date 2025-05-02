package andrea_freddi.eCellar_backend.controllers;

import andrea_freddi.eCellar_backend.entities.Cellar;
import andrea_freddi.eCellar_backend.entities.User;
import andrea_freddi.eCellar_backend.exception.BadRequestException;
import andrea_freddi.eCellar_backend.payloads.CellarDTO;
import andrea_freddi.eCellar_backend.payloads.CellarPayload;
import andrea_freddi.eCellar_backend.services.CellarsService;
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
 * This class is a Spring Boot REST controller that handles requests related to cellars.
 * It is currently empty and does not contain any request handling methods.
 */

/*
1. GET http://localhost:3001/cellars
2. POST http://localhost:3001/cellars (+ req.body) --> 201 // this is the endpoint to create a new cellar
3. GET http://localhost:3001/cellars/{cellarId}
4. PUT http://localhost:3001/cellars/{cellarId} (+ req.body)
5. DELETE http://localhost:3001/cellars/{cellarId} --> 204
*/

@RestController
@RequestMapping("/cellars")
public class CellarsController {
    @Autowired
    private CellarsService cellarsService; // Inject the CellarsService to handle business logic related to cellars

    // this method is used to get all the cellars from the database
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public Page<CellarDTO> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sortBy) {
        // inserted the default values for page, size and sortBy
        return this.cellarsService.findAll(page, size, sortBy);
    }

    // this method is used to get a cellar by id from the database
    @GetMapping("/{cellarId}")
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public Cellar findById(@PathVariable UUID cellarId) {
        return this.cellarsService.findById(cellarId);
    }

    // all the following endpoints are used to manage a cellar by the current authenticated user
    // this method is used to create a new cellar in the database
    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED) // this status code indicates that a new resource has been created: 201
    public CellarDTO save(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated CellarPayload body, BindingResult validationResult) {
        // It receives a CellarPayload object containing the cellar data and validates it using the @Validated annotation
        // If there are validation errors, it throws a BadRequestException with the error messages
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("There were errors in the payload! " + message);
        }
        return this.cellarsService.save(body, currentAuthenticatedUser);
    }

    // this method is used to get a cellar by id from the database by the current authenticated user
    @GetMapping("/me/{cellarId}")
    public CellarDTO findByIdAndUser(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID cellarId) {
        return this.cellarsService.findByIdAndUser(cellarId, currentAuthenticatedUser);
    }

    // this method is used to get a cellar by its name from the database by the current authenticated user
    @GetMapping("/me/name")
    public CellarDTO findByNameAndUser(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestParam String name) {
        return this.cellarsService.findByNameAndUser(name, currentAuthenticatedUser);
    }

    // this method is used to update a cellar in the database by the current authenticated user
    @PutMapping("/me/{cellarId}")
    public CellarDTO findByIdAndUserAndUpdate(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID cellarId, @RequestBody @Validated CellarPayload body, BindingResult validationResult) {
        // It receives a CellarPayload object containing the updated cellar data and validates it using the @Validated annotation
        // If there are validation errors, it throws a BadRequestException with the error messages
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("There were errors in the payload! " + message);
        }
        return this.cellarsService.findByIdAndUserAndUpdate(cellarId, body, currentAuthenticatedUser);
    }

    // this method is used to delete a cellar in the database by the current authenticated user
    @DeleteMapping("/me/{cellarId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // this status code indicates that the request was successful and there is no content to return: 204
    public void findByIdAndUserAndDelete(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID cellarId) {
        this.cellarsService.findByIdAndUserAndDelete(cellarId, currentAuthenticatedUser);
    }
}