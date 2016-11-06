package com.example.saorla.ucdfood;


public class Friends {

    private int _uid;
    private int _friendid;

    public Friends(){

    }

    public Friends(int _uid) {
        this._uid = _uid;
    }

    public int get_uid() {
        return _uid;
    }

    public int get_friendid() {
        return _friendid;
    }

    public void set_uid(int _uid) {
        this._uid = _uid;
    }

    public void set_friendid(int _friendid) {
        this._friendid = _friendid;
    }
}
