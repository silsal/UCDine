package com.example.saorla.ucdfood;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//This class is responsible for creating the database

public class MyDBHandler extends SQLiteOpenHelper{
    private final String EV_LOG = "YOU CAN DO IT!";

    private static final String DB_TAG = "Please work!";
    //Name and version of db
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "foodshareDB.db";
    //Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_EVENTS = "events";
    public static final String TABLE_MY_EVENTS = "my_events";

    //common column names
    public static final String COLUMN_USER_ID = "_uid";
    //users table column names
    public static final String COLUMN_USERNAME = "uname";
    public static final String COLUMN_FIRST_NAME = "fname";
    public static final String COLUMN_SURNAME = "sname";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASS = "pass";
    public static final String COLUMN_HOST_SCORE = "host_score";
    public static final String COLUMN_ATTENDEE_POINTS = "attendee_points";
    public static final String COLUMN_AVAILABLE_POINTS = "available_points";
    public static final String COLUMN_COURSE = "course";
    public static final String COLUMN_BIO = "bio";
    public static final String COLUMN_PROFILE_PIC = "user_pic";

    //events table column names
    public static final String COLUMN_EVENT_ID = "_eid";
    public static final String COLUMN_HOST_ID = "_hid";
    public static final String COLUMN_INVITE_NUM = "invite_num";
    public static final String COLUMN_AVAILABLE_INVITE_NUM = "available_num";
    public static final String COLUMN_EVENT_NAME = "event_name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_EVENT_PIC = "event_pic";

    //My events table column names
    public static final String COLUMN_MY_EVENT_ID = "_myeid";
    public static final String COLUMN_EVENT_ATTENDED_ID = "_myaid";


    SQLiteDatabase db;

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + TABLE_USERS + "( " +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY NOT NULL," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_FIRST_NAME + " TEXT," +
                COLUMN_SURNAME + " TEXT," +
                COLUMN_LOCATION + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_PASS + " TEXT NOT NULL,"+
                COLUMN_HOST_SCORE + " INTEGER DEFAULT 0," +
                COLUMN_COURSE + " TEXT," +
                COLUMN_BIO + " TEXT," +
                COLUMN_ATTENDEE_POINTS + " INTEGER DEFAULT 3," +
                COLUMN_AVAILABLE_POINTS + " INTEGER DEFAULT 3," +
                COLUMN_PROFILE_PIC + " TEXT" +
                " );";

        String query2 = "CREATE TABLE " + TABLE_EVENTS + " ( " +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY," +
                COLUMN_HOST_ID + " INTEGER NOT NULL," +
                COLUMN_INVITE_NUM + " INTEGER NOT NULL CHECK(invite_num > 0)," +
                COLUMN_AVAILABLE_INVITE_NUM + " INTEGER DEFAULT 0," +
                COLUMN_EVENT_NAME + " TEXT," +
                COLUMN_ADDRESS + " TEXT NOT NULL," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_TIME + " TEXT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_EVENT_PIC + " TEXT " +
        " );";

        String query3 = "CREATE TABLE " + TABLE_MY_EVENTS + " ( " +
                COLUMN_MY_EVENT_ID + " INTEGER NOT NULL," +
                COLUMN_EVENT_ATTENDED_ID+ " INTEGER NOT NULL," +
                "PRIMARY KEY" + "(" + COLUMN_MY_EVENT_ID + ", " +  COLUMN_EVENT_ATTENDED_ID+ ")" +
                " );";


        //create the required tables
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        //some default users for the prototype
        String user1 = "Insert into users(_uid,uname,fname,sname,email,pass) values(1, 'coffeeman','david', 'coyle', 'davo@ucdconnect.ie', 'iloveandroid')";
        String user2 = "Insert into users(_uid,uname,fname,sname,email,pass) values(2, 'tragedy','silvia', 'saloni', 'saloni@ucdconnect.ie', 'anotherknife')";
        db.execSQL(user1);
        db.execSQL(user2);
        //some default events for the prototype
        String event1 = "Insert into events(_eid, _hid, invite_num, available_num, address, event_name, time, date, description) values(1,1,5,5,'31 Stillorgan Road', 'Italian Pizza Night','20:00','2016-12-02', 'It''s the end of term, and I feel like celebrating! Come join myself and my roommates for some homemade Italian pizzas - meat eaters and vegetarians welcome!')";
        String event2 = "Insert into events(_eid, _hid, invite_num, available_num, address, event_name, time, date, description) values(2,2,10,7,'18 Pearse Square, Dublin 2', 'It''s Thai Time','19:30','2016-12-03', 'So, my roommate is about to sit his culinary final exam, and needs some guinea pigs. On Friday he will be making a load of test dishes, and anyone who wants to help us eat them is welcome.')";
        String event3 = "Insert into events(_eid, _hid, invite_num, available_num, address, event_name, time, date, description) values(3,1,6,3,'31 Stillorgan Road', 'Feeling Dessert-ed','21:00','2016-12-13', 'Nearly all of my roommates have gone home for Christmas, and the fridge is full of eggs and milk, which can only mean one thing - cake time! Come join the last two standing for an evening of pancakes, lemon drizzle and tiramisu!')";
        String event4 = "Insert into events(_eid, _hid, invite_num, available_num, address, event_name, time, date, description) values(4,2,4,4,'18 Pearse Square, Dublin 2', 'Sunday Roast', '15:00','2016-12-04', 'Ever since moving out from my parents house, I''ve missed the proper Sunday dinner. So, this week I''m going to host my own! If you''re around, come on over for turkey, veggies, and all the stuffing you can eat!')";
        db.execSQL(event1);
        db.execSQL(event2);
        db.execSQL(event3);
        db.execSQL(event4);

        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_EVENTS);
        this.onCreate(db);
    }

    /**
     * Method to insert user object into database
     * this method is promted by signin.java
     */
    public void insertUsers(Users u){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from users";
        Cursor cursor = db.rawQuery(query,null);
        int count  = cursor.getCount() + 1;
        Log.i(DB_TAG, "THIS IS THE COUNTER "+count);
        values.put(COLUMN_USER_ID, count);
        values.put(COLUMN_FIRST_NAME, u.getFname());
        values.put(COLUMN_SURNAME, u.getSname());
        values.put(COLUMN_USERNAME, u.getUname());
        values.put(COLUMN_LOCATION, u.getLocation());
        values.put(COLUMN_EMAIL, u.getEmail());
        values.put(COLUMN_PASS,u.getPass());
        values.put(COLUMN_COURSE,"CompSci");
        values.put(COLUMN_BIO,"Tell the UCDine world a little bit about yourself");
        values.put(COLUMN_PROFILE_PIC, "1");
//        values.put(COLUMN_PROFILE_PIC, " /9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAMiKlq+Wfcivo6/h1cju////////////////////////////////////////////////////////////////////2wBDAdXh4f//////////////////////////////////////////////////////////////////////////////////wAARCAG6AboDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwCOiiigAooooAKKKKACiiigAooooAKKWigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigApaSigAooooAKKKKACiiigAooooAKKKKACiiigApKWigAAzSEYNHSjrQAUUUUAFFFFABRRRQAUtJRQAUUUUAFFFFABRRRQAUtFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFMAooopAFFFFABRRS0AJRS0UAJRS0lABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFMApMUtFACUUYopAFFFFABRRRQAtFFFMApKWkoAKKKMUgFopKWgApKKKACilFIaBC0UlFAxaSilFACUopTTaAFopKKAFNJSinUCG0UHrSgUDDFFLRigBtFO2ikKkUAJRRRTAKKKM0AFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFACdDSkiikxSsAUtJRmgAooooAWkooxQACnU2igVgoxQBS0DExRilopgJmilopAGKKKWmAlGKWigBOaKWigAooooAQ0ozSgUtIBMc0uKKWkAYooooGBpuadSEUAJ1pMUuKbTEJTsUlKDQAlFB5pKAFozSU7FACUUd6KYBRRRmgAooooAKKKKACiiigAooooAKKKKACiiigAooooAKTFLRQAlFApaQBRRRTAKKKKACiiigAooooAKKKKACiiigApaSigBaKSloAKBRSikAtFFAHNACgUuKUUUAJijFLRQAYoIpaKAG4ppWpKSgCLFJinng000AJS0UUwEpQ3FFJikAh5ozS0UAJS4pKcDxQA2lpD1paACijNFMAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiijNABS0maM0gFpAMmkpynBoACpFJmnswI4pmKADNKvSkxS9qAFzTkplPWgB1FFFABRSUUALRSUUALRSUUADdKjJqQ1EaAFopKWmAUUlFAC0UUUAIRRilooATFJTqQ0AGKSlBxQeaQBRSUuaACikzS0wCiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKWkzRmkMDSoMmm0UCJHAxUYpSSaBQAYpaKKBiYpaKKACiinbQeh5oAbTlpvSnqKQC0UUE4piClpMikzQMWlpuaM0AOopuaNwoAWoz1qTrTcDcc0AMxSU6kIoEAGaUqRQpwacxGKAG0UmaWmAUUUUAFFFFACYoxS0UAIaMcUUZpAGOKQUuaSgSFopM0UDFopM0tMAooooAKKKKACiiigAooooAKKKKAFpKWkpDBRk04pxxTVbBp5cYoER4paBS0DCiiigAooooAKKKKAFFKBxSCl7UAB60+mgcUtIANMJp5FNKimA3IopdooxQACg04ChhQAygEUuKAooAUGhhzSgCg0gGnrSUdzRTATFFLRQAmKBzS0g4NAhSpFJT2YYpgoAWiiimAUUUUhhSUtFMQlFFFACGlAopOlIGBpRSUA0ALRRmimAUUUUAFFFFABRRRQAUUUUDCiikNIApcUlLQIWiilAzQMSinHpTaACiiigAooooAKfio6ercYoAd2oozRSAKKKTNAC0nGaQmgHHWmA+im7qN1AC8UYppOcUA0AOpKM0hOKAG0UUUwCiiikAUUUUwEpKdSYzSELRSdKKAFooooGFFFFACUUUUxBRRRQAUhpaQ0gDFFANFAC0UgpaYBRRRSAKKKKACiiigYGnIoPNNNAYigQ51GM00UFi1FAC06mjrT6BjWpKDRQAUlFFAC0UlFMQUUUlIYueakqKpB0FACk000tBpAJS4o+lJzTACKAKXBo5oAMcUlGTmigBRTW606mHk0ALRRRQAUUUUxBRRRSGIadHSUlAhz9aSkHWloAKKKKYwooopAJRS0lABRRRTEFFFFABikxS0UgEozRS4oASlpKWgAooooGFFFFABSGlpO9AMBS0bTQKAFHWnHpTaUnigBKKKSgAooooEFFFFABRRRQAlSD7opgGTUi/doASig0UDFFFFFABxRxSUYoAWg0UhoAQnim0E5NAoAWiiimIKKKKACiiigApM0dSKkKjFICMUUUUALRRRTGFFFFIAooooASiiigAooopiCiiigBDRmlopAJS02loAWiiigYUUUUAFC8HmijGaAJcjFRHlqTJ6UCgQ6ikpaBhSUtJQAUUUUAFFFFAgoopwHrQAKOKcvSkoBwaAFNNpxpDQMUGjNNozQIdRmmZozQMfmmnpQOaVh8tAhpGelJ0pwpGBPNABSZpOaVRk0AGaWkYYNJQA4AmkOQacjY60jHJoAb3pxYmkpaAEpaKKBhRRRQAUUUuOKAEoopKACiiigAooopgFFFFABRRRQAmKWkpaQBRRSUALRRRQAU5elNpy9KAGuKSlfrSx4oEN5FKKkYDFRigBaSlpKBhRRSgZoASlC5607AFHWgQZA6UUGigApDRmigAB7UtJQDQAUlOooAbRS4pQKAAChvu0tNagAFKKQUtAC0YoooAQqDRtFLRQA3aaTGKfRQAyilK+lJQMKKKKACiiigBRQTSUUAFJRRQAUUUUwCiiigAooooAKKKKAEooopAFFFFABS0lFAC05abSrQApGabgg0+igBhYmkBp5XNMxQIdQBmlVPWnfhQO4gWlozRQISlopKACijNHWgApKKKACkpaKAAUtJSigAoFLRQAU1qdTW60AGKXOBzTdwHFIxyMUAPpajDEU8HIoAKWkooAWk70GigBSeKa3WlooAbilxilppNAwoopKAFpKKKACiiimAUUUUAFFFFABRRRQAUUlLSASiiigAooooAKKKKACnrTKcOtADqKKKBhSKOc0tLQJhmjNFIRQIDzSA4 4NGfWlPNABRSD0paACiiigBDRu9aWk+tACFvSm/SnYoxQAm40u6kIpKAJFYHinVDTgTQA88Uw0ZpKAENLig04dKAG4pwGKKKAFpaSigAooo7UAHegUmaUUAB6Uyn00jFACUUUUxhRRRQAUUUUAFFFFABRRRSAKKKKAEozRRigQUUUUAFFFFAwooooAKUUlLQA8UU0HFOoGFFBooEwpaSigQYpOlLRQAhGelANFHQ0AB60tIaAaAFpKWkoAQUuKCKQGgBab0NOpCKAEIoAxSjpRigA");

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    /**
     * Method to return password from database
     * given a username
     */
    public String searchPass(String uname){
        db = this.getReadableDatabase();
        String query = "select uname, pass from "+TABLE_USERS;
        Cursor cursor = db.rawQuery(query,null);
        String a,b;
        b = "not found";
        if (cursor.moveToFirst()){
            do{
                a = cursor.getString(0);
                if (a.equals(uname)){
                    b = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        db.close();
        return b;
    }

    /**
     * Method to return user_id from database
     * given a username
     */
    public String searchId(String uname) {
        db = this.getReadableDatabase();
        String query = "select uname, _uid from " + TABLE_USERS;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        b = "not found";
        if (cursor.moveToFirst()) {
            do {
                a = cursor.getString(0);
                if (a.equals(uname)) {
                    b = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        db.close();
        return b;
    }


    //add event to Events table
    public void addEvent(Events events){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String queryEvents = "select * from events";
        Cursor cursor = db.rawQuery(queryEvents,null);
        int countevent  = cursor.getCount() + 1;

        values.put(COLUMN_EVENT_ID, countevent);
        values.put(COLUMN_HOST_ID, events.get_hid());
        values.put(COLUMN_INVITE_NUM, events.getInvite_num());
        values.put(COLUMN_AVAILABLE_INVITE_NUM, events.getAvailable_num());
        values.put(COLUMN_EVENT_NAME, events.getEvent_name());
        values.put(COLUMN_ADDRESS, events.getAddress());
        values.put(COLUMN_DESCRIPTION, events.getDescription());
        values.put(COLUMN_TIME, events.getTime());
        values.put(COLUMN_DATE, events.getDate());
        values.put(COLUMN_EVENT_PIC, events.getEvent_pic());

        //SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public void  getPoints(int userid) {
        db = this.getWritableDatabase();

        int new_score = 3;
        db.execSQL("update " + TABLE_USERS + " set available_points = available_points +" + new_score + " where _uid =" + userid+";");


        db.close();

    }
    public void  reducePoints(int userid) {
        db = this.getWritableDatabase();

        int reduce_score = 1;
        db.execSQL("update " + TABLE_USERS + " set available_points = available_points -" + reduce_score + " where _uid =" + userid+";");


        db.close();

    }

    public void  reduceAvailableNumber(int eventid) {
        db = this.getWritableDatabase();
        int reduce_attending = 1;
        db.execSQL("update " + TABLE_EVENTS + " set available_num = available_num -" + reduce_attending + " where _eid =" + eventid+";");
        db.close();
    }
    public int EventSize() {
        db = this.getReadableDatabase();
        String query = "select * from "+TABLE_EVENTS;
        Cursor cursor = db.rawQuery(query,null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;

    }
    //method which joins the Users table and Events table on userid
    public ArrayList<String[]> selectHostName(){
        db = this.getReadableDatabase();
        //get the host id from Events
        String query = "select _hid from "+TABLE_EVENTS+";";
        //get the number of events in the database
        int s = EventSize();
        Cursor c = db.rawQuery(query,null);
        int count = 0;
        String [] hid = new String[s];
        if (c.moveToFirst()){
            do{
                hid[count]=c.getString(c.getColumnIndex("_hid"));
                count++;
            }while(c.moveToNext());

        }
        String [] unames = new String [s];
        String [] avail_points = new String [s];
        int cnt =0;
        //find the userid in the Users table which matches the host id from the Events table
        for(int i=0;i<hid.length;i++){
            String query2= "select uname, available_points from "+TABLE_USERS+ " where _uid == "+hid[i];
            Cursor c2 = db.rawQuery(query2,null);

            if(c2.moveToFirst()){
                do{
                    unames[cnt]=(c2.getString(c2.getColumnIndex("uname")));
                    avail_points[cnt]=Integer.toString(c2.getInt(c2.getColumnIndex("available_points")));
                    cnt++;
                }while(c2.moveToNext());

            }
        }

        db.close();
        Log.i(EV_LOG, "Usernames");
        ArrayList<String[]> userDetails = new ArrayList<String[]>();
        userDetails.add(unames);
        userDetails.add(avail_points);
        return userDetails;

    }

    //method which returns the details of each event from the Events table in arrays
    public ArrayList<String[]>  eventinfostr(){
        db = this.getReadableDatabase();
        int i = EventSize();

        ArrayList<String[]> eventinfoString = new ArrayList<String[]>();

        String [] event_name = new String[i];
//        String [] host_name = new String[i];
        String [] event_time = new String[i];
        String [] event_date = new String[i];
        String [] event_description = new String[i];
        String [] event_address = new String[i];

        String[] invite_num = new String[i];
        String[] avail_num = new String[i];
        String[] eid = new String[i];

        String query = "select event_name, time, date, description, address, invite_num, available_num, _eid from "+TABLE_EVENTS;
        Cursor result = db.rawQuery(query,null);
        int count = 0;
        if (result.moveToFirst()){
            do{
                event_name[count]=result.getString(result.getColumnIndex("event_name"));
                event_time[count]=result.getString(result.getColumnIndex("time"));
                event_date[count]=result.getString(result.getColumnIndex("date"));
                event_description[count]=result.getString(result.getColumnIndex("description"));
                event_address[count]=result.getString(result.getColumnIndex("address"));
                invite_num[count]= Integer.toString(result.getInt(result.getColumnIndex("invite_num")));
                avail_num[count]= Integer.toString(result.getInt(result.getColumnIndex("available_num")));
                eid[count]= Integer.toString(result.getInt(result.getColumnIndex("_eid")));


                count++;
            }while (result.moveToNext());
        }
        db.close();
        ArrayList<String[]> userDetails = selectHostName();

        eventinfoString.add(event_name);
        //host name
        eventinfoString.add(userDetails.get(0));
        eventinfoString.add(event_time);
        eventinfoString.add(event_date);
        eventinfoString.add(event_description);
        eventinfoString.add(event_address);
        eventinfoString.add(invite_num);
        eventinfoString.add(avail_num);
        eventinfoString.add(eid);
        //avail points
        eventinfoString.add(userDetails.get(1));

        return eventinfoString;
    }

    //General "Select" fuunction
    public String databaseSelectByIDToString(String TableName, String ColumnName, int WhereValueEquals){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + ColumnName +" FROM " + TableName + " WHERE " + COLUMN_USER_ID +" == " + WhereValueEquals + ";";

        //cursor
        Cursor cursor = db.rawQuery(query, null);
        //get first row in the results
        cursor.moveToFirst();


        dbString += cursor.getString(0);

        if (dbString.length() ==0 ){
            dbString = "Empty";
        }
        db.close();
        return dbString;
    }


    //General "Update" function
    public void databaseUpdateByIDToString(String TableName, String ColumnName, String UpdateValue, int UserId){
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TableName +" SET " + ColumnName + " = '" + UpdateValue + "' WHERE " + COLUMN_USER_ID +" = " + UserId + ";";

        db.execSQL(query);

        db.close();
    }

    //General "Count" function
    public String databaseCountByIDToString(String TableName, String CountColumnName, String WhereColumnName, int WhereEqualsValue){
        String dbCountString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT count(" + CountColumnName + ") FROM " + TableName + " WHERE " + WhereColumnName + " = " + WhereEqualsValue + ";";

        //cursor
        Cursor cursor = db.rawQuery(query, null);
        //get first row in the results
        cursor.moveToFirst();


        dbCountString += cursor.getString(0);

        if (dbCountString.length() ==0 ){
            dbCountString = "27";
        }
        db.close();
        return dbCountString;
    }

    //Specific "Select" multiples into Array function
    public String[] databaseSelectByIDToArray(String TableName, String ColumnNameSelect, String ColumnNameEquals,int WhereValueEquals){


//        String[] dbString = {};
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + ColumnNameSelect +" FROM " + TableName + " WHERE " + ColumnNameEquals +" = " + WhereValueEquals + ";";

        Cursor resultRows = db.rawQuery(query, null);
        //Find the number of rows in result
        resultRows.moveToLast();
        int len = resultRows.getPosition();

        //Create empty array of length same as number rows
        String [] dbString = new String[len+1];

        //move to first row in the results
        resultRows.moveToFirst();

        //Set Counter
        int pos = 0;
        if (resultRows.moveToFirst()){
            do{
                dbString[pos] = resultRows.getString(0);
//            dbString[pos] = "7";
                pos ++;
            }
            while (resultRows.moveToNext());
        }
        db.close();
        return dbString;
    }

    //Specific "Select" multiples into Array function
    public String[] databaseSelectJoinByIDToArray(String Table_1_Name, String ColumnNameSelect, String Table_2_Name, String Column_1_NameEquals, String Column_2_NameEquals){
        String[] dbString = {};
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + ColumnNameSelect +" FROM " + Table_1_Name + " INNER JOIN " + Table_2_Name + " ON " + Table_1_Name+ "." +Column_1_NameEquals +" == " + Table_2_Name+ "." +Column_2_NameEquals + ";";

        Cursor cursor = db.rawQuery(query, null);
        //get first row in the results
        cursor.moveToFirst();
        int pos = 0;
        //move through the whole table
        while(!cursor.isAfterLast()){
            if(cursor.getString(pos)!=null){
                dbString[0] = cursor.getString(pos);
                pos +=1;
            }
            cursor.moveToNext();
        }

        db.close();
        return dbString;
    }


}
