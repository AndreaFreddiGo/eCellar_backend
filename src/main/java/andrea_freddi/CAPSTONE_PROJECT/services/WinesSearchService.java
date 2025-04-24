package andrea_freddi.CAPSTONE_PROJECT.services;

import andrea_freddi.CAPSTONE_PROJECT.elasticsearch.WineDocument;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * This class is a service that handles the search functionality for wines.
 * It uses Elasticsearch to perform the search operations.
 * The class is annotated with @Service, indicating that it is a Spring service component.
 */

@Service
public class WinesSearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public List<WineDocument> search(String text) {
        BoolQuery boolQuery = BoolQuery.of(b -> {
            // MultiMatch query per ricerca nei campi testuali
            b.should(q -> q.multiMatch(MultiMatchQuery.of(m -> m
                    .query(text)
                    .fields("name^2", "producer", "grapeVarieties", "appellation", "country", "region")
                    .fuzziness("AUTO")
            )));

            // If the text is numeric, search in the vintage field
            try {
                int year = Integer.parseInt(text);
                b.should(q -> q.term(TermQuery.of(t -> t
                        .field("vintage")
                        .value(year)
                )));
            } catch (NumberFormatException ignored) {
                // Ignore if the text is not numeric
            }

            return b;
        });

        Query elasticQuery = Query.of(q -> q.bool(boolQuery));

        NativeQuery query = NativeQuery.builder()
                .withQuery(elasticQuery)
                .build();

        SearchHits<WineDocument> hits = elasticsearchOperations.search(query, WineDocument.class);
        return hits.stream().map(SearchHit::getContent).toList();
    }
}
