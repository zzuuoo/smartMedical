package com.zuovx.Model;

import java.io.Serializable;

public class Doctor implements Serializable {
    private int doctorId;
    private String account;//账号,6位以上,外键，手机作为账号
    private String name;//名字
    private int sex;//1->男或0->女
    private int age;//年龄,大于等于0，以年为单位
    private String idNumber;//身份证号码
    private String phone;//电话
    private String address;//地址


    private String introduction;//医生简介
    private String forte;//特长
    private int sectionId;//属于哪个科室


    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getForte() {
        return forte;
    }

    public void setForte(String forte) {
        this.forte = forte;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
