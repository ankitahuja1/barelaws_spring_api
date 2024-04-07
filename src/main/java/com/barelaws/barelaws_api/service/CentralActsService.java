package com.barelaws.barelaws_api.service;

import com.barelaws.barelaws_api.model.CentralActsData;
import com.barelaws.barelaws_api.model.SupremeCourtData;
import com.barelaws.barelaws_api.repository.CentralActsRepository;
import com.barelaws.barelaws_api.repository.JudgementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CentralActsService {


    @Autowired
    private CentralActsRepository centralActsRepository;

    public List<CentralActsData> getAllActsData(){
        List<CentralActsData> sections = new ArrayList<>();
        centralActsRepository.findAll()
                .forEach(sections::add);
        return sections;
    }


    public List<CentralActsData> searchByActName(String actName){
        return new ArrayList<>(centralActsRepository.findByActName(actName));
    }

    public CentralActsData addNewSection(CentralActsData sectionData) {
        return centralActsRepository.save(sectionData);
    }

    public List<CentralActsData> centralActsSearch(String text){

        return centralActsRepository.findByActData(text);
    }
}
