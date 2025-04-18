package andrea_freddi.CAPSTONE_PROJECT.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

// creates the Address class and manages Getter and Setter and empty constructor with lombok
// instead I manage the constructor as well as the toString

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @Setter(AccessLevel.NONE) // I don't want it to be set from the outside
    @GeneratedValue
    @Column(name = "address_id", nullable = false)
    private UUID id;
    @Column(nullable = false)
    private String label; // es. "Home", "Work", "Cellar"
    @Column(name = "address_line", nullable = false)
    private String addressLine;
    @Column(nullable = false)
    private String city;
    private String province;
    @Column(name = "postal_code", nullable = false)
    private String postalCode;
    @Column(nullable = false)
    private String country;

    // relation between Address and User
    // a user can have multiple addresses

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Address(String label, String addressLine, String city, String province, String postalCode, String country, User user) {
        this.label = label;
        this.addressLine = addressLine;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
        this.user = user;
    }

    @Override
    public String toString() {
        return label + ": " + addressLine + ", " + city + ", " + province + ", " + postalCode + ", " + country;
    }
}
