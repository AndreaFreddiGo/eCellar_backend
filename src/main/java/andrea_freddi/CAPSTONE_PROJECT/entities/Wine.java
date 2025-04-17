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
    private Integer vintage;
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
    @Enumerated(EnumType.STRING)
    private WineColor color;
    @Enumerated(EnumType.STRING)
    private WineSweetness sweetness;
    @Enumerated(EnumType.STRING)
    private WineEffervescence effervescence;
    @Enumerated(EnumType.STRING)
    private WineCategory category;
    private String description;
    private String imageUrl;
    @Column(name = "begin_consume_year")
    private Integer beginConsumeYear;
    @Column(name = "end_consume_year")
    private Integer endConsumeYear;
    @Enumerated(EnumType.STRING)
    private Drinkability drinkability;
    private String barcode;
    @Column(name = "professional_score")
    private Float professionalScore;
    @Column(name = "community_score")
    private Float communityScore;

    @OneToMany(mappedBy = "wine")
    private List<CellarWine> cellarWines;

    public Wine(String name, String producer, String country, WineColor color, List<String> grapeVarieties) {
        this.name = name;
        this.producer = producer;
        this.country = country;
        this.color = color;
        this.grapeVarieties = grapeVarieties;
    }

    @Override
    public String toString() {
        return "Wine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", producer='" + producer + '\'' +
                ", vintage=" + vintage +
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
