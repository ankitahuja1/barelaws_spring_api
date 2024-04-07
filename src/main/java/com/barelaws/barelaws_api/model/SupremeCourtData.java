package com.barelaws.barelaws_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "sc-judgements")
public class SupremeCourtData {


    @Id
    private String id;

    private String judgementText;

    private String dataSet;

    private Object highlight;

    public String getDataSet() {
        return dataSet;
    }

    public void setDataSet(String dataSet) {
        this.dataSet = dataSet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudgementText() {
        return judgementText;
    }

    public void setJudgementText(String judgementText) {
        this.judgementText = judgementText;
    }

    public Object getHighlight() {
        return highlight;
    }

    public void setHighlight(Object highlight) {
        this.highlight = highlight;
    }
}
