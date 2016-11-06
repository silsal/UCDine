package com.example.saorla.ucdfood;

//Users table. This contains information about every user on the app!
public class Users {
    private int _uid;
    private String username;
    private String first_name;
    private String surname;
    private String email;
    private int host_score;
    private int attendee_points;
    private int available_points;


    public Users(){

    }

    public Users(String username, String first_name, String surname, String email) {
        this.username = username;
        this.first_name = first_name;
        this.surname = surname;
        this.email = email;
    }

    public void set_uid(int _uid) {
        this._uid = _uid;
    }

    public void setUsername(String username) { this.username = username; }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHost_score(int host_score) {
        this.host_score = host_score;
    }

    public void setAttendee_points(int attendee_points) {
        this.attendee_points = attendee_points;
    }

    public void setAvailable_points(int available_points) { this.available_points = available_points; }

    public int get_uid() {
        return _uid;
    }

    public String getUsername() { return username; }

    public String getFirst_name() {
        return first_name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public int getHost_score() {
        return host_score;
    }

    public int getAttendee_points() {
        return attendee_points;
    }

    public int getAvailable_points() {
        return available_points;
    }
}
