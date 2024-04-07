package com.barelaws.barelaws_api.repository;

import com.barelaws.barelaws_api.model.CentralActsData;
import com.barelaws.barelaws_api.model.SupremeCourtData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CentralActsRepository extends ElasticsearchRepository<CentralActsData, String> {

    List<CentralActsData> findByActData(String text);

    List<CentralActsData> findByActName(String text);
}
