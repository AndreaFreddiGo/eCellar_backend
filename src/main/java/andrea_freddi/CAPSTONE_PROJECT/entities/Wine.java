package andrea_freddi.CAPSTONE_PROJECT.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

// creates the Wine class and manages Getter and Setter and empty constructor with lombok
// I manage the constructor myself as well as toString

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "wines")
public class Wine {
    @Id
    @Setter(AccessLevel.NONE) // I don't want it to be set from the outside
    @GeneratedValue
    @Column(name = "wine_id", nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String producer;
    private Integer year;
    private Float abv;
    @ElementCollection
    @CollectionTable(name = "wine_grape_varieties", joinColumns = @JoinColumn(name = "wine_id"))
    @Column(name = "grape_variety")
    private List<String> grapeVarieties;
    private String appellation;
    @Column(nullable = false)
    private String country;
    private String region;
    @Column(nullable = false)
    private WineColor color;
    private WineSweetness sweetness;
    private WineEffervescence effervescence;
    private WineCategory category;
    private String description;
    private String imageUrl;

    public Wine(String name, String producer, Integer year, Float abv, List<String> grapeVarieties, String appellation, String country, String region, WineColor color, WineSweetness sweetness, WineEffervescence effervescence, WineCategory category, String description, String imageUrl) {
        this.name = name;
        this.producer = producer;
        this.year = year;
        this.abv = abv;
        this.grapeVarieties = grapeVarieties;
        this.appellation = appellation;
        this.country = country;
        this.region = region;
        this.color = color;
        this.sweetness = sweetness;
        this.effervescence = effervescence;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Wine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", producer='" + producer + '\'' +
                ", year=" + year +
                ", abv=" + abv +
                ", grapeVarieties=" + grapeVarieties +
                ", appellation='" + appellation + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", color=" + color +
                ", sweetness=" + sweetness +
                ", effervescence=" + effervescence +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
