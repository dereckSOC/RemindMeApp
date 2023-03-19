package com.example.remindme;

public class Account {
    String username, email, phone;

    public Account() {

    }

    public Account(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
