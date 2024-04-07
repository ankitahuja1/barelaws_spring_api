package com.barelaws.barelaws_api.controller;

import com.barelaws.barelaws_api.model.CentralActsData;
import com.barelaws.barelaws_api.service.CentralActsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/v1/central-acts")
public class CentralActsController {

    @Autowired
    private CentralActsService centralActsService;

    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    @GetMapping("/")
    public List<CentralActsData> getAllSections() { return centralActsService.getAllActsData(); }


    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    @PostMapping("/search-by-act-name")
    public List<CentralActsData> getActs(@Valid @RequestBody String searchText) {
        return centralActsService.searchByActName(searchText);
    }


    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    @PostMapping(value = "/search-by-act-data")
    public List<CentralActsData> getSectionsByText(@Valid @RequestBody String searchText) {
        return centralActsService.centralActsSearch(searchText);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @CrossOrigin
    @PostMapping("/")
    public CentralActsData addNewSection(@Valid @RequestBody CentralActsData centralAct) {
        return centralActsService.addNewSection(centralAct);
    }

}