package andrea_freddi.CAPSTONE_PROJECT.controllers;

import andrea_freddi.CAPSTONE_PROJECT.elasticsearch.WineDocument;
import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.entities.Wine;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import andrea_freddi.CAPSTONE_PROJECT.payloads.WinePayload;
import andrea_freddi.CAPSTONE_PROJECT.services.WinesSearchService;
import andrea_freddi.CAPSTONE_PROJECT.services.WinesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * This class is a Spring Boot REST controller that handles requests related to wines.
 * It is mapped to the "/wines" URL path.
 */

@RestController
@RequestMapping("/wines")
@RequiredArgsConstructor
public class WinesController {

    @Autowired
    private WinesService winesService; // Injecting the WinesService to handle wine-related operations

    @Autowired
    private WinesSearchService winesSearchService; // Injecting the WinesSearchService to handle wine search operations

    // Endpoint to get a list of wines based on the provided query parameter
    @GetMapping("/search")
    public List<WineDocument> searchWines(@RequestParam String query) {
        return winesSearchService.search(query);
    }

    // this method is used to get all wines from the database
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public Page<Wine> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "id") String sortBy) {
        // inserted the default values for page, size and sortBy
        return this.winesService.findAll(page, size, sortBy);
    }

    // this method is used to get a wine by id from the database
    @GetMapping("/{wineId}")
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public Wine findById(@PathVariable UUID wineId) {
        return this.winesService.findById(wineId);
    }

    // this method is used to create a new wine in the database
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // this status code indicates that a new resource has been created: 201
    public Wine save(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated WinePayload body, BindingResult validationResult) {
        // It receives a WinePayload object containing the wine data and validates it using the @Validated annotation
        // If there are validation errors, it throws a BadRequestException with the error messages
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("There were errors in the payload! " + message);
        }
        return this.winesService.save(body, currentAuthenticatedUser);
    }
}
