package andrea_freddi.eCellar_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

// creates the CellarWine class and manages Getter and Setter and empty constructor with lombok
// I manage the constructor myself as well as toString

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cellar_wines")
public class CellarWine {
    @Id
    @Setter(AccessLevel.NONE) // I don't want it to be set from the outside
    @GeneratedValue
    @Column(name = "cellar_wine_id", nullable = false)
    private UUID id;
    @Column(nullable = false)
    private int quantity;
    @Column(name = "bottle_size", nullable = false)
    private BottleSize size;
    @Column(name = "is_public", nullable = false)
    private boolean isPublic;
    @Column(name = "personal_notes")
    private String personalNotes;
    @Column(name = "purchase_date")
    private Integer purchaseDate;
    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;
    @Column(name = "asking_price")
    private BigDecimal askingPrice;
    @Column(name = "my_score")
    private Integer myScore;

    // relations between CellarWine and other entities: User, Wine and Cellar
    // a CellarWine is associated with a User, a Wine and a Cellar
    // a User can have multiple CellarWines, a Wine can be in multiple CellarWines
    // and a Cellar can have multiple CellarWines

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "wine_id", nullable = false)
    private Wine wine;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cellar_id", nullable = false)
    private Cellar cellar;

    @OneToMany(mappedBy = "cellarWine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseProposal> purchaseProposals;

    public CellarWine(
            int quantity,
            BottleSize size,
            boolean isPublic,
            String personalNotes,
            Integer purchaseDate,
            BigDecimal purchasePrice,
            BigDecimal askingPrice,
            Integer myScore,
            Cellar cellar,
            Wine wine,
            User user
    ) {
        this.quantity = quantity;
        this.size = size;
        this.isPublic = isPublic;
        this.personalNotes = personalNotes;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.askingPrice = askingPrice;
        this.myScore = myScore;
        this.cellar = cellar;
        this.wine = wine;
        this.user = user;
    }


    @Override
    public String toString() {
        return "CellarWine{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", size=" + size +
                ", publicWine=" + isPublic +
                ", personalNotes='" + personalNotes + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", purchasePrice=" + purchasePrice +
                ", askingPrice=" + askingPrice +
                ", myScore=" + myScore +
                ", userId=" + (user != null ? user.getId() : "null") +
                ", wineId=" + (wine != null ? wine.getId() : "null") +
                ", cellarId=" + (cellar != null ? cellar.getId() : "null") +
                '}';
    }
}
