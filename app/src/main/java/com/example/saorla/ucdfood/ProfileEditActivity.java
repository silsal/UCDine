package com.example.saorla.ucdfood;

/**
 * Created by Paudi on 09/11/2016.
 */

//This Class constructs the Edit Profile Activity, making calls to the database to retrieve values to be populated in relevant hint & text
//Allows the user to input new values and save to DB
//Allows user to update profile picture by accessing camera and gallery
//Allows user to update their GPS location by accessing the gps system
//The action-bar also has a "drop-down" menu to access the other activities in the app

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_BIO;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_COURSE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_EMAIL;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_LOCATION;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_PROFILE_PIC;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_USERNAME;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_USERS;


public class ProfileEditActivity extends AppCompatActivity {
    //Initialise Variables
    private int userID;
    private EditText userName;
    private EditText userEmail;
    private EditText userCourse;
    private EditText userBio;
    private String updateMsg = "Updated: ";
    private ImageView getGPS;
    private ImageButton profileImage;
    private ImageButton profileImage2;
    //String for writing images to the DB
    private String bitmapString ="";
    //Database Helper
    private MyDBHandler dbHandler;
    private int checkForGps;
    private Thread thread = new ThreadClass();
    private static Looper threadLooper = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Create the Activity View
        setContentView(R.layout.activity_edit_profile);
        //Set the transition effect
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        //Assign a Resources variable
        Resources res = getResources();
        //Assign a DB helper
        dbHandler = new MyDBHandler(this);

        //Assign the current User ID to a variable via use of Shared Preference
        userID = Integer.parseInt(getIdfromSharedPreference());

        //Control to check if user has visited GPS settings
        checkForGps = 0;

        //Get the views
        userName = (EditText) findViewById(R.id.aep_editUserName);
        userEmail = (EditText) findViewById(R.id.aep_editUserEmail);
        userCourse = (EditText) findViewById(R.id.aep_editUserCourse);
        userBio = (EditText) findViewById(R.id.aep_editUserBio);

        //Set the Hints
        setViewHint(TABLE_USERS, COLUMN_USERNAME, userName, String.format(res.getString(R.string.user_name)));
        setViewHint(TABLE_USERS, COLUMN_EMAIL, userEmail,  String.format(res.getString(R.string.user_email),"email?"));
        setViewHint(TABLE_USERS, COLUMN_COURSE, userCourse,  String.format(res.getString(R.string.user_course),"course?"));
        setViewHint(TABLE_USERS, COLUMN_BIO, userBio,  String.format(res.getString(R.string.user_bio)));

        profileImage = (ImageButton) findViewById(R.id.aep_behindImageButton);
        profileImage2 = (ImageButton) findViewById(R.id.aep_updateImageButton);

        //Create Dialog box for choose camera/gallery
        final String[] option = new String[] {"Take Photo", "Gallery Upload"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Log.e("Selected Item", String.valueOf(which));
                if (which == 0) {
                    //Call the take picture function
                    updateProfilePic(null);
                }
                if (which == 1) {
                    //Call the go-to-gallery fucntion
                    pickGallery(null);
                }
            }
        });

        //Display Dialog Box on-Click
        final AlertDialog dialog = builder.create();

        //Disable the button if the device has no camera
        if(!hasCamera()){
            profileImage.setEnabled(false);
            Toast.makeText(this,"Feature not available: NO DEVICE CAMERA",Toast.LENGTH_LONG).show();
        }

        //Add a listener for on-click on the button
        profileImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });


        //********************
        // GPS BUTTON LISTENER
        //********************

        //Get GPS Logo Image view
        getGPS = (ImageView) findViewById(R.id.aep_maps_icon);

        //Add a listener for on-click on the button
        getGPS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EnableGPSIfPossible();
            }
        });


        //End of "On Create"
    }


    //*****************************
    //  On resume from Changing GPS Setting
    //*****************************
    @Override
    public void onResume(){
        super.onResume();
        // If returning from Location Setting and GPS was activated
        if (isGSPEnabled() && checkForGps == 1){
            thread.start();
            checkForGps = 0;
        }
        //If returning from Location Setting Screen but user didn't activate GPS
        if(!isGSPEnabled() && checkForGps == 1){
            Toast.makeText(this,"GPS not Activated.\nLocation Not Set",Toast.LENGTH_LONG).show();
            checkForGps = 0;
        }

    }

    //*****************************
    //  Looper Thread Helper Class
    //*****************************
    private class ThreadClass extends Thread {
        @Override
        public void run() {
            try {
                super.run();
                sleep(5000);  //Delay of 5 seconds
            } catch (Exception e) {

            } finally {

                Looper.prepare();

                getGpsCoords();

                threadLooper = Looper.myLooper();
                Looper.loop();  // loop until "quit()" is called.
            }
        }

    }


    //*********************************************
    //FUNCTIONS RELATING TO THE MENU IN ACTION BAR
    //*********************************************

    //Function (Menu Options) that generates the Menu Options in the Activity bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_profile, menu);
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

            //Save icon
            case R.id.save:
                saveAllUpdates();
                Toast.makeText(this, ""+updateMsg , Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(this);
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


    //Function called when the user clicks the Search Events quick-link
    public void goToEvents() {
        Intent intent = new Intent(this, EventList.class);
        startActivity(intent);
        finish();
    }

    //Function called when the user clicks the Create Events quick-link
    public void goToCreate() {
        Intent intent = new Intent(this, CreateEvent.class);
        //intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
        finish();
    }

    //Function called when the user clicks the Search Recipe quick-link
    public void goToRecipe() {
        Intent intent = new Intent(this, RecipeFinder.class);
        //intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
        finish();
    }


    //**************************************************
    //HELPER FUNCTIONS FOR POPULATING TEXT & IMAGE VIEWS
    //**************************************************

    //General Function to Set the "Hint" in edit text views
    public void setViewHint(String Table, String Column, EditText view, String default_hint) {
        String hint = dbHandler.databaseSelectByIDToString(Table, Column, userID);
        if (hint.length() >0){
            view.setHint(hint);
        }
        else {view.setHint(default_hint);}
    }

    //General Function to Set the bitmap image in Image Views
    public void setImage(String Table, String Column, ImageView view) {
        String image_path = dbHandler.databaseSelectByIDToString(Table, Column, userID);

        try {
            File f=new File(image_path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            view.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }


    //***********************************************************
    //HELPER FUNCTIONS FOR UPDATING DATABASE WITH NEW USER INPUTS
    //***********************************************************

    //Function to Update the UserName
    public void updateUserName() {
        String name_input = userName.getText().toString();
        if (name_input.length() >0){
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_USERNAME, name_input, userID);
            updateMsg = (""+updateMsg).concat("User Name ");
        }
    }

    //Function to Update the User Email
    public void updateUserEmail() {
        String email_input = userEmail.getText().toString();
        if (email_input.length() >0){
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_EMAIL, email_input, userID);
            updateMsg = (""+updateMsg).concat("User Email ");
        }
    }

    //Function to Update the User Course Details
    public void updateUserCourse() {
        String course_input = userCourse.getText().toString();
        if (course_input.length() >0){
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_COURSE, course_input, userID);
            updateMsg = (""+updateMsg).concat("User Course ");
        }
    }

    //Function to update the User Biography
    public void updateUserBio() {
        String bio_input = userBio.getText().toString();
        if (bio_input.length() >0){
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_BIO, bio_input, userID);
            updateMsg = (""+updateMsg).concat("User Bio ");
        }
    }

    //Function to update the User Picture
    public void updateUserPic() {
        if (bitmapString.length() >0){
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_PROFILE_PIC, bitmapString, userID);
            updateMsg = (""+updateMsg).concat("User Picture ");
        }
    }

    //Wrapper Function that updates all fields in Edit Profile Page
    public void saveAllUpdates(){
        updateUserName();
        updateUserEmail();
        updateUserCourse();
        updateUserBio();
        updateUserPic();
        if ((""+updateMsg).equals("Updated: ")){
            updateMsg = (""+updateMsg).concat("No Updates!");
        }
        else {updateMsg = "Profile Updated";}
    }


    //******************
    //GENERAL FUNCTIONS
    //******************

    //General Function that Selects the Shared Preference for User ID
    public String getIdfromSharedPreference(){
        SharedPreferences prefs = getSharedPreferences("User_Id",0);
        String extractedText =  prefs.getString("shared_ref_id","No ID found");
        return extractedText;
    }

    //Function to check if device has camera
    public boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }


    //**************************
    //CAMERA & GALLERY FUNCTIONS
    //**************************

    //Function that compresses and converts a bitmap to a string of byte-array
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,5, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    //Function that retrieves a string of byte-array and converts to a bitmap
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    //Selecting From Gallery
    private int PICK_IMAGE_REQUEST = 1;
    public void pickGallery(View view) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Take Picture With Camera
    private int CAMERA_REQUEST = 11;
    public void updateProfilePic(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
    }

    //Function to carry out once the response from either Camera or Gallery has completed.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If GALLERY
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                //Get the bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //Resize the bitmap
                Bitmap resized = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.5), (int)(bitmap.getHeight()*0.5), true);

                //Compress & Convert Bitmap to String & Assign the String to the variable for inclusion in DB.
                bitmapString = BitMapToString(resized);

                //Convert String to Bitmap
                Bitmap db_bitmap = StringToBitMap(bitmapString);

                //Set Bitmap into ButtonImage Profile Pic
                profileImage.setImageBitmap(db_bitmap);
                profileImage.setAlpha((float) 1);
                Toast.makeText(this, "Image Inserted", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Insert Failed", Toast.LENGTH_LONG).show();
            }
        }

        //IF CAMERA
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this,"Camera Request/Response Successful",Toast.LENGTH_SHORT).show();

//            **** INTENTIONALLY OMITTED ****
//            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

            //Get the path the picture is saved to
            Uri selectedImageUri = data.getData();
            String selectedImagePath = getRealPathFromURI(selectedImageUri);
            //Log.d("********Img Path", selectedImagePath);

            //Advise the user that the image is too large to handle but its saved in gallery
            Toast.makeText(this, "Image not Inserted due to Memory Limitations.\n\nFile stored at Location:\n "+ selectedImagePath+"\n\nUse \"Gallery Upload\" to se this image as your Profile Picture.", Toast.LENGTH_LONG).show();
            File imgFile = new  File(selectedImagePath);

            //**** THIS SECTION IS INTENTIONALLY DISABLED DUE TO MEMORY CAPABILITIES ISSUES *********************
            if(imgFile.exists()){
                //Convert the file path to a bitmap
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                //Resize the bitmap & Convert to String to save in DB
                //Bitmap resized1 = Bitmap.createScaledBitmap(myBitmap,(int)(myBitmap.getWidth()*0.05), (int)(myBitmap.getHeight()*0.05), true);
//                bitmapString = BitMapToString(resized1);
//                Bitmap db_bitmap = StringToBitMap(bitmapString);
                //Set the image into the image-view
//                profileImage.setImageBitmap(db_bitmap);
            }
        }
    }

    //----------------------------------------
    /**
     * This method is used to get real path of file from from uri
     * (FROM STACKOVERFLOW)
     * @param contentUri
     * @return String
     */
    //----------------------------------------
    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }

    //*************************
    // GPS RELATED FUNCTIONS
    //*************************

    //Function to check if GPS is enabled
    private boolean isGSPEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
            return true;
        }
        else{return false;}
    }

    //Function to allow user to enable GPS - Calls POP-UP Dialog box
    private void EnableGPSIfPossible() {
        if ( !isGSPEnabled() ) {
            buildAlertMessageNoGps();
        }
        else {
            Toast.makeText(this,"GPS Active! Accessing your current location.",Toast.LENGTH_LONG).show();
            thread.start();}
    }

//    Function to build the pop-up alert for enable gps
    private  void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        checkForGps = 1;
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    //Function that extracts the gps co-ordinates from the gps system (Ref. Stackoverflow)
    protected void getGpsCoords(){

        GpsHandler gpsTracker = new GpsHandler(this);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            String stringLatitude = String.valueOf(gpsTracker.latitude);

            String stringLongitude = String.valueOf(gpsTracker.longitude);

            String country = gpsTracker.getCountryName(this);

            String city = gpsTracker.getLocality(this);

            String postalCode = gpsTracker.getPostalCode(this);

            String addressLine = gpsTracker.getAddressLine(this);

            if (stringLatitude == "0.0" && stringLongitude == "0.0"){
                Toast.makeText(this, "Searching for GPS!\nMove to another position & try again" , Toast.LENGTH_LONG).show();
            }
            else{
                //Update the Database & Notify User
                dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_LOCATION, ""+stringLatitude+", " +stringLongitude, userID);
                Toast.makeText(this, "| Lat: " + stringLatitude + " |\n| Long: " + stringLongitude + " |", Toast.LENGTH_LONG).show();}
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
    }


}