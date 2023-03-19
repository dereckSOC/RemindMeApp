package com.example.myapplication;

public class Account {
    String username, email, phone, userid;

    public Account() {

    }

    public Account(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public Account(String username, String phone, String userid) {
        this.username = username;
        this.phone = phone;
        this.userid = userid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
