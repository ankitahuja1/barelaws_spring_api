package com.barelaws.barelaws_api.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "acts_data")
public class ActEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(name="enactment_date")
    private String enactmentDate;

    @Column(name="act_number")
    private String actNumber;

    @Column(name="act_name")
    private String actName;

    @Column(name="act_link")
    private String actLink;

    @Column(name="file_name")
    private String fileName = null;


    public int getId() {
        return id;
    }

    public void setId(int id) {
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
