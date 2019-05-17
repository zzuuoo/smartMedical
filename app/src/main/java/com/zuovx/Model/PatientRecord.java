package com.zuovx.Model;

import java.io.Serializable;

public class PatientRecord implements Serializable {

    private int patientRecordId;
    private int patientId;
    private int doctorId;
    private int scheduleId;
    private String admissionTime;//入院时间
    private String chief;//主诉
    private String nowHistory;//现病史
    private String pastHistory;//过去病史
    private String personalHistory;//个人史
    private String familyHistory;//家族病史
    private String physicalExamination;//体格检查
    private String therapeuticExamination;//辅助检查
    private String diagnosis;//初步/最后诊断

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getPatientRecordId() {
        return patientRecordId;
    }

    public void setPatientRecordId(int patientRecordId) {
        this.patientRecordId = patientRecordId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getAdmissionTime() {
        return admissionTime;
    }

    public void setAdmissionTime(String admissionTime) {
        this.admissionTime = admissionTime;
    }

    public String getChief() {
        return chief;
    }

    public void setChief(String chief) {
        this.chief = chief;
    }

    public String getNowHistory() {
        return nowHistory;
    }

    public void setNowHistory(String nowHistory) {
        this.nowHistory = nowHistory;
    }

    public String getPastHistory() {
        return pastHistory;
    }

    public void setPastHistory(String pastHistory) {
        this.pastHistory = pastHistory;
    }

    public String getPersonalHistory() {
        return personalHistory;
    }

    public void setPersonalHistory(String personalHistory) {
        this.personalHistory = personalHistory;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getPhysicalExamination() {
        return physicalExamination;
    }

    public void setPhysicalExamination(String physicalExamination) {
        this.physicalExamination = physicalExamination;
    }

    public String getTherapeuticExamination() {
        return therapeuticExamination;
    }

    public void setTherapeuticExamination(String therapeuticExamination) {
        this.therapeuticExamination = therapeuticExamination;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
