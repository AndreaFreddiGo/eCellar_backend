package andrea_freddi.eCellar_backend.payloads;

/*
 * Data Transfer Object for CellarWine entity
 * This class is used to transfer CellarWine data between different layers of the application
 * It contains all the necessary fields to represent a CellarWine
 */

import andrea_freddi.eCellar_backend.entities.BottleSize;

import java.math.BigDecimal;
import java.util.UUID;

public record CellarWineDTO(
        UUID id,
        int quantity,
        BottleSize size,
        boolean isPublic,
        String personalNotes,
        Integer purchaseDate,
        BigDecimal purchasePrice,
        BigDecimal askingPrice,
        Integer myScore,
        UUID userId,
        UUID cellarId,
        UUID wineId,
        String wineName,
        String wineProducer,
        Integer wineVintage
) {
}
