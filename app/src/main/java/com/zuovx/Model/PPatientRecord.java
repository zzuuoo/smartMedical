package com.zuovx.Model;

import java.io.Serializable;

public class PPatientRecord implements Serializable {
    private Patient patient;
    private PatientRecord patientRecord;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public PatientRecord getPatientRecord() {
        return patientRecord;
    }

    public void setPatientRecord(PatientRecord patientRecord) {
        this.patientRecord = patientRecord;
    }
}
