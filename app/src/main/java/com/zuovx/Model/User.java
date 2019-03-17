package com.zuovx.Model;

public class User {
    private int userId;
    private String password;//密码：6-12位
    private String account;//账号：6位以上，//手机号 唯一 外键
    private int userStatus;//1：病人，2：医生。0：管理员
    private String headPath;//头像路径

    public User(String account, String password,Integer status)
    {
        this.account=account;
        this.password=password;
        this.userStatus=status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getStatus() {
        return userStatus;
    }

    public void setStatus(int status) {
        this.userStatus = status;
    }

    public String getHead() {
        return headPath;
    }

    public void setHead(String headPath) {
        this.headPath = headPath;
    }
}
