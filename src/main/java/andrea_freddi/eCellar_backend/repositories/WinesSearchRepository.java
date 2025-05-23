package andrea_freddi.eCellar_backend.repositories;

import andrea_freddi.eCellar_backend.elasticsearch.WineDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/*
 * This interface extends the ElasticsearchRepository to provide CRUD operations for WineDocument entities.
 * It also includes a custom method to search for wines by name or producer.
 */

@Repository
public interface WinesSearchRepository extends ElasticsearchRepository<WineDocument, UUID> {
}


