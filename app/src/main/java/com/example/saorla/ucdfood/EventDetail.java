package com.example.saorla.ucdfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by shauna on 16/11/2016.
 */

public class EventDetail extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.saorla.ucdfood.MESSAGE";
    public String[] stringArray(String string_name){
        String[] strArray = string_name.split(" ");
        return strArray;
    }
    String strName = "name this tune";
    String name = stringArray(strName)[0];
    String user_name = name;

    private static Button button_attend;
    public final static String PICTURE_MESSAGE = "event_pic";
    public final static String TITLE_MESSAGE = "event_title";
    public final static String HOST_MESSAGE = "event_host";
    public final static String DETAILS_MESSAGE = "event_details";
    public int points = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        onButtonClickListener();
        String title ="";
        String host = "";
        String details="";
        Intent intent = getIntent();
        if (null != intent) {
            title = intent.getStringExtra(EventList.TITLE_MESSAGE);
            host = intent.getStringExtra(EventList.HOST_MESSAGE);
            details = intent.getStringExtra(EventList.DETAILS_MESSAGE);
        }

        TextView titleTxt = (TextView) findViewById(R.id.title);
        titleTxt.setText(title);

        TextView hostTxt = (TextView) findViewById(R.id.host);
        hostTxt.setText(host);

        TextView descriptionTxt = (TextView) findViewById(R.id.description);
        descriptionTxt.setText(details);


// confirmation alert box
    }
    public void onButtonClickListener() {
        button_attend = (Button) findViewById(R.id.attend);
        button_attend.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(EventDetail.this);
                        a_builder.setMessage("Are you sure you want to attend this event? One point will be deducted from you").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (points < 1){
                                            Toast.makeText(getApplicationContext(), "Sorry, you dont have any points left on your account, try hosting an event to earn points!", Toast.LENGTH_LONG).show();
                                        }else {
                                            points -= 1;
                                            Toast.makeText(getApplicationContext(), "One point has been deducted from your account", Toast.LENGTH_LONG).show();
                                            dialog.cancel();
                                        }}
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Confirmation");
                        alert.show();
                    }
                }
        );

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.event_detail_main, menu);
        return true;
    }
    /** Called when the user clicks the Profile quick-link */
    public void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
        finish();
    }

    /** Called when the user clicks the Create Events quick-link */
    public void goToCreate() {
        Intent intent = new Intent(this, CreateEvent.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
        finish();
    }

    /** Called when the user clicks the Search Recipe quick-link */
    public void goToRecipe() {
        Intent intent = new Intent(this, RecipeFinder.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
        finish();
    }
    /** Called when the user clicks the Create Events quick-link */
    public void goToEvents() {
        Intent intent = new Intent(this, EventList.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //action when corresponding action-bar item is clicked
        switch(item.getItemId()) {

            case R.id.create_events_ql:
                goToCreate();
                return true;

            case R.id.profile_ql:
                goToProfile();
                return true;

            case R.id.search_recipe_ql:
                goToRecipe();
                return true;

            case R.id.search_events_ql:
                goToEvents();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
