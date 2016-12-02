package com.example.saorla.ucdfood;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

/**
 * Activity for main menu which includes:
 * Novelty onclick logo function
 * Buttons to navigate from homepage to other activities
 */

public class Display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_profile, R.anim.slide_out_profile);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
    }

    /**
     Method to animate logo on click &
     Produce 'Baaa' sound
     */
    public void flash(View v) {
        //Set up animation
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(5);
        animation.setRepeatMode(Animation.REVERSE);

        //Set up Media player
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sheep);
        if(v.getId()==R.id.BSheep){
            mp.start();
            v.startAnimation(animation);
            Toast.makeText(getBaseContext(), "Baaaa, welcome!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Methods in menu to allow users to move between homepage and other actvites
     * including profile, event list, Recipe finder and create event
     */

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
