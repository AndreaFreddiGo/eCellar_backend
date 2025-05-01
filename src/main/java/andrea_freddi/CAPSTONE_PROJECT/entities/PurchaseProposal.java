package andrea_freddi.CAPSTONE_PROJECT.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// creates the PurchaseProposal class and manages Getter and Setter and empty constructor with lombok
// instead I manage the constructor as well as the toString

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase_proposals")
public class PurchaseProposal {
    @Id
    @Setter(AccessLevel.NONE) // I don't want it to be set from the outside
    @GeneratedValue
    @Column(name = "purchase_proposal_id", nullable = false)
    private UUID id;
    @Column(name = "proposing_price", nullable = false)
    private BigDecimal proposingPrice;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProposalStatus status;
    @CreationTimestamp
    @Column(name = "proposal_date", nullable = false)
    private LocalDateTime proposalDate;
    private String message;

    // relations between PurchaseProposal and other entities
    // a user and a cellarWine can have many purchase proposals

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private CellarWine cellarWine;

    public PurchaseProposal(BigDecimal proposingPrice, String message, User user, CellarWine cellarWine) {
        this.proposingPrice = proposingPrice;
        this.status = ProposalStatus.PENDING;
        this.message = message;
        this.user = user;
        this.cellarWine = cellarWine;
    }

    @Override
    public String toString() {
        return "PurchaseProposal{" +
                "id=" + id +
                ", proposingPrice=" + proposingPrice +
                ", status=" + status +
                ", proposalDate=" + proposalDate +
                ", message='" + message + '\'' +
                ", userId=" + (user != null ? user.getId() : "null") +
                ", cellarWineId=" + (cellarWine != null ? cellarWine.getId() : "null") +
                '}';
    }
}

