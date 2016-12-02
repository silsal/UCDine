package com.example.saorla.ucdfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by shauna on 16/11/2016.
 */

//this class is for the EventDetail activity. It is passed data from the event list activity depending on which
// event is clicked and displays the details of that event
public class EventDetail extends AppCompatActivity {

    private static Button button_attend;
    public final static String DETAILS_MESSAGE = "event_details";
    public final static String TITLE_MESSAGE = "event_title";
    public final static String HOST_MESSAGE = "host_details";
    public final static String TIME_MESSAGE = "time";
    public final static String DATE_MESSAGE = "date";
    public final static String ADDRESS_MESSAGE = "address";
    public final static String IS_ATTENDING_MESSAGE = "is_attending";
    public final static String CAN_ATTEND_MESSAGE = "can_attend";
    public final static String POINTS_MESSAGE = "points";
    public final static String EID_MESSAGE = "eid";
    int int_is_attending;
    int int_can_attend;
    int int_points;

    String title ="";
    String host = "";
    String details="";
    String time="";
    String date="";
    String address="";
    String is_attending="";
    String can_attend="";
    String points="";
    String eid="";

    MyDBHandler helperevent = new MyDBHandler(this);

    //method to create the activity onload
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);
        onButtonClickListener();
        Intent intent = getIntent();
        if (null != intent) {
            title = intent.getStringExtra(EventList.TITLE_MESSAGE);
            host = intent.getStringExtra(EventList.HOST_MESSAGE);
            details = intent.getStringExtra(EventList.DETAILS_MESSAGE);
            time = intent.getStringExtra(EventList.TIME_MESSAGE);
            date = intent.getStringExtra(EventList.DATE_MESSAGE);
            address = intent.getStringExtra(EventList.ADDRESS_MESSAGE);
            is_attending = intent.getStringExtra(EventList.IS_ATTENDING_MESSAGE);
           can_attend = intent.getStringExtra(EventList.CAN_ATTEND_MESSAGE);
            points = intent.getStringExtra(EventList.POINTS_MESSAGE);
            eid = intent.getStringExtra(EventList.EID_MESSAGE);
        }
        int_is_attending = Integer.parseInt(is_attending);
        int_can_attend =  Integer.parseInt(can_attend);
        int_points = Integer.parseInt(points);

        //FIND VIEWS
        TextView titleTxt = (TextView) findViewById(R.id.title);
        titleTxt.setText(title);

        TextView hostTxt = (TextView) findViewById(R.id.host);
        hostTxt.setText(host);

        TextView timeTxt = (TextView) findViewById(R.id.time);
        timeTxt.setText(time);

        TextView descriptionTxt = (TextView) findViewById(R.id.description);
        descriptionTxt.setText(details);

        TextView dateTxt = (TextView) findViewById(R.id.date);
        dateTxt.setText(date);

        TextView addressTxt = (TextView) findViewById(R.id.address);
        addressTxt.setText(address);


// confirmation alert box
    }
    public void onButtonClickListener() {
        button_attend = (Button) findViewById(R.id.attend);
        button_attend.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //build alert dialog box and set the message you want to appear
                        AlertDialog.Builder a_builder = new AlertDialog.Builder(EventDetail.this);
                        a_builder.setMessage("Are you sure you want to attend this event? One point will be deducted from you").setCancelable(false)
                                //if yes is selected
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //retrieve user id
                                        String user = getIdfromSharedPreference();
                                        //cast as integers
                                        int id = Integer.parseInt(user);
                                        int points = helperevent.getPoints(id);
                                        int int_eid = Integer.parseInt(eid);
                                        //condition to check if there are available positions to attend the event and then if the user has points
                                        if (int_is_attending >=1) {
                                            if (points <1) {
                                                Toast.makeText(getApplicationContext(), "Sorry, you dont have any points left on your account, try hosting an event to earn points!", Toast.LENGTH_LONG).show();
                                            } else {
                                                MyEvent mev = new MyEvent();
                                                mev.setMyaid(int_eid);
                                                //write to database to decrement users points and positions available to attend the event & Update the "My Events" table with Event ID.
                                                helperevent.reducePoints(id);
                                                helperevent.reduceAvailableNumber(int_eid);
                                                helperevent.addToMyEvent(mev);//
                                                Toast.makeText(getApplicationContext(), "One point has been deducted from your account", Toast.LENGTH_LONG).show();
                                                dialog.cancel();
                                                //decrement the points variable inside the page as this is not updated unless the database is called again
                                                points --;
                                                int_is_attending --;
                                            }
                                        }
                                    }
                                })
                                //cancel dialog box if no selected
                                .setNegativeButton("No",new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = a_builder.create();
                        alert.setTitle("Confirm Attendance");
                        alert.show();
                    }
                }
        );}
    //function that retrive the id of the user
    public String getIdfromSharedPreference() {
        SharedPreferences prefs = getSharedPreferences("User_Id", 0);
        String extractedText = prefs.getString("shared_ref_id", "No ID found");

        return extractedText;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.event_detail_main, menu);
        return true;
    }
    /** Called when the user clicks the Profile quick-link */
    public void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    /** Called when the user clicks the Create Events quick-link */
    public void goToCreate() {
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
        finish();
    }

    /** Called when the user clicks the Search Recipe quick-link */
    public void goToRecipe() {
        Intent intent = new Intent(this, RecipeFinder.class);
        startActivity(intent);
        finish();
    }
    /** Called when the user clicks the Create Events quick-link */
    public void goToEvents() {
        Intent intent = new Intent(this, EventList.class);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //action when corresponding action-bar item is clicked
        switch(item.getItemId()) {

            //"Back" button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

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

