package andrea_freddi.CAPSTONE_PROJECT.services;

import andrea_freddi.CAPSTONE_PROJECT.elasticsearch.WineDocument;
import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.entities.Wine;
import andrea_freddi.CAPSTONE_PROJECT.entities.WineStatus;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import andrea_freddi.CAPSTONE_PROJECT.mappers.WineMapper;
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
import org.springframework.transaction.annotation.Transactional;

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

    // This field is used to access the WineMapper, which converts between Wine entities and WineDTOs
    @Autowired
    private WineMapper wineMapper;

    // This method finds a wine by its ID
    public Wine findById(UUID wineId) {
        // If the wine is not found, throw a BadRequestException
        return this.winesRepository.findById(wineId).orElseThrow(
                () -> new BadRequestException("Wine with id " + wineId + " not found!")
        );
    }

    // This method finds a wine by its ID (if user is admin the wine is searched in the database, otherwise in the search repository)
    public WineDTO findByIdAndUser(UUID wineId, User user) {
        // Check if the user is an admin
        if (user.isAdmin()) {
            // If the user is an admin, find the wine in the database
            Wine found = this.winesRepository.findById(wineId).orElseThrow(
                    () -> new BadRequestException("Wine with id " + wineId + " not found!")
            );
            // Convert the Wine entity to a WineDTO
            return wineMapper.wineToDTO(found);
        }
        // If the user is not an admin, find the wine in the search repository
        WineDocument found = this.winesSearchRepository.findById(wineId).orElseThrow(
                () -> new BadRequestException("Wine with id " + wineId + " not found!")
        );
        // Convert the WineDocument to a Wine entity
        return wineMapper.wineDocumentToDTO(found);
    }

    // This method finds all wines with pagination and sorting
    public Page<WineDTO> findAll(int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.winesRepository.findAll(pageable).map(wineMapper::wineToDTO);
    }

    // This method finds all wines by status with pagination and sorting
    public Page<WineDTO> findAllByStatus(WineStatus status, int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.winesRepository.findAllByStatus(status, pageable).map(wineMapper::wineToDTO);
    }

    // This method finds all wines visible to a user with pagination and sorting
    public Page<WineDTO> findVisibleWinesForUser(User user, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return winesRepository.findVisibleToUser(
                WineStatus.VERIFIED,
                user,
                List.of(WineStatus.USER_SUBMITTED, WineStatus.PENDING_REVIEW),
                pageable
        ).map(wineMapper::wineToDTO);
    }

    // This method saves a new wine
    @Transactional // This annotation indicates that the method should be executed within a transaction
    public WineDTO save(WinePayload body, User user) {
        // Check if the wine name already exists (case-insensitive)
        this.winesRepository.findByNameAndProducerAndVintage(
                body.name().trim().toLowerCase(),
                body.producer().trim().toLowerCase(),
                body.vintage()
        ).ifPresent(wine -> {
            throw new BadRequestException("Wine " + body.name() + " already exists!");
        });

        // If it doesn't exist, create a new wine and save it
        Wine newWine = new Wine(body.name(), body.producer(), body.country(), body.color(), body.grapeVarieties());
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
        newWine.setCreatedBy(user);
        // Set the status based on whether the user is an admin or not
        newWine.setStatus(user.isAdmin() ? WineStatus.VERIFIED : WineStatus.USER_SUBMITTED);

        Wine savedWine = this.winesRepository.save(newWine);
        winesSearchRepository.save(WineDocument.fromEntity(savedWine));

        // Convert the saved wine entity to a WineDTO and return it
        return wineMapper.wineToDTO(savedWine);
    }

    // This method updates an existing wine
    @Transactional // This annotation indicates that the method should be executed within a transaction
    public WineDTO findByIdAndUpdate(UUID wineId, WinePayload body, User user) {
        // Find the wine by its ID
        Wine found = this.findById(wineId);
        // Check if the wine name already exists (case-insensitive)
        this.winesRepository.findByNameAndProducerAndVintage(
                body.name().trim().toLowerCase(),
                body.producer().trim().toLowerCase(),
                body.vintage()
        ).ifPresent(wine -> {
            if (!wine.getId().equals(found.getId())) {
                throw new BadRequestException("Wine " + body.name() + " already exists!");
            }
        });
        // Check if the user is an admin or if they are the creator of the wine
        if (!user.isAdmin()) {
            if (!user.getId().equals(found.getCreatedBy().getId())) {
                throw new BadRequestException("You are not authorized to update this wine.");
            }
            if (found.getStatus() != WineStatus.USER_SUBMITTED) {
                throw new BadRequestException("You can no longer modify this wine.");
            }
        }
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
        if (user.isAdmin() && body.status() != null) {
            found.setStatus(body.status());
        }
        Wine updated = this.winesRepository.save(found);
        winesSearchRepository.save(WineDocument.fromEntity(updated));
        return wineMapper.wineToDTO(updated);
    }

    // This method deletes a wine by its ID
    @Transactional // This annotation indicates that the method should be executed within a transaction
    public void findByIdAndDelete(UUID wineId, User user) {
        Wine found = this.findById(wineId);
        if (!user.isAdmin()) {
            throw new BadRequestException("You are not authorized to delete this wine!");
        }
        this.winesRepository.delete(found);
    }
}
