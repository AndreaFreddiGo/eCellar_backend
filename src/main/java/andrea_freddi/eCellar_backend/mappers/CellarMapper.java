package andrea_freddi.eCellar_backend.mappers;

import andrea_freddi.eCellar_backend.entities.Cellar;
import andrea_freddi.eCellar_backend.payloads.CellarDTO;
import andrea_freddi.eCellar_backend.payloads.CellarWineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

// This class is responsible for mapping between Cellar entities and CellarDTOs

@Component
public class CellarMapper {

    @Autowired
    CellarWineMapper cellarWineMapper;

    // Converts a Cellar entity to a CellarDTO
    public CellarDTO cellarToDTO(Cellar cellar) {
        if (cellar == null) return null;

        List<CellarWineDTO> cellarWinesDTO = cellar.getCellarWines() != null
                ? cellar.getCellarWines().stream()
                .map(cellarWineMapper::cellarWineToDTO)
                .toList()
                : null;

        return new CellarDTO(
                cellar.getId(),
                cellar.getName(),
                cellar.getDescription(),
                cellar.getUser() != null ? cellar.getUser().getId() : null,
                cellarWinesDTO
        );
    }
}
