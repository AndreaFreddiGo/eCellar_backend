package andrea_freddi.CAPSTONE_PROJECT.services;

import andrea_freddi.CAPSTONE_PROJECT.entities.Cellar;
import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.exception.BadRequestException;
import andrea_freddi.CAPSTONE_PROJECT.payloads.CellarPayload;
import andrea_freddi.CAPSTONE_PROJECT.repositories.CellarsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// This class is a service that handles the business logic for cellars
// It is annotated with @Service, which indicates that it is a service component in the Spring framework

@Service
public class CellarsService {

    // This field is used to access the CellarsRepository, which is used to interact with the database
    @Autowired
    private CellarsRepository cellarsRepository;

    // This method finds a cellar by its ID
    public Cellar findById(UUID cellarId) {
        return this.cellarsRepository.findById(cellarId).orElseThrow(
                () -> new BadRequestException("Cellar with id " + cellarId + " not found!")
        );
    }

    // This method finds all cellars with pagination and sorting
    public Page<Cellar> findAll(int page, int size, String sortBy) {
        if (size > 20) size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.cellarsRepository.findAll(pageable);
    }

    // This method finds all cellars by user ID
    public List<Cellar> findAllByUserId(User user) {
        return this.cellarsRepository.findAllByUser(user);
    }

    // This method saves a new cellar
    public Cellar save(CellarPayload body, User user) {
        // Check if the cellar name already exists for the user
        this.cellarsRepository.findByNameAndUser(body.name(), user).ifPresent(
                cellar -> {
                    throw new BadRequestException("Cellar with name " + body.name() + " already exists!");
                }
        );
        // If it doesn't exist, create a new cellar and save it
        Cellar newCellar = new Cellar(body.name(), body.description(), user);
        return this.cellarsRepository.save(newCellar);
    }

    // This method updates an existing cellar by its ID
    public Cellar findByIdAndUpdate(UUID cellarId, CellarPayload body, User user) {
        // Find the cellar and check ownership
        Cellar found = this.findById(cellarId);
        if (!found.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not authorized to update this cellar!");
        }
        // Check if another cellar with the same name already exists for the user
        this.cellarsRepository.findByNameAndUser(body.name(), user).ifPresent(existing -> {
            if (!existing.getId().equals(cellarId)) {
                throw new BadRequestException("You already have a cellar named '" + body.name() + "'!");
            }
        });
        // Apply the updates
        found.setName(body.name());
        found.setDescription(body.description());
        return this.cellarsRepository.save(found);
    }


    // This method deletes a cellar by its ID
    public void findByIdAndDelete(UUID cellarId, User user) {
        // Find the cellar and check ownership
        Cellar found = this.findById(cellarId);
        if (!found.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You are not authorized to delete this cellar!");
        }
        // Delete the cellar
        this.cellarsRepository.delete(found);
    }
}