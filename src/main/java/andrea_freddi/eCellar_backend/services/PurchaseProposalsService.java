package andrea_freddi.eCellar_backend.services;

import andrea_freddi.eCellar_backend.entities.CellarWine;
import andrea_freddi.eCellar_backend.entities.ProposalStatus;
import andrea_freddi.eCellar_backend.entities.PurchaseProposal;
import andrea_freddi.eCellar_backend.entities.User;
import andrea_freddi.eCellar_backend.exception.BadRequestException;
import andrea_freddi.eCellar_backend.exception.NotFoundException;
import andrea_freddi.eCellar_backend.mappers.PurchaseProposalMapper;
import andrea_freddi.eCellar_backend.payloads.PurchaseProposalDTO;
import andrea_freddi.eCellar_backend.payloads.PurchaseProposalPayload;
import andrea_freddi.eCellar_backend.repositories.PurchaseProposalsRepository;
import andrea_freddi.eCellar_backend.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
 * This service handles creation, retrieval, update, and deletion
 * of purchase proposals submitted by users for public cellar wines.
 */

@Service
public class PurchaseProposalsService {

    // Injecting the PurchaseProposalsRepository to handle database operations
    @Autowired
    private PurchaseProposalsRepository purchaseProposalsRepository;

    // Injecting the CellarWinesService to handle cellar wine-related operations
    @Autowired
    private CellarWinesService cellarWinesService;

    // Injecting the PurchaseProposalMapper to convert between PurchaseProposal entities and DTOs
    @Autowired
    private PurchaseProposalMapper purchaseProposalMapper;

    // Injecting the SecurityUtils to handle security-related operations
    @Autowired
    private SecurityUtils securityUtils;

    // This method retrieves a proposal by its ID
    public PurchaseProposal findById(UUID purchaseProposalId) {
        return this.purchaseProposalsRepository.findById(purchaseProposalId)
                .orElseThrow(() -> new NotFoundException("Purchase proposal with ID " + purchaseProposalId + " not found"));
    }

    // This method retrieves a proposal by its ID and user
    public PurchaseProposalDTO findByIdAndUser(UUID purchaseProposalId, User user) {
        PurchaseProposal found = this.findById(purchaseProposalId);
        // Check if the purchase proposal belongs to the user or if the user is an admin
        securityUtils.checkOwnershipOrAdmin(user, found.getUser().getId());
        return purchaseProposalMapper.purchaseProposalDTO(found);
    }

    // This method finds all proposals with pagination and sorting
    public Page<PurchaseProposalDTO> findAll(int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.purchaseProposalsRepository.findAll(pageable).map(purchaseProposalMapper::purchaseProposalDTO);
    }

    // This method finds all proposals by user
    public List<PurchaseProposalDTO> findAllByUser(User user) {
        List<PurchaseProposal> purchaseProposal = this.purchaseProposalsRepository.findAllByUser(user);
        return purchaseProposal.stream()
                .map(purchaseProposalMapper::purchaseProposalDTO)
                .collect(Collectors.toList());
    }

    // This method saves a new proposal
    public PurchaseProposalDTO save(PurchaseProposalPayload body, User user) {
        CellarWine cellarWine = cellarWinesService.findById(body.cellarWineId());
        // Check if the cellar wine is public
        if (!cellarWine.isPublic()) {
            throw new BadRequestException("Cannot propose to buy a private cellar wine.");
        }
        // Check if it's an own cellar wine
        if (cellarWine.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Cannot propose to buy your own wine.");
        }
        // Create a new purchase proposal
        PurchaseProposal newProposal = new PurchaseProposal(
                body.proposingPrice(),
                body.message(),
                user,
                cellarWine
        );
        return purchaseProposalMapper.purchaseProposalDTO(purchaseProposalsRepository.save(newProposal));
    }

    // This method updates an existing proposal (only price/message can be changed if still pending)
    public PurchaseProposalDTO findByIdAndUserAndUpdate(UUID purchaseProposalId, PurchaseProposalPayload body, User user) {
        PurchaseProposal found = this.findById(purchaseProposalId);
        // Check if the purchase proposal belongs to the user or if the user is an admin
        securityUtils.checkOwnershipOrAdmin(user, found.getUser().getId());
        // Check if the proposal is still pending
        if (found.getStatus() != ProposalStatus.PENDING) {
            throw new BadRequestException("Only pending proposals can be updated.");
        }
        found.setProposingPrice(body.proposingPrice());
        found.setMessage(body.message());
        // Save the updated proposal
        return purchaseProposalMapper.purchaseProposalDTO(purchaseProposalsRepository.save(found));
    }

    // This method deletes a proposal by its ID (only the owner can delete it)
    public void findByIdAndUserAndDelete(UUID purchaseProposalId, User user) {
        // Check if the proposal exists
        PurchaseProposal found = this.findById(purchaseProposalId);
        // Check if the purchase proposal belongs to the user or if the user is an admin
        securityUtils.checkOwnershipOrAdmin(user, found.getUser().getId());
        // Delete the proposal
        this.purchaseProposalsRepository.delete(found);
    }

    // This method updates the status of a proposal to "accepted" (only by the cellarWine owner)
    public PurchaseProposalDTO findByIdAndAndUserAccept(UUID purchaseProposalId, User user) {
        // Check if the proposal exists
        PurchaseProposal found = this.findById(purchaseProposalId);
        // Check if the cellar wine belongs to the user or if the user is an admin
        securityUtils.checkOwnershipOrAdmin(user, found.getCellarWine().getUser().getId());

        found.setStatus(ProposalStatus.ACCEPTED);
        return purchaseProposalMapper.purchaseProposalDTO(purchaseProposalsRepository.save(found));
    }

    // This method updates the status of a proposal to "rejected" (only by the cellarWine owner)
    public PurchaseProposalDTO findByIdAndAndUserReject(UUID purchaseProposalId, User user) {
        // Check if the proposal exists
        PurchaseProposal found = this.findById(purchaseProposalId);
        // Check if the cellar wine belongs to the user or if the user is an admin
        securityUtils.checkOwnershipOrAdmin(user, found.getCellarWine().getUser().getId());

        found.setStatus(ProposalStatus.REJECTED);
        return purchaseProposalMapper.purchaseProposalDTO(purchaseProposalsRepository.save(found));
    }
}
