package andrea_freddi.eCellar_backend.services;

import andrea_freddi.eCellar_backend.elasticsearch.WineDocument;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
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
        String[] tokens = text.trim().toLowerCase().split("\\s+");
        BoolQuery.Builder mustBuilder = new BoolQuery.Builder();

        for (String token : tokens) {
            BoolQuery.Builder innerShould = new BoolQuery.Builder();

            if (token.length() <= 4) {
                innerShould.should(q -> q.prefix(p -> p.field("name").value(token)));
                innerShould.should(q -> q.prefix(p -> p.field("producer").value(token)));
                innerShould.should(q -> q.prefix(p -> p.field("grapeVarieties").value(token)));
                innerShould.should(q -> q.prefix(p -> p.field("appellation").value(token)));
                innerShould.should(q -> q.prefix(p -> p.field("country").value(token)));
                innerShould.should(q -> q.prefix(p -> p.field("region").value(token)));
            } else {
                innerShould.should(q -> q.match(m -> m.field("name").query(token).fuzziness("AUTO")));
                innerShould.should(q -> q.match(m -> m.field("producer").query(token).fuzziness("AUTO")));
                innerShould.should(q -> q.match(m -> m.field("grapeVarieties").query(token).fuzziness("AUTO")));
                innerShould.should(q -> q.match(m -> m.field("appellation").query(token).fuzziness("AUTO")));
                innerShould.should(q -> q.match(m -> m.field("country").query(token).fuzziness("AUTO")));
                innerShould.should(q -> q.match(m -> m.field("region").query(token).fuzziness("AUTO")));
            }

            // Colore (solo match esatto, upper perché è keyword)
            innerShould.should(q -> q.term(t -> t.field("color").value(token.toUpperCase())));

            // Annate
            try {
                int yearPrefix = Integer.parseInt(token);
                if (token.length() == 4) {
                    innerShould.should(q -> q.term(t -> t.field("vintage").value(yearPrefix)));
                } else if (token.length() == 3) {
                    int min = yearPrefix * 10;
                    int max = min + 10;
                    innerShould.should(q -> q.range(r -> r.field("vintage").gte(JsonData.of(min)).lt(JsonData.of(max))));
                } else if (token.length() == 2) {
                    int min = yearPrefix * 100;
                    int max = min + 100;
                    innerShould.should(q -> q.range(r -> r.field("vintage").gte(JsonData.of(min)).lt(JsonData.of(max))));
                }
            } catch (NumberFormatException ignored) {
            }

            mustBuilder.must(q -> q.bool(innerShould.build()));
        }

        Query elasticQuery = Query.of(q -> q.bool(mustBuilder.build()));

        NativeQuery query = NativeQuery.builder()
                .withQuery(elasticQuery)
                .build();

        SearchHits<WineDocument> hits = elasticsearchOperations.search(query, WineDocument.class);
        return hits.stream().map(SearchHit::getContent).toList();
    }

}
