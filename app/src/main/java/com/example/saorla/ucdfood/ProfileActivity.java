package com.example.saorla.ucdfood;

/**
 * Created by Paudi on 09/11/2016.
 */

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.content.SharedPreferences;
//For camera
import android.widget.ImageView;
import android.widget.Button;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ViewFlipper;

import static android.R.attr.cursorVisible;
import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_ATTENDEE_POINTS;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_AVAILABLE_POINTS;
//import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_COURSE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_BIO;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_COURSE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_EMAIL;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_HOST_ID;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_HOST_SCORE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_USERNAME;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_USER_ID;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_EVENTS;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_USERS;
import android.content.Intent;


public class ProfileActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.saorla.ucdfood.MESSAGE";
    private int userID;
    TextView userDeetsName;
    TextView userDeetsEmail;
    TextView userDeetsCourse;
    TextView userDeetsPoints;
    TextView userDeetsEvents;
    TextView userDeetsRanking;
    TextView userDeetsBio;

    public String[] stringArray(String string_name){
        String[] strArray = string_name.split(" ");
        return strArray;
    }

    String strName = "name this tune";

    String name = stringArray(strName)[0];


    String user_name;
    String user_email;
    String user_course;
    String user_points;
    String user_events;
    String user_ranking;
    String user_bio;

    String userDeetsText;
    TextView userWelcome;
    MyDBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //user_id = from Login/Display getIdfromSharedPreferences**METHOD FROM ADAM***
        String db_response_userTable = "id Paudi Smith Paudi paudi@ucd.ie CompSci 2 6";
        String db_response_eventTable = "id_e id_u 5 PizzaTown Italian 25_Nov_2016";
        String db_response_reviewsTable = "Great!";

        setContentView(R.layout.activity_profile);
        overridePendingTransition(R.anim.slide_in_profile, R.anim.slide_out_profile);

        Resources res = getResources();
        dbHandler = new MyDBHandler(this);
        userID = Integer.parseInt(getIdfromSharedPreference());
//        userID = 0;
        //FIND VIEWS
        //In row 2 of Profile Layout
        userDeetsEmail = (TextView) findViewById(R.id.ap_user_email);
        userDeetsCourse = (TextView) findViewById(R.id.ap_user_course);
        userDeetsPoints = (TextView) findViewById(R.id.ap_user_points);
        userDeetsEvents = (TextView) findViewById(R.id.ap_user_events);
        //In row 3 of Profile Layout
        userDeetsName = (TextView) findViewById(R.id.ap_user_name);
        userDeetsRanking = (TextView) findViewById(R.id.ap_user_rating);
        //In row 3 of Profile Layout
        userDeetsBio = (TextView) findViewById(R.id.ap_user_about);
        //Welcome Note
        userWelcome = (TextView) findViewById(R.id.ap_welcome_note);

        //GET VALUES FROM DATABASE
        //user_name = stringArray(db_response_userTable)[3];
        user_name = populateDetails(TABLE_USERS, COLUMN_USERNAME,userID);
        //user_name = getIdfromSharedPrefernece();
        //user_email = stringArray(db_response_userTable)[4];
        user_email = populateDetails(TABLE_USERS, COLUMN_EMAIL,userID);
        //user_course = stringArray(db_response_userTable)[5];
        user_course = populateDetails(TABLE_USERS, COLUMN_COURSE,userID);
        //user_points = stringArray(db_response_userTable)[6];
        user_points = populateDetails(TABLE_USERS, COLUMN_AVAILABLE_POINTS,userID);
        //user_events = Integer.toString(stringArray(db_response_eventTable).length);
        user_events = populateCountDetails(TABLE_EVENTS, COLUMN_HOST_ID, COLUMN_HOST_ID, userID);
        //user_ranking = stringArray(db_response_userTable)[7];
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


        //Welcome Note
        String welcome_note = String.format(res.getString(R.string.welcome3), user_name);

        //ASSIGN TEXT STRINGS TO VIEWS IN LAYOUTS
        userDeetsName.setText(user_name);
        userDeetsEmail.setText(user_email_combined);
        userDeetsCourse.setText(user_course_combined);
        userDeetsBio.setText(user_bio);
        userDeetsRanking.setText(user_ranking_combined);
        userDeetsPoints.setText(user_points_combined);
        userDeetsEvents.setText(user_events_combined);
        userWelcome.setText(welcome_note);

        //Intent intent = getIntent();
        //String message = intent.getStringExtra(ProfileEditActivity.EXTRA_MESSAGE_2);
    }


    public String getIdfromSharedPreference(){
        SharedPreferences prefs = getSharedPreferences("User_Id",0);
        String extractedText =  prefs.getString("shared_ref_id","No ID found");

        return extractedText;
    }






//    public String getIdfromSharedPrefernece(){
//        SharedPreferences prefs = getSharedPreferences(id);
//        String extractedText =  prefs.getString("User_id");
//
//        return extractedText;
//    }

    public String populateDetails(String Table, String Column, int ID){
        return dbHandler.databaseSelectByIDToString(Table, Column, ID);
    }


    public String populateCountDetails(String Table, String CountColumnName, String WhereColumnName, int WhereEqualsValue){
        return dbHandler.databaseCountByIDToString(Table, CountColumnName, WhereColumnName, WhereEqualsValue);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    public void jumpToProfile(){
        ScrollView scroll = (ScrollView) findViewById(R.id.ap_main_scroll);
        scroll.smoothScrollTo(0,0);
    }

    public void jumpToEvents(){
        ScrollView scroll = (ScrollView) findViewById(R.id.ap_main_scroll);
        scroll.smoothScrollTo(0,1600);
    }

    public void jumpToReviews(){
        ScrollView scroll = (ScrollView) findViewById(R.id.ap_main_scroll);
        scroll.smoothScrollTo(0,3200);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //action when corresponding action-bar item is clicked
        switch(item.getItemId()) {

            case R.id.profile_jump:
                jumpToProfile();
                return true;

            case R.id.events_jump:
                jumpToEvents();
                return true;

            case R.id.reviews_jump:
                jumpToReviews();
                return true;

            case R.id.create_events_ql:
                goToCreate();
                return true;

            case R.id.search_events_ql:
                goToEvents();
                return true;

            case R.id.search_recipe_ql:
                goToRecipe();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

//    public TextView getTextView() {
//        TextView txtView = (TextView)findViewById(R.id.ap_user_name);
//        return txtView;
//    }

    /** Called when the user clicks the Send button */
    public void editProfile(View view) {
        Intent intent = new Intent(this, ProfileEditActivity.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
        finish();
    }


//    TODO ******** ENSURE CORRECT ACTIVITY NAME******************

    /** Called when the user clicks the Search Events quick-link */
    public void goToEvents() {
        Intent intent = new Intent(this, EventList.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
    }

    /** Called when the user clicks the Create Events quick-link */
    public void goToCreate() {
        Intent intent = new Intent(this, CreateEvent.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
    }

    /** Called when the user clicks the Search Recipe quick-link */
    public void goToRecipe() {
        Intent intent = new Intent(this, RecipeFinder.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
    }

    public void cancelToast(View view){
        //Create Toast
        Context context = getApplicationContext();
//        Context context = this;
        CharSequence cancelText = ("Event Cancelled");
        int duration = Toast.LENGTH_SHORT;

        Toast toast_cancel = Toast.makeText(context, cancelText, duration);
        toast_cancel.setGravity(Gravity.TOP, 0, 200);

        View toast_view = toast_cancel.getView();
        toast_view.setBackgroundResource(R.drawable.border_background_reverse);
        toast_cancel.setView(toast_view);


        toast_cancel.show();

    }


    /** Called when the user clicks the "UPDATE" button on Profile Page*/
//    public void updateUserDetails(View view) {
//        //Capture the Input Name
//        EditText editText_name = (EditText) findViewById(R.id.ap_user_name);
//        String toast_msg_name = editText_name.getText().toString();
//        if (toast_msg_name.isEmpty()){toast_msg_name = "Name Empty!";}
//        //Capture the input Email
//        EditText editText_email = (EditText) findViewById(R.id.ap_user_email);
//        String toast_msg_email = editText_email.getText().toString();
//        if (toast_msg_email.isEmpty()){toast_msg_email = "Email Empty!";}
//        //Capture the input Course
//        EditText editText_course = (EditText) findViewById(R.id.ap_user_course);
//        String toast_msg_course = editText_course.getText().toString();
//        if (toast_msg_course.isEmpty()){toast_msg_course = "Course Empty!";}
//
//        //Crete Toast
//        Context context = getApplicationContext();
//        CharSequence text = ("Profile Successfully Updated!" +
//                "\nName:\t").concat(toast_msg_name)
//                .concat("\nEmail:\t").concat(toast_msg_email)
//                .concat("\nCourse:\t").concat(toast_msg_course);
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast_update = Toast.makeText(context, text, duration);
//        toast_update.setGravity(Gravity.TOP, 0, 200);
//
//        View toast_view = toast_update.getView();
//        toast_view.setBackgroundResource(R.drawable.border_background_reverse);
//        toast_update.setView(toast_view);
//
//
//        toast_update.show();
//
//        //Make keyboard disappear onclick
//        // Check if no view has focus:
//        View profile_view = this.getCurrentFocus();
//        if (profile_view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(profile_view.getWindowToken(), 0);
//            //imm.setCursorVisible(false);
//        }
//    }
}