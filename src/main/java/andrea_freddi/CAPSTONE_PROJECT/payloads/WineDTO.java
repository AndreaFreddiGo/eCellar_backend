package andrea_freddi.CAPSTONE_PROJECT.payloads;

import andrea_freddi.CAPSTONE_PROJECT.entities.*;

import java.util.List;
import java.util.UUID;

/*
 * Data Transfer Object for Wine entity.
 * This class is used to transfer wine data between different layers of the application.
 * It contains all the necessary fields to represent a wine.
 */

public record WineDTO(
        UUID id,
        String name,
        String producer,
        Integer vintage,
        Float abv,
        List<String> grapeVarieties,
        String appellation,
        String country,
        String region,
        WineColor color,
        WineSweetness sweetness,
        WineEffervescence effervescence,
        WineCategory category,
        String description,
        String imageUrl,
        Integer beginConsumeYear,
        Integer endConsumeYear,
        Drinkability drinkability,
        String barcode,
        Float professionalScore,
        Float communityScore,
        WineStatus status,
        UUID createdBy,
        UUID verifiedBy
) {
}
