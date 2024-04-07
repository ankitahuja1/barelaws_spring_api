package com.barelaws.barelaws_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;

@Document(indexName = "central-acts")
public class CentralActsData {

    @Id
    private String id;

    private String enactmentDate;

    private String actNumber;

    private String actName;

    private String actLink;

    private String fileName;

    private ArrayList<String> actData;

    public ArrayList<String> getActData() {
        return actData;
    }

    public void setActData(ArrayList<String> actData) {
        this.actData = actData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnactmentDate() {
        return enactmentDate;
    }

    public void setEnactmentDate(String enactmentDate) {
        this.enactmentDate = enactmentDate;
    }

    public String getActNumber() {
        return actNumber;
    }

    public void setActNumber(String actNumber) {
        this.actNumber = actNumber;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActLink() {
        return actLink;
    }

    public void setActLink(String actLink) {
        this.actLink = actLink;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
