package andrea_freddi.eCellar_backend.services;

import andrea_freddi.eCellar_backend.entities.Cellar;
import andrea_freddi.eCellar_backend.entities.CellarWine;
import andrea_freddi.eCellar_backend.entities.User;
import andrea_freddi.eCellar_backend.entities.Wine;
import andrea_freddi.eCellar_backend.exception.BadRequestException;
import andrea_freddi.eCellar_backend.mappers.CellarWineMapper;
import andrea_freddi.eCellar_backend.payloads.CellarWineDTO;
import andrea_freddi.eCellar_backend.payloads.CellarWinePayload;
import andrea_freddi.eCellar_backend.repositories.CellarWinesRepository;
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

// This service class manages CRUD operations related to CellarWine entities
// All operations are restricted to the authenticated user who owns the cellar wine entry.

@Service
public class CellarWinesService {

    // This field is used to access the CellarWinesRepository, which is used to interact with the database
    @Autowired
    private CellarWinesRepository cellarWinesRepository;

    // The SecurityUtils is injected into this service to handle security-related operations
    @Autowired
    private SecurityUtils securityUtils;

    // The CellarWineMapper is injected to convert between CellarWine entities and CellarWineDTOs
    @Autowired
    private CellarWineMapper cellarWineMapper;

    // The CellarsService is injected to handle operations related to cellars
    @Autowired
    private CellarsService cellarService;

    // The WinesService is injected to handle operations related to wines
    @Autowired
    private WinesService wineService;

    // This method finds a CellarWine by its ID
    public CellarWine findById(UUID cellarWineId) {
        return this.cellarWinesRepository.findById(cellarWineId).orElseThrow(
                () -> new BadRequestException("CellarWine with id " + cellarWineId + " not found!")
        );
    }

    // This method finds a CellarWine by its ID and user
    public CellarWineDTO findByIdAndUser(UUID cellarWineId, User user) {
        CellarWine found = this.findById(cellarWineId);
        // Check if the cellar wine belongs to the user or if the user is an admin
        securityUtils.checkOwnershipOrAdmin(user, found.getUser().getId());
        return cellarWineMapper.cellarWineToDTO(found);
    }

    // This method finds all CellarWines with pagination and sorting
    public Page<CellarWineDTO> findAll(int page, int size, String sortBy) {
        if (size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.cellarWinesRepository.findAll(pageable).map(cellarWineMapper::cellarWineToDTO);
    }

    // This method finds all CellarWines by user
    public List<CellarWineDTO> findAllByUser(User user) {
        List<CellarWine> cellarWines = this.cellarWinesRepository.findAllByUser(user);
        return cellarWines.stream()
                .map(cellarWineMapper::cellarWineToDTO)
                .collect(Collectors.toList());
    }

    // This method saves a new CellarWine
    public CellarWineDTO save(CellarWinePayload body, User user) {
        Cellar cellar = cellarService.findById(body.cellarId());
        securityUtils.checkOwnershipOrAdmin(user, cellar.getUser().getId());

        Wine wine = wineService.findById(body.wineId());

        CellarWine newWine = new CellarWine(
                body.quantity(),
                body.size(),
                body.isPublic(),
                body.personalNotes(),
                body.purchaseDate(),
                body.purchasePrice(),
                body.askingPrice(),
                body.myScore(),
                cellar,
                wine,
                user
        );

        return cellarWineMapper.cellarWineToDTO(cellarWinesRepository.save(newWine));
    }


    // This method updates an existing CellarWine by its ID
    public CellarWineDTO findByIdAndUserAndUpdate(UUID cellarWineId, CellarWinePayload body, User user) {
        CellarWine found = this.findById(cellarWineId);
        securityUtils.checkOwnershipOrAdmin(user, found.getUser().getId());

        found.setQuantity(body.quantity());
        found.setSize(body.size());
        found.setPublic(body.isPublic());
        found.setPersonalNotes(body.personalNotes());
        found.setPurchaseDate(body.purchaseDate());
        found.setPurchasePrice(body.purchasePrice());
        found.setAskingPrice(body.askingPrice());
        found.setMyScore(body.myScore());

        return cellarWineMapper.cellarWineToDTO(this.cellarWinesRepository.save(found));
    }

    // This method deletes a CellarWine by its ID
    public void findByIdAndUserAndDelete(UUID cellarWineId, User user) {
        CellarWine found = this.findById(cellarWineId);
        securityUtils.checkOwnershipOrAdmin(user, found.getUser().getId());
        this.cellarWinesRepository.delete(found);
    }
}
