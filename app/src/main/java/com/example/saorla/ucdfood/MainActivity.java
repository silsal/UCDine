package com.example.saorla.ucdfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.saorla.ucdfood.MESSAGE";
    private static final String TAG = "Saorla's Message!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "OnCreate");
    }

    //Launch RecipeFinder activity
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, RecipeFinder.class);
        startActivity(intent);
    }

    //launch the InsertUser activity
    public void createUser(View v) {
        Intent in = new Intent(this, InsertUser.class);
        startActivity(in);
    }

    //launch the eventList activity
    public void eventList(View l) {
        Intent i = new Intent(this, EventList.class);
        startActivity(i);
    }

    //launch create event
    public void createEvent(View event) {
        // Do something in response to button
        Intent launchCreateEvent = new Intent(this, CreateEvent.class);
        startActivity(launchCreateEvent);
    }

}