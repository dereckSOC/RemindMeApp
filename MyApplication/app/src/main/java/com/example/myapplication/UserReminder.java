package com.example.myapplication;

public class UserReminder {
    private String title;
    private String notes;
    private String date;
    private String location;
    public UserReminder () {

    }

    public UserReminder(String date, String title, String notes) {
        this.date = date;
        this.title = title;
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
