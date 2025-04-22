package andrea_freddi.CAPSTONE_PROJECT.elasticsearch;

import andrea_freddi.CAPSTONE_PROJECT.entities.Wine;
import andrea_freddi.CAPSTONE_PROJECT.entities.WineCategory;
import andrea_freddi.CAPSTONE_PROJECT.entities.WineColor;
import andrea_freddi.CAPSTONE_PROJECT.entities.WineEffervescence;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "wines")
public class WineDocument {
    @Id
    private UUID id;
    private String name;
    private String producer;
    private Integer vintage;
    private List<String> grapeVarieties;
    private String appellation;
    private String country;
    private String region;
    private WineColor color;
    private WineEffervescence effervescence;
    private WineCategory category;

    public WineDocument(Wine wine) {
        this.id = wine.getId();
        this.name = wine.getName();
        this.producer = wine.getProducer();
        this.vintage = wine.getVintage();
        this.grapeVarieties = wine.getGrapeVarieties();
        this.appellation = wine.getAppellation();
        this.country = wine.getCountry();
        this.region = wine.getRegion();
        this.color = wine.getColor();
        this.effervescence = wine.getEffervescence();
        this.category = wine.getCategory();
    }

    public static WineDocument fromEntity(Wine wine) {
        return new WineDocument(wine);
    }

    @Override
    public String toString() {
        return "WineDocument{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", producer='" + producer + '\'' +
                ", vintage=" + vintage +
                ", grapeVarieties=" + grapeVarieties +
                ", appellation='" + appellation + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", color=" + color +
                ", effervescence=" + effervescence +
                ", category=" + category +
                '}';
    }
}
