package com.barelaws.barelaws_api.controller;


import com.barelaws.barelaws_api.model.SupremeCourtData;
import com.barelaws.barelaws_api.repository.JudgementRepository;
import com.barelaws.barelaws_api.service.JudgementServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/v1/judgements")
public class JudgementController {

    @Autowired
    private JudgementServices judgementServices;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/supreme-court")
    public List<SupremeCourtData> getAllJudgements() { return judgementServices.getAllSupremeCourtJudgements(); }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/supreme-court/search-by-id")
    public Optional<SupremeCourtData> getJudgementsById(@Valid @RequestBody String searchText) {
        return judgementServices.findBySupremeCourtId(searchText);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/supreme-court/search-by-text")
    public List<SupremeCourtData> getJudgementByText(@Valid @RequestBody String text) {
        return judgementServices.supremeCourtSearch(text);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/supreme-court/search-highlights-by-text")
    public Mono<Object>  getJudgementByTextV2(@Valid @RequestBody String text) {
        return judgementServices.searchDocuments(text);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/supreme-court/search-by-id-and-text", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SupremeCourtData> getJudgementByText(@Valid @RequestBody HashMap<String, String> searchData) {
        return judgementServices.supremeCourtSearchIdText(searchData.get("id"), searchData.get("text"));
    }


    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value="/supreme-court", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SupremeCourtData addNewJudgement(@Valid @RequestBody SupremeCourtData judgement) {
        return judgementServices.addNewJudgement(judgement);
    }

}
