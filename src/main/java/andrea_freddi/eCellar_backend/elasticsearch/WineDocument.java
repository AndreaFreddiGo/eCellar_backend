package andrea_freddi.eCellar_backend.elasticsearch;

import andrea_freddi.eCellar_backend.entities.Wine;
import andrea_freddi.eCellar_backend.entities.WineColor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "wines")
public class WineDocument {
    @Id
    private UUID id;

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "standard"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            })
    private String name;

    private String producer;
    @Field(type = FieldType.Integer)
    private Integer vintage;
    private List<String> grapeVarieties;
    private String appellation;
    private String country;
    private String region;

    @Field(type = FieldType.Keyword)
    private WineColor color;

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
                '}';
    }
}
