package com.zuovx.Model;

/**
 * 科室
 */
public class Section {
    private int sectionId;
    private String sectionName;
    private String sectionIntroduction;

    public String toString(){
        return sectionName;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSectionIntroduction() {
        return sectionIntroduction;
    }

    public void setSectionIntroduction(String sectionIntroduction) {
        this.sectionIntroduction = sectionIntroduction;
    }
}
