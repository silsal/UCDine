package com.example.saorla.ucdfood;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 09/11/2016.
 */

public class Display extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

//        String username = getIntent().getStringExtra("Username");
//
//        TextView tv = (TextView) findViewById(R.id.TVusername);
//        tv.setText(username);

//        displayUserId();



    }
//    public void displayUserId (){
//        String userid = getIdfromSharedPrefernece();
//        TextView textView = (TextView)findViewById(R.id.TVdisplayid);
//        textView.setText(userid);
//    }

    public String getIdfromSharedPrefernece(){
        SharedPreferences prefs = getSharedPreferences("User_Id",0);
        String extractedText =  prefs.getString("shared_ref_id","No ID found");

        return extractedText;
    }

    public void showCreateActivity (View view){
        Intent i = new Intent(this, CreateEvent.class);
        startActivity(i);
    }

    public void showProfileActivity (View view){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void showRecipeFinder (View view){
        Intent i = new Intent(this, RecipeFinder.class);
        startActivity(i);
    }



}
