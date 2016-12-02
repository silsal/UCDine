package com.example.saorla.ucdfood;

/*This class is used to create instances of Event, which are used to add rows to the events table in the database.
It contains setter and getter methods for each column in the table.
*/
public class Events {

    private int _eid;
    private int _hid;
    private int invite_num;
    private int available_num;
    private String event_name;
    private String address;
    private String description;
    private String time;
    private String date;
    private String event_pic;



    public Events(){

    }


    // create setter for events
    public void set_eid(int _eid) {
        this._eid = _eid;
    }
    public void set_hid(int _hid) {
        this._hid = _hid;
    }
    public void setInvite_num(int invite_num) {
        this.invite_num = invite_num;
    }
    public void setAvailable_num(int available_num) {this.available_num = available_num;}
    public void setEvent_name(String event_name) {this.event_name = event_name;}
    public void setAddress(String address) {
        this.address = address;
    }
    public void setDescription(String description) {this.description = description;}
    public void setTime(String time) {this.time = time;}
    public void setDate(String date) {
        this.date = date;
    }
    public void setEvent_pic(String event_pic) {this.event_pic = event_pic;}

    //  create getter for events

    public int get_eid() {
        return _eid;
    }
    public int get_hid() {
        return _hid;
    }
    public int getInvite_num() {
        return invite_num;
    }
    public int getAvailable_num() {return available_num;}
    public String getEvent_name() {return event_name;}
    public String getAddress() {
        return address;
    }
    public String getDescription() {return description;}
    public String getTime() {return time;}
    public String getDate() {
        return date;
    }
    public String getEvent_pic() {return event_pic;}


}
