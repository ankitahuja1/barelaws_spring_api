package com.barelaws.barelaws_api.service;

import com.barelaws.barelaws_api.model.SupremeCourtData;
import com.barelaws.barelaws_api.repository.JudgementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class JudgementServices {

    private final WebClient webClient;

    @Autowired
    public JudgementServices(WebClient.Builder webClientBuilder, @Value("${spring.elasticsearch.uris}") String elasticURL, ObjectMapper objectMapper) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // Set maxInMemorySize to unlimited
                .build();

        this.webClient = webClientBuilder.baseUrl(elasticURL).exchangeStrategies(exchangeStrategies).build();
    }

    @Autowired
    private JudgementRepository judgementRepository;

    public List<SupremeCourtData> getAllSupremeCourtJudgements() {
        List<SupremeCourtData> judgements = new ArrayList<>();
        judgementRepository.findAll()
                .forEach(judgements::add);
        return judgements;
    }


    public Optional<SupremeCourtData> findBySupremeCourtId(String id) {
        return judgementRepository.findById(id);
    }

    public List<SupremeCourtData> supremeCourtSearchIdText(String id, String text) {
        return judgementRepository.findByIdAndJudgementText(id, text);
    }


    public Mono<Object> searchDocuments(String searchTerm) {

        String queryString = """
                {
                  "query": {
                    "match": { "judgementText": "%s" }
                  },
                  "highlight": {
                    "pre_tags" : ["<b>"],
                    "post_tags" : ["</b>"],
                    "fields": {
                      "judgementText": {"fragment_size" : 200, "number_of_fragments" : 5}
                    }
                  }
                }
                """;

        queryString = String.format(queryString, searchTerm);

        return webClient.post()
                .uri("/sc-judgements/_search")
                .contentType(MediaType.APPLICATION_JSON) // Set Content-Type header
                .accept(MediaType.APPLICATION_JSON) // Set Accept header
                .bodyValue(queryString)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .map(responseMap -> {
                    // Extract the hits array from the response map
                    List<Map<String, Object>> hits = (List<Map<String, Object>>) ((Map<String, Object>) responseMap.get("hits")).get("hits");
                    // Remove the _source field from each hit
                    hits.forEach(hit -> hit.remove("_source"));
                    // Return only the modified hits array
                    return hits.stream().map(hit -> (Object) hit).collect(Collectors.toList());                });
    }


    public SupremeCourtData addNewJudgement(SupremeCourtData judgement) {
        return judgementRepository.save(judgement);
    }

    public List<SupremeCourtData> supremeCourtSearch(String text) {

        return judgementRepository.findByJudgementText(text);
    }
}
