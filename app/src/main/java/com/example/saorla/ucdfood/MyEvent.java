package com.example.saorla.ucdfood;

/*This class is used to create instances of MyEvent, which are used to add rows to the personal events table in the database.
It contains setter and getter methods for each column in the table.
*/
public class MyEvent {

    private int myeid;
    private int myaid;

    //Methods to set attributes
    public void setMyeid(int myeid) {this.myeid = myeid;}
    public void setMyaid(int myaid) {this.myaid = myaid;}

    //Methods to get the attributes
    public int getMyeid() {return myeid;}
    public int getMyaid() {return myaid;}
}
