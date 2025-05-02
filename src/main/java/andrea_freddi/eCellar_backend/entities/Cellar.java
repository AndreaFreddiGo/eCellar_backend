package andrea_freddi.eCellar_backend.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

// creates the Cellar class and manages Getter and Setter and empty constructor with lombok
// I manage the constructor myself as well as toString

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cellars")
public class Cellar {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE) // I don't want it to be set from the outside
    private UUID id;
    @Column(nullable = false)
    private String name;
    private String description;

    // relations between Cellar and other entities: User and CellarWine
    // a Cellar is associated with a User and can have multiple CellarWines

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "cellar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CellarWine> cellarWines;

    public Cellar(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Cellar{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + (user != null ? user.getId() : "null") +
                '}';
    }
}
