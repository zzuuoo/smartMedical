package com.zuovx.Model;

/**
 * 预约
 */
public class Book {
    private int bookId;
    private int doctorId;
    private int patientId;
    private String bookTime;//预约时间
    private boolean isAvaliablity;//是否生效
    private boolean isCancle;//是否取消

    public boolean isAvaliablity() {
        return isAvaliablity;
    }

    public void setAvaliablity(boolean avaliablity) {
        isAvaliablity = avaliablity;
    }

    public boolean isCancle() {
        return isCancle;
    }

    public void setCancle(boolean cancle) {
        isCancle = cancle;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }
}
