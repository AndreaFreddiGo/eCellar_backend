package andrea_freddi.eCellar_backend.controllers;

import andrea_freddi.eCellar_backend.entities.Address;
import andrea_freddi.eCellar_backend.entities.User;
import andrea_freddi.eCellar_backend.exception.BadRequestException;
import andrea_freddi.eCellar_backend.payloads.AddressDTO;
import andrea_freddi.eCellar_backend.payloads.AddressPayload;
import andrea_freddi.eCellar_backend.services.AddressesService;
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
 * This class is a Spring Boot REST controller that handles HTTP requests related to addresses.
 * It is mapped to the "/addresses" URL path.
 */

/*
1. GET http://localhost:3001/addresses
2. POST http://localhost:3001/addresses (+ req.body) --> 201 // this is the endpoint to create a new address
3. GET http://localhost:3001/addresses/{addressId}
4. PUT http://localhost:3001/addresses/{addressId} (+ req.body)
5. DELETE http://localhost:3001/addresses/{addressId} --> 204
*/

@RestController
@RequestMapping("/addresses")
public class AddressesController {
    @Autowired
    private AddressesService addressesService; // Inject the AddressesService to handle business logic related to addresses

    // this method is used to get all the addresses from the database
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public Page<AddressDTO> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        // inserted the default values for page, size and sortBy
        return this.addressesService.findAll(page, size, sortBy);
    }

    // this method is used to get an address by id from the database
    @GetMapping("/{addressId}")
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public Address findById(@PathVariable UUID addressId) {
        return this.addressesService.findById(addressId);
    }

    // all the following endpoints are used to manage an address by the current authenticated user
    // this method is used to create a new address in the database
    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED) // this status code indicates that a new resource has been created: 201
    public AddressDTO save(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated AddressPayload body, BindingResult validationResult) {
        // It receives an AddressPayload object containing the address data and validates it using the @Validated annotation
        // If there are validation errors, it throws a BadRequestException with the error messages
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("There were errors in the payload! " + message);
        }
        return this.addressesService.save(body, currentAuthenticatedUser);
    }

    // this method is used to get an address by id from the database by the current authenticated user
    @GetMapping("/me/{addressId}")
    public AddressDTO findByIdAndUser(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID addressId) {
        return this.addressesService.findByIdAndUser(addressId, currentAuthenticatedUser);
    }

    // this method is used to get an address by its label from the database by the current authenticated user
    @GetMapping("/me/label")
    public AddressDTO findByLabelAndUser(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestParam String label) {
        return this.addressesService.findByLabelAndUser(label, currentAuthenticatedUser);
    }

    // this method is used to update an address in the database by the current authenticated user
    @PutMapping("/me/{addressId}")
    public AddressDTO findByIdAndUserAndUpdate(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID addressId, @RequestBody @Validated AddressPayload body, BindingResult validationResult) {
        // It receives an AddressPayload object containing the updated address data and validates it using the @Validated annotation
        // If there are validation errors, it throws a BadRequestException with the error messages
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("There were errors in the payload! " + message);
        }
        return this.addressesService.findByIdAndUserAndUpdate(addressId, body, currentAuthenticatedUser);
    }

    // this method is used to delete an address in the database by the current authenticated user
    @DeleteMapping("/me/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // this status code indicates that the request was successful and there is no content to return: 204
    public void findByIdAndUserAndDelete(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID addressId) {
        this.addressesService.findByIdAndUserAndDelete(addressId, currentAuthenticatedUser);
    }
}
