package andrea_freddi.CAPSTONE_PROJECT.repositories;

import andrea_freddi.CAPSTONE_PROJECT.entities.User;
import andrea_freddi.CAPSTONE_PROJECT.entities.Wine;
import andrea_freddi.CAPSTONE_PROJECT.entities.WineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WinesRepository extends JpaRepository<Wine, UUID> {
    // this method is used to find all wines by status
    Page<Wine> findAllByStatus(WineStatus status, Pageable pageable);

    // this method is used to find a wine by name, producer and vintage (case-insensitive)
    @Query("SELECT w FROM Wine w WHERE LOWER(w.name) = LOWER(:name) AND LOWER(w.producer) = LOWER(:producer) AND w.vintage = :vintage")
    Optional<Wine> findByNameAndProducerAndVintage(@Param("name") String name, @Param("producer") String producer, @Param("vintage") Integer vintage);

    // this method is used to find all wines by status and user submitted
    @Query("""
                SELECT w FROM Wine w
                WHERE w.status = :verified
                   OR (w.createdBy = :user AND w.status IN (:userStatuses))
            """)
    Page<Wine> findVisibleToUser(
            @Param("verified") WineStatus verified,
            @Param("user") User user,
            @Param("userStatuses") List<WineStatus> userStatuses,
            Pageable pageable
    );
    
}

