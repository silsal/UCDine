package com.example.saorla.ucdfood;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * Created by user on 09/11/2016.
 */

public class Display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
    }


        public void flash(View v) {
            final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
            animation.setDuration(300); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            animation.setRepeatCount(5); // Repeat animation infinitely
            animation.setRepeatMode(Animation.REVERSE);

            final MediaPlayer mp = MediaPlayer.create(this, R.raw.sheep);

            if(v.getId()==R.id.BSheep){
                mp.start();
                v.startAnimation(animation);
            }

        }

//        String username = getIntent().getStringExtra("Username");
//
//        TextView tv = (TextView) findViewById(R.id.TVusername);
//        tv.setText(username);

//        displayUserId();




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

    public void showEventListActivity (View view){
        Intent i = new Intent(this, EventList.class);
        startActivity(i);
    }




}
