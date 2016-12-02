package com.example.saorla.ucdfood;

//Created by Paudi on 09/11/2016.

//This Class constructs the Profile Activity, making calls to the database to retrieve values to be populated in relevant text and image views
//It incorporates a number of fragments for different same sections of information, "Events" Activities" and "Reviews".
//A on-click button allows the user transition to "Edit Profile" Activity.
//The action Bar has a number of "quick-links" that scroll the view to the relevant information section.
//The action-bar also has a "drop-down" menu to access the other activities in the app

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.view.Gravity;
import android.content.SharedPreferences;
//For camera
import android.widget.ImageView;
import android.graphics.Bitmap;
//From Database Handler
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_AVAILABLE_POINTS;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_BIO;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_COURSE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_EMAIL;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_HOST_ID;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_HOST_SCORE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_MY_EVENT_ID;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_PROFILE_PIC;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_USERNAME;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_EVENTS;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_MY_EVENTS;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_USERS;


public class ProfileActivity extends AppCompatActivity {
    //Initialise Variables
    //Views
    private int userID;
    ImageView userDeetsPic;
    TextView userDeetsName;
    TextView userDeetsEmail;
    TextView userDeetsCourse;
    TextView userDeetsPoints;
    TextView userDeetsEvents;
    TextView userDeetsRanking;
    TextView userDeetsBio;
    //Values
    String user_name;
    String user_email;
    String user_course;
    String user_points;
    String user_events;
    String user_ranking;
    String user_bio;

    //Database Helper
    MyDBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create the activity view
        setContentView(R.layout.activity_profile);
        //Describe the transitions when entering / exiting activity view
        overridePendingTransition(R.anim.slide_in_profile, R.anim.slide_out_profile);

        //Assign a Resources variable
        Resources res = getResources();
        //Assign a DB helper
        dbHandler = new MyDBHandler(this);

        //Assign the current User ID to a variable via use of Shared Preference
        userID = Integer.parseInt(getIdfromSharedPreference());

        //FIND VIEWS and assign to variables
        userDeetsPic = (ImageView) findViewById(R.id.ap_profile_image);
        userDeetsEmail = (TextView) findViewById(R.id.ap_user_email);
        userDeetsCourse = (TextView) findViewById(R.id.ap_user_course);
        userDeetsPoints = (TextView) findViewById(R.id.ap_user_points);
        userDeetsEvents = (TextView) findViewById(R.id.ap_user_events);
        userDeetsName = (TextView) findViewById(R.id.ap_user_name);
        userDeetsRanking = (TextView) findViewById(R.id.ap_user_rating);
        userDeetsBio = (TextView) findViewById(R.id.ap_user_about);


        //GET VALUES FROM DATABASE
        user_name = populateDetails(TABLE_USERS, COLUMN_USERNAME,userID);
        user_email = populateDetails(TABLE_USERS, COLUMN_EMAIL,userID);
        user_course = populateDetails(TABLE_USERS, COLUMN_COURSE,userID);
        user_points = populateDetails(TABLE_USERS, COLUMN_AVAILABLE_POINTS,userID);
        user_events = String.valueOf(Integer.parseInt(populateCountDetails(TABLE_EVENTS, COLUMN_HOST_ID, COLUMN_HOST_ID, "=", userID))+Integer.parseInt(populateCountDetails(TABLE_MY_EVENTS, COLUMN_MY_EVENT_ID, COLUMN_MY_EVENT_ID, ">=", 0)));
        user_ranking = populateDetails(TABLE_USERS, COLUMN_HOST_SCORE, userID);
        user_bio = populateDetails(TABLE_USERS, COLUMN_BIO, userID);

        //INSERT DATABASE VALUES INTO PRE-SET STRINGS
        //Points
        String user_points_combined = String.format(res.getString(R.string.user_points), user_points);
        //Email
        String user_email_combined = String.format(res.getString(R.string.user_email), user_email);
        //Course
        String user_course_combined = String.format(res.getString(R.string.user_course), user_course);
        //Events
        String user_events_combined = String.format(res.getString(R.string.user_events), user_events);
        //Ranking
        String user_ranking_combined = String.format(res.getString(R.string.user_rating), user_ranking);

        //ASSIGN IMAGE TO PROFILE PIC
        setProfileImage(userDeetsPic);

        //ASSIGN TEXT STRINGS TO VIEWS IN LAYOUTS
        userDeetsName.setText(user_name);
        userDeetsEmail.setText(user_email_combined);
        userDeetsCourse.setText(user_course_combined);
        userDeetsBio.setText(user_bio);
        userDeetsRanking.setText(user_ranking_combined);
        userDeetsPoints.setText(user_points_combined);
        userDeetsEvents.setText(user_events_combined);

        //End of ON CREATE
    }


    //*********************************************
    //FUNCTIONS RELATING TO THE MENU IN ACTION BAR
    //*********************************************

    //Function (Menu Options) that generates the Menu Options in the Activity bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    //Function (Menu Options Click) that instructs operations to be performed on-click of Menu Options.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //action when corresponding action-bar item is clicked
        switch(item.getItemId()) {

            //"Back" button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            //Quick Scroll to "Profile" Section
            case R.id.profile_jump:
                jumpToProfile();
                return true;

            //Quick Scroll to "Events" Section
            case R.id.events_jump:
                jumpToEvents();
                return true;

            //Quick Scroll to "Reviews" Section
            case R.id.reviews_jump:
                jumpToReviews();
                return true;

            //Drop-down: Create Event
            case R.id.create_events_ql:
                goToCreate();
                return true;

            //Drop-down: Search Event
            case R.id.search_events_ql:
                goToEvents();
                return true;

            //Drop-down: Search Recipe
            case R.id.search_recipe_ql:
                goToRecipe();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Function called when the user clicks the "Profile" quick-link
    public void jumpToProfile(){
        ScrollView scroll = (ScrollView) findViewById(R.id.ap_main_scroll);
        scroll.smoothScrollTo(0,0);
    }

    //Function called when the user clicks the "Events" quick-link
    public void jumpToEvents(){
        ScrollView scroll = (ScrollView) findViewById(R.id.ap_main_scroll);
        scroll.smoothScrollTo(0,1620);
    }

    //Function called when the user clicks the "Reviews quick-link
    public void jumpToReviews(){
        ScrollView scroll = (ScrollView) findViewById(R.id.ap_main_scroll);
        scroll.smoothScrollTo(0,3500);
    }

    //Function called when the user clicks the Search Events quick-link
    public void goToEvents() {
        Intent intent = new Intent(this, EventList.class);
        startActivity(intent);
        finish();
    }

    //Function cCalled when the user clicks the Create Events quick-link
    public void goToCreate() {
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
        finish();
    }

    //Function called when the user clicks the Search Recipe quick-link
    public void goToRecipe() {
        Intent intent = new Intent(this, RecipeFinder.class);
        startActivity(intent);
        finish();
    }

    //Function called when the user clicks the "EDIT PROFILE" button
    public void editProfile(View view) {
        Intent intent = new Intent(this, ProfileEditActivity.class);
        startActivity(intent);
        //finish();
    }


    //**************************
    //DATABASE HELPER FUNCTIONS
    //**************************

    //Function to Select data from Database for input into a View
    public String populateDetails(String Table, String Column, int ID){
        return dbHandler.databaseSelectByIDToString(Table, Column, ID);
    }

    //Function to Select a count of Occurances from Database for input into a View
    public String populateCountDetails(String Table, String CountColumnName, String WhereColumnName, String EqualityMeasure, int WhereEqualsValue){
        return dbHandler.databaseCountByIDToString(Table, CountColumnName, WhereColumnName, EqualityMeasure, WhereEqualsValue);
    }

    //Function to Select the User Profile Pic byte-string from DB and set into Profile Picture ImageView
    public void setProfileImage(ImageView image) {
        String imgInDB = dbHandler.databaseSelectByIDToString(TABLE_USERS, COLUMN_PROFILE_PIC, userID);
        if(imgInDB.length() > 1) {
            image.setImageBitmap(StringToBitMap(imgInDB));
        }
    }

    //Function to convert a Byte String to a Bitmap
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    //**************************
    //GENERAL HELPER FUNCTIONS
    //**************************

    //Function that retrieves the Shared Preference for the current User ID
    public String getIdfromSharedPreference(){
        SharedPreferences prefs = getSharedPreferences("User_Id",0);
        String extractedText =  prefs.getString("shared_ref_id","No ID found");

        return extractedText;
    }

    //Function that calls a "test" toast for use in debugging.
    public void testToast(){
        //Crete Toast;
        Context context = getApplicationContext();
        CharSequence text = ("Test OK");
        int duration = Toast.LENGTH_SHORT;

        Toast toast_set = Toast.makeText(context, text, duration);
        toast_set.setGravity(Gravity.TOP, 0, 200);

        View toast_view = toast_set.getView();
        toast_view.setBackgroundResource(R.drawable.border_background_reverse);
        toast_set.setView(toast_view);

        toast_set.show();
    }

    //End Class
}