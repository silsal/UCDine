package com.example.saorla.ucdfood;

//Events table. This creates instances of Event, which can be added to the DB by all users.
public class Events {

    private int _eid;
    private int _hid;
    private int invite_num;
    private String address;
    //private int time;
    //private int date;


    public Events(){

    }

    public Events(int _eid) {
        this._eid = _eid;
    }

    public void set_eid(int _eid) {
        this._eid = _eid;
    }

    public void set_hid(int _hid) {
        this._hid = _hid;
    }

    public void setInvite_num(int invite_num) {
        this.invite_num = invite_num;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int get_eid() {
        return _eid;
    }

    public int get_hid() {
        return _hid;
    }

    public int getInvite_num() {
        return invite_num;
    }

    public String getAddress() {
        return address;
    }
}
