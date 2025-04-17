package andrea_freddi.CAPSTONE_PROJECT.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private LocalDateTime purchaseDate;
    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;
    @Column(name = "asking_price")
    private BigDecimal askingPrice;
    @Column(name = "my_score")
    private Float myScore;

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

    public CellarWine(int quantity, User user, Wine wine, boolean publicWine) {
        this.quantity = quantity;
        this.user = user;
        this.wine = wine;
        this.isPublic = publicWine;
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
                ", userId=" + (user != null ? user.getId() : "null") +
                ", wineId=" + (wine != null ? wine.getId() : "null") +
                ", cellarId=" + (cellar != null ? cellar.getId() : "null") +
                '}';
    }
}
