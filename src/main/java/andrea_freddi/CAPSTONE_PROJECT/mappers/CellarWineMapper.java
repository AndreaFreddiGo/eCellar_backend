package andrea_freddi.CAPSTONE_PROJECT.mappers;

import andrea_freddi.CAPSTONE_PROJECT.entities.CellarWine;
import andrea_freddi.CAPSTONE_PROJECT.payloads.CellarWineDTO;
import org.springframework.stereotype.Component;

// This class is responsible for mapping between CellarWine entities and CellarWineDTOs

@Component
public class CellarWineMapper {

    // Converts a CellarWine entity to a CellarWineDTO
    public CellarWineDTO cellarWineToDTO(CellarWine cellarWine) {
        if (cellarWine == null) return null;

        return new CellarWineDTO(
                cellarWine.getId(),
                cellarWine.getQuantity(),
                cellarWine.getSize(),
                cellarWine.isPublic(),
                cellarWine.getPersonalNotes(),
                cellarWine.getPurchaseDate(),
                cellarWine.getPurchasePrice(),
                cellarWine.getAskingPrice(),
                cellarWine.getMyScore(),
                cellarWine.getUser() != null ? cellarWine.getCellar().getId() : null,
                cellarWine.getCellar() != null ? cellarWine.getCellar().getId() : null,
                cellarWine.getWine() != null ? cellarWine.getWine().getId() : null
        );
    }
}
