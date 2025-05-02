package andrea_freddi.eCellar_backend.controllers;

import andrea_freddi.eCellar_backend.entities.PurchaseProposal;
import andrea_freddi.eCellar_backend.entities.User;
import andrea_freddi.eCellar_backend.exception.BadRequestException;
import andrea_freddi.eCellar_backend.payloads.PurchaseProposalDTO;
import andrea_freddi.eCellar_backend.payloads.PurchaseProposalPayload;
import andrea_freddi.eCellar_backend.services.PurchaseProposalsService;
import jakarta.validation.Valid;
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
 * This controller handles endpoints related to purchase proposals
 */

/*
1. GET http://localhost:3001/purchaseProposals
2. POST http://localhost:3001/purchaseProposals (+ req.body) --> 201 // this is the endpoint to create a new proposal
3. GET http://localhost:3001/purchaseProposals/{purchaseProposalId}
4. PUT http://localhost:3001/purchaseProposals/{purchaseProposalId} (+ req.body)
5. DELETE http://localhost:3001/purchaseProposals/{purchaseProposalId} --> 204
*/

@RestController
@RequestMapping("/purchaseProposals") // define the path for the controller
public class PurchaseProposalsController {
    @Autowired
    private PurchaseProposalsService purchaseProposalsService; // inject the PurchaseProposalsService to handle purchase proposal operations


    // This method returns all proposals with pagination
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public Page<PurchaseProposalDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "proposalDate") String sortBy) {
        // inserted the default values for page, size and sortBy
        return purchaseProposalsService.findAll(page, size, sortBy);
    }

    // this method is used to get a purchase proposal by id from the database
    @GetMapping("/{purchaseProposalId}")
    @PreAuthorize("hasAuthority('ADMIN')") // only users with an ADMIN role can access this endpoint
    public PurchaseProposal findById(@PathVariable UUID purchaseProposalId) {
        return this.purchaseProposalsService.findById(purchaseProposalId);
    }

    // all the following methods are used to manage purchase proposals made by the current user
    // This method returns all proposals made by the current user
    @GetMapping("/me")
    public List<PurchaseProposalDTO> findAllByUser(@AuthenticationPrincipal User currentUser) {
        return purchaseProposalsService.findAllByUser(currentUser);
    }

    // This method creates a new purchase proposal
    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED) // this status code is returned when a new resource is created: 201
    public PurchaseProposalDTO save(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody @Validated PurchaseProposalPayload body, BindingResult validationResult) {
        // It receives a PurchaseProposalPayload object containing the details of the proposal and validates it using the @Validated annotation
        // If there are validation errors, it throws a BadRequestException with the error messages
        if (validationResult.hasErrors()) {
            String message = validationResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("There were errors in the payload! " + message);
        }
        return this.purchaseProposalsService.save(body, currentAuthenticatedUser);
    }

    // this method is used to get a proposal by id from the database by the current authenticated user
    @GetMapping("/me/{purchaseProposalId}")
    public PurchaseProposalDTO findByIdAndUser(@PathVariable UUID purchaseProposalId,
                                               @AuthenticationPrincipal User currentUser) {
        return this.purchaseProposalsService.findByIdAndUser(purchaseProposalId, currentUser);
    }

    // this method is used to update a proposal in the database by the current authenticated user
    @PutMapping("/me/{purchaseProposalId}")
    public PurchaseProposalDTO findByIdAndUserAndUpdate(@PathVariable UUID purchaseProposalId,
                                                        @AuthenticationPrincipal User currentUser,
                                                        @RequestBody @Valid PurchaseProposalPayload body) {
        return this.purchaseProposalsService.findByIdAndUserAndUpdate(purchaseProposalId, body, currentUser);
    }


    // this method is used to delete a cellar wine in the database by the current authenticated user
    @DeleteMapping("/me/{purchaseProposalId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // this status code indicates that the request was successful and the resource was deleted: 204
    public void findByIdAndUserAndDelete(@PathVariable UUID purchaseProposalId,
                                         @AuthenticationPrincipal User currentUser) {
        this.purchaseProposalsService.findByIdAndUserAndDelete(purchaseProposalId, currentUser);
    }

    // This method accepts a proposal (only by the cellar wine owner)
    @PostMapping("/{purchaseProposalId}/accept")
    public PurchaseProposalDTO findByIdAndAndUserAccept(@PathVariable UUID purchaseProposalId,
                                                        @AuthenticationPrincipal User currentUser) {
        return purchaseProposalsService.findByIdAndAndUserAccept(purchaseProposalId, currentUser);
    }

    // This method rejects a proposal (only by the cellar wine owner)
    @PostMapping("/{purchaseProposalId}/reject")
    public PurchaseProposalDTO findByIdAndAndUserReject(@PathVariable UUID purchaseProposalId,
                                                        @AuthenticationPrincipal User currentUser) {
        return purchaseProposalsService.findByIdAndAndUserReject(purchaseProposalId, currentUser);
    }
}

