package andrea_freddi.CAPSTONE_PROJECT.services;

import andrea_freddi.CAPSTONE_PROJECT.elasticsearch.WineDocument;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WinesSearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public List<WineDocument> search(String text) {
        // Costruzione query compatibile ELC
        Query elasticQuery = Query.of(q -> q
                .multiMatch(MultiMatchQuery.of(m -> m
                        .query(text)
                        .fields("name^2", "producer", "vintage", "grapeVarieties", "appellation", "country", "region", "color", "effervescence", "category")
                        .fuzziness("AUTO")
                        .type(co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.BoolPrefix)
                ))
        );

        NativeQuery query = NativeQuery.builder()
                .withQuery(elasticQuery)
                .withSort(Sort.by("name.keyword").ascending())
                .build();

        SearchHits<WineDocument> hits = elasticsearchOperations.search(query, WineDocument.class);
        return hits.stream().map(SearchHit::getContent).toList();
    }
}
