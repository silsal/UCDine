package com.example.saorla.ucdfood;
/**
 * This file contains the user class which creates
 * instances of user in the user table for the database
 */

public class Users {

    private String uname;
    private String fname;
    private String sname;
    private String location;
    private String email;
    private String pass;
    private int host_score;
    private int attendee_points;
    private int available_points;
    private String course;
    private String bio;
    private String user_pic;

    //Methods to set attributes

    public void setUname(String uname){
        this.uname = uname;
    }
    public void setFname(String fname){
        this.fname = fname;
    }
    public void setSname(String sname){
        this.sname = sname;
    }
    public void setLocation(String location) {this.location = location;}
    public void setEmail(String email) { this.email=email;   }
    public void setPass(String pass){
        this.pass = pass;
    }
    public void setHost_score(int host_score) {
        this.host_score = host_score;
    }
    public void setAttendee_points(int attendee_points) {
        this.attendee_points = attendee_points;
    }
    public void setAvailable_points(int available_points) {this.available_points = available_points;}
    public void setCourse(String course) {this.course = course;}
    public void setBio(String bio) {this.bio = bio;}
    public void setUser_pic(String user_pic) {this.user_pic = user_pic;}


    //Get Methods

    public String getUname () {
        return this.uname;
    }
    public String getFname() {
        return this.fname;
    }
    public String getSname() {
        return this.sname;
    }
    public String getLocation() {return location;}
    public String getEmail() {
        return this.email;
    }
    public String getPass(){
        return this.pass;
    }
    public int getHost_score() {
        return this.host_score;
    }
    public int getAttendee_points() {
        return this.attendee_points;
    }
    public int getAvailable_points() {
        return this.available_points;
    }
    public String getCourse() {return course;}
    public String getBio() {return bio;}
    public String getUser_pic() {return user_pic;}
}
