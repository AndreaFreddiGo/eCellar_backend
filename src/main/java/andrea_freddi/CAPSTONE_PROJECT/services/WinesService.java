package andrea_freddi.CAPSTONE_PROJECT.services;

import andrea_freddi.CAPSTONE_PROJECT.elasticsearch.WineDocument;
import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.entities.Wine;
import andrea_freddi.CAPSTONE_PROJECT.entities.WineStatus;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import andrea_freddi.CAPSTONE_PROJECT.payloads.WineDTO;
import andrea_freddi.CAPSTONE_PROJECT.payloads.WinePayload;
import andrea_freddi.CAPSTONE_PROJECT.repositories.WinesRepository;
import andrea_freddi.CAPSTONE_PROJECT.repositories.WinesSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// This class is a service that handles the business logic for the Wines entity
// It is annotated with @Service, indicating that it is a Spring service component

@Service
public class WinesService {

    // This field is used to access the WinesRepository, which interacts with the database
    @Autowired
    private WinesRepository winesRepository;

    // This field is used to access the WinesSearchRepository, which provides search functionality
    @Autowired
    private WinesSearchRepository winesSearchRepository;

    // This method finds a wine by its ID
    public Wine findById(UUID wineId) {
        return this.winesRepository.findById(wineId).orElseThrow(
                () -> new BadRequestException("Wine with id " + wineId + " not found!")
        );
    }

    // This method finds all wines with pagination and sorting
    public Page<Wine> findAll(int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.winesRepository.findAll(pageable);
    }

    // This method finds all wines by status with pagination and sorting
    public Page<Wine> findAllByStatus(WineStatus status, int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.winesRepository.findAllByStatus(status, pageable);
    }

    // This method finds all wines visible to a user with pagination and sorting
    public Page<Wine> findVisibleWinesForUser(User user, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return winesRepository.findVisibleToUser(
                WineStatus.VERIFIED,
                user,
                List.of(WineStatus.USER_SUBMITTED, WineStatus.PENDING_REVIEW),
                pageable
        );
    }

    // This method saves a new wine
    public WineDTO save(WinePayload body, User user) {
        // Check if the wine already exists (case-insensitive)
        this.winesRepository.findByNameAndProducerAndVintage(
                body.name().trim().toLowerCase(),
                body.producer().trim().toLowerCase(),
                body.vintage()
        ).ifPresent(
                wine -> {
                    throw new BadRequestException("Wine " + body.name() + " already exists!");
                }
        );
        Wine newWine = new Wine(body.name(), body.producer(), body.country(), body.color(), body.grapeVarieties());

        // Set additional fields
        newWine.setVintage(body.vintage());
        newWine.setAbv(body.abv());
        newWine.setAppellation(body.appellation());
        newWine.setRegion(body.region());
        newWine.setSweetness(body.sweetness());
        newWine.setEffervescence(body.effervescence());
        newWine.setCategory(body.category());
        newWine.setDescription(body.description());
        newWine.setImageUrl(body.imageUrl());
        newWine.setBeginConsumeYear(body.beginConsumeYear());
        newWine.setEndConsumeYear(body.endConsumeYear());
        newWine.setDrinkability(body.drinkability());
        newWine.setBarcode(body.barcode());

        // Assign creator
        newWine.setCreatedBy(user);

// Set status based on a user role
        if (user.isAdmin()) {
            newWine.setStatus(WineStatus.VERIFIED);
        } else {
            newWine.setStatus(WineStatus.USER_SUBMITTED);
        }

        // Save the wine in the relational database
        Wine savedWine = this.winesRepository.save(newWine);

        // Save in Elasticsearch
        winesSearchRepository.save(WineDocument.fromEntity(savedWine));

        return new WineDTO(
                savedWine.getId(),
                savedWine.getName(),
                savedWine.getProducer(),
                savedWine.getVintage(),
                savedWine.getAbv(),
                savedWine.getGrapeVarieties(),
                savedWine.getAppellation(),
                savedWine.getCountry(),
                savedWine.getRegion(),
                savedWine.getColor(),
                savedWine.getSweetness(),
                savedWine.getEffervescence(),
                savedWine.getCategory(),
                savedWine.getDescription(),
                savedWine.getImageUrl(),
                savedWine.getBeginConsumeYear(),
                savedWine.getEndConsumeYear(),
                savedWine.getDrinkability(),
                savedWine.getBarcode(),
                savedWine.getProfessionalScore(),
                savedWine.getCommunityScore(),
                savedWine.getStatus()
        );
    }

    // This method updates an existing wine
    public Wine findByIdAndUpdate(UUID wineId, WinePayload body, User user) {
        Wine found = this.findById(wineId);
        // Check if the wine already exists (case-insensitive)
        this.winesRepository.findByNameAndProducerAndVintage(
                body.name().trim().toLowerCase(),
                body.producer().trim().toLowerCase(),
                body.vintage()
        ).ifPresent(
                wine -> {
                    if (!wine.getId().equals(found.getId())) {
                        throw new BadRequestException("Wine " + body.name() + " already exists!");
                    }
                }
        );
        // If the user is not admin, only allow editing their own USER_SUBMITTED wines
        if (!user.isAdmin()) {
            if (!user.getId().equals(found.getCreatedBy().getId())) {
                throw new BadRequestException("You are not authorized to update this wine.");
            }
            if (found.getStatus() != WineStatus.USER_SUBMITTED) {
                throw new BadRequestException("You can no longer modify this wine.");
            }
        }
        // Update the wine fields
        found.setName(body.name());
        found.setProducer(body.producer());
        found.setVintage(body.vintage());
        found.setAbv(body.abv());
        found.setGrapeVarieties(body.grapeVarieties());
        found.setAppellation(body.appellation());
        found.setCountry(body.country());
        found.setRegion(body.region());
        found.setColor(body.color());
        found.setSweetness(body.sweetness());
        found.setEffervescence(body.effervescence());
        found.setCategory(body.category());
        found.setDescription(body.description());
        found.setImageUrl(body.imageUrl());

        // Allow admin to update status
        if (user.isAdmin() && body.status() != null) {
            found.setStatus(body.status());
            // found.setVerifiedBy(user); // Uncomment if you track who verified it
        }

        return this.winesRepository.save(found);
    }

    // This method deletes a wine by its ID
    public void findByIdAndDelete(UUID wineId, User user) {
        Wine found = this.findById(wineId);
        if (!user.isAdmin()) {
            throw new BadRequestException("You are not authorized to delete this wine!");
        }
        this.winesRepository.delete(found);
    }
}
