package com.zuovx.Model;

import java.io.Serializable;

public class BookPatientSche implements Serializable {
    private Book book;
    private Patient patient;
    private Schedule schedule;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
