package andrea_freddi.CAPSTONE_PROJECT.payloads;

/*
 * Data Transfer Object for Cellar entity
 * This class is used to transfer cellar data between different layers of the application
 * It contains all the necessary fields to represent a cellar
 */

import java.util.List;
import java.util.UUID;

public record CellarDTO(
        UUID id,
        String name,
        String description,
        UUID userId,
        List<CellarWineDTO> cellarWines
) {
}
