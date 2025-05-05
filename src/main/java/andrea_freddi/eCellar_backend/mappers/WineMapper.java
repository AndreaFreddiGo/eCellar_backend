package andrea_freddi.eCellar_backend.mappers;

import andrea_freddi.eCellar_backend.elasticsearch.WineDocument;
import andrea_freddi.eCellar_backend.entities.Wine;
import andrea_freddi.eCellar_backend.payloads.WineDTO;
import andrea_freddi.eCellar_backend.repositories.WinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// This class is responsible for mapping between Wine entities and WineDTOs

@Component
public class WineMapper {

    @Autowired
    private WinesRepository winesRepository; // Repository for database operations

    // Converts a Wine entity to a WineDTO
    public WineDTO wineToDTO(Wine wine) {
        if (wine == null) return null;

        return new WineDTO(
                wine.getId(),
                wine.getName(),
                wine.getProducer(),
                wine.getVintage(),
                wine.getAbv(),
                wine.getGrapeVarieties(),
                wine.getAppellation(),
                wine.getCountry(),
                wine.getRegion(),
                wine.getColor(),
                wine.getSweetness(),
                wine.getEffervescence(),
                wine.getCategory(),
                wine.getDescription(),
                wine.getImageUrl(),
                wine.getBeginConsumeYear(),
                wine.getEndConsumeYear(),
                wine.getDrinkability(),
                wine.getBarcode(),
                wine.getProfessionalScore(),
                wine.getCommunityScore(),
                wine.getStatus(),
                wine.getCreatedBy() != null ? wine.getCreatedBy().getId() : null,
                wine.getVerifiedBy() != null ? wine.getVerifiedBy().getId() : null
        );
    }

    // Converts a WineDocument to a full WineDTO by loading the full entity from DB
    public WineDTO wineDocumentToDTO(WineDocument doc) {
        if (doc == null) return null;

        return winesRepository.findById(doc.getId())
                .map(this::wineToDTO)
                .orElse(null);
    }

    public Wine wineDTOToEntity(WineDTO dto) {
        if (dto == null) return null;

        Wine wine = new Wine();
        wine.setName(dto.name());
        wine.setProducer(dto.producer());
        wine.setVintage(dto.vintage());
        wine.setAbv(dto.abv());
        wine.setGrapeVarieties(dto.grapeVarieties());
        wine.setAppellation(dto.appellation());
        wine.setCountry(dto.country());
        wine.setRegion(dto.region());
        wine.setColor(dto.color());
        wine.setSweetness(dto.sweetness());
        wine.setEffervescence(dto.effervescence());
        wine.setCategory(dto.category());
        wine.setDescription(dto.description());
        wine.setImageUrl(dto.imageUrl());
        wine.setBeginConsumeYear(dto.beginConsumeYear());
        wine.setEndConsumeYear(dto.endConsumeYear());
        wine.setDrinkability(dto.drinkability());
        wine.setBarcode(dto.barcode());
        wine.setProfessionalScore(dto.professionalScore());
        wine.setCommunityScore(dto.communityScore());
        wine.setStatus(dto.status());
        // createdBy and verifiedBy intentionally omitted

        return wine;
    }


}

