package com.example.saorla.ucdfood;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

//This class is responsible for creating the database

public class MyDBHandler extends SQLiteOpenHelper{
    private static final String DB_TAG = "Please work!";
    //Name and version of db
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "foodshare.db";
    //Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_EVENTS = "events";
    public static final String TABLE_FRIENDS = "friends";
    public static final String TABLE_FAVOURITERECIPES = "favouriterecipes";
    //common column names
    public static final String COLUMN_USER_ID = "_uid";
    //users table column names
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_HOST_SCORE = "host_score";
    public static final String COLUMN_ATTENDEE_POINTS = "attendee_points";
    public static final String COLUMN_AVAILABLE_POINTS = "available_points";
    //events table column names
    public static final String COLUMN_EVENT_ID = "_eid";
    public static final String COLUMN_HOST_ID = "_hid";
    public static final String COLUMN_INVITE_NUM = "invite_num";
    public static final String COLUMN_EVENT_NAME = "event_name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";
    //friends table column names
    public static final String COLUMN_FRIEND_ID = "_friendid";
    //favourite recipes column names
    public static final String COLUMN_RECIPE_ID = "_recipeid";
    public static final String COLUMN_RECIPE_URL = "recipe_url";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + TABLE_USERS + "( " +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_FIRST_NAME + " TEXT," +
                COLUMN_SURNAME + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_HOST_SCORE + " INTEGER DEFAULT 0," +
                COLUMN_ATTENDEE_POINTS + " INTEGER DEFAULT 3," +
                COLUMN_AVAILABLE_POINTS + " INTEGER DEFAULT 3" +
                " );";

        String query2 = "CREATE TABLE " + TABLE_EVENTS + " ( " +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY," +
                COLUMN_HOST_ID + " INTEGER NOT NULL," +
                COLUMN_INVITE_NUM + " INTEGER NOT NULL CHECK(invite_num > 0)," +
                COLUMN_EVENT_NAME + " TEXT," +
                COLUMN_ADDRESS + " TEXT NOT NULL" +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_TIME + " TEXT," +
                COLUMN_DATE + " TEXT," +
                " );";

        String query3 = "CREATE TABLE " + TABLE_FRIENDS + " ( " +
                COLUMN_USER_ID + " INTEGER NOT NULL," +
                COLUMN_FRIEND_ID + " INTEGER NOT NULL," +
                "PRIMARY KEY" + "(" + COLUMN_USER_ID + ", " + COLUMN_FRIEND_ID + ")" +
                " );";

        String query4 = "CREATE TABLE " + TABLE_FAVOURITERECIPES + " ( " +
                COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY," +
                COLUMN_USERNAME + " INTEGER NOT NULL," +
                COLUMN_RECIPE_URL + " TEXT" +
                " );";

        //create the required tables
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITERECIPES);
        onCreate(db);
    }

    //add user to users table
    public void addUser(Users users){
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, users.getUsername());
        values.put(COLUMN_FIRST_NAME, users.getFirst_name());
        values.put(COLUMN_SURNAME, users.getSurname());
        values.put(COLUMN_EMAIL, users.getEmail());
        SQLiteDatabase db = getWritableDatabase();
        Log.i(DB_TAG, "Adding to DB " + values + "");
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    //delete user from users table
    public void deleteUser(String username){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=\"" + username + "\";");
    }

    //add event to Events table
    public void addEvent(Events events){
        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_ID, events.get_eid());
        values.put(COLUMN_HOST_ID, events.get_hid());
        values.put(COLUMN_INVITE_NUM, events.getInvite_num());
        values.put(COLUMN_EVENT_NAME, events.getEvent_name());
        values.put(COLUMN_ADDRESS, events.getAddress());
        values.put(COLUMN_DESCRIPTION, events.getDescription());
        values.put(COLUMN_TIME, events.getTime());
        values.put(COLUMN_DATE, events.getDate());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    //delete event from Events table
    public void deleteEvent(){

    }

    //add friend to Friends table
    public void addFriend(){

    }

    //delete friend from Friends table
    public void deleteFriend(){

    }

    //add recipe to FavouriteRecipes table
    public void addRecipe(){

    }

    //delete recipe from FavouriteRecipes table
    public void deleteRecipe(){

    }

    //test database by printing to string
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + ";";

        //cursor
        Cursor cursor = db.rawQuery(query, null);
        //get first row in the results
        cursor.moveToFirst();
        //move through the whole table
        while(!cursor.isAfterLast()){
            if(cursor.getString(cursor.getColumnIndex("username"))!=null){
                dbString += cursor.getString(cursor.getColumnIndex("username"));
                dbString += "\n";
            }
            cursor.moveToNext();
        }
        db.close();
        return dbString;
    }
}
