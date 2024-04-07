package com.barelaws.barelaws_api.repository;

import com.barelaws.barelaws_api.model.SupremeCourtData;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JudgementRepository extends ElasticsearchRepository<SupremeCourtData, String> {

    @Query("""
            {
                "fuzzy": {
                    "judgementText": {
                        "value": "?0",
                        "fuzziness": "AUTO"
                    }
                }
            }
            """)
    List<SupremeCourtData> findByFuzzyJudgementText(String text);


    @Highlight(fields = {
            @HighlightField(name = "judgementText")
    })
    List<SupremeCourtData> findByJudgementText(String text);


    @NotNull
    Optional<SupremeCourtData> findById(@NotNull String id);


    @Highlight(fields = {
            @HighlightField(name = "judgementText")
    })
    @Query("""
            {
                "bool": {
                    "filter": [
                        {"terms": {"_id": ["?0"]}},
                        {"simple_query_string": {"query": "?1", "fields": ["judgementText"]}}
                    ]
                }
            }
            """)
    List<SupremeCourtData> findByIdAndJudgementText(String id, String text);


}
