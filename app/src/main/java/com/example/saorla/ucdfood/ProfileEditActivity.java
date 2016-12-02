package com.example.saorla.ucdfood;

/**
 * Created by Paudi on 09/11/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
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
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_BIO;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_COURSE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_EMAIL;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_LOCATION;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_PROFILE_PIC;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_USERNAME;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_USERS;
//import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ProfileEditActivity extends AppCompatActivity {

    private int userID;
    EditText userName;
    EditText userEmail;
    EditText userCourse;
    EditText userBio;
    EditText userDeetsInput;
    TextView userDeets;
    String updateMsg = "Updated: ";
    public ImageView mImageView;
    //MyDBHandler dbHandler;
    private File output=null;
    ImageButton addImage;
    ImageView getGPS;
    ImageButton profileImage;
    ImageButton profileImage2;
    String bitmapString;
    //String path = getFilesDir().getAbsolutePath();
    MyDBHandler dbHandler;
    int checkForGps;
    private Thread thread = new ThreadClass();
    private static Looper threadLooper = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        Resources res = getResources();
        dbHandler = new MyDBHandler(this);
        userID = Integer.parseInt(getIdfromSharedPreference());
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

        //Set the transition effect
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
//                    Toast.makeText(getApplicationContext(),"Gallery" , LENGTH_SHORT);
                }
            }
        });

        //Display Dialog Box on-Click
        final AlertDialog dialog = builder.create();

        //Disable the button if the device has no camera
        addImage = (ImageButton) findViewById(R.id.aep_behindImageButton);
        if(!hasCamera()){
            addImage.setEnabled(false);
            Toast.makeText(this,"Feature not available: NO DEVICE CAMERA",Toast.LENGTH_LONG).show();
        }

        //Add a listener for on-click on the button
        addImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });


//********************
// GPS BUTTON LISTENER
//********************

        //Disable the button if the device has no camera
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
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_EMAIL, course_input, userID);
            updateMsg = (""+updateMsg).concat("User Course ");
        }
    }

    //Function to update the User Biography
    public void updateUserBio() {
        String bio_input = userBio.getText().toString();
        if (bio_input.length() >0){
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_EMAIL, bio_input, userID);
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
        else {updateMsg = "Test";}
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

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,5, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 5, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Uri imageToUploadUri;

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
//        File f = new File(Environment.getExternalStorageDirectory(), "PROFILE_IMAGE.jpg");
//        File f = new File(getApplicationContext().getFilesDir(), "PROFILE_IMAGE.jpg");

//        File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//
//        imageToUploadUri = Uri.fromFile(f);

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


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Bitmap resized = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.5), (int)(bitmap.getHeight()*0.5), true);

                //Compress & Convert Bitmap to String
//                bitmapString = BitMapToString(bitmap);
                //Assign the String to the variable for inclusion in DB.
                bitmapString = BitMapToString(resized);
                //Save String in DB
                //updateUserPic();

                //Retrieve String from DB
                //String from_db = dbHandler.databaseSelectByIDToString(TABLE_USERS, COLUMN_PROFILE_PIC, userID);

                //Convert String to Bitmap
                //Bitmap db_bitmap = StringToBitMap(from_db);
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

            Uri selectedImageUri = data.getData();
            String selectedImagePath = getRealPathFromURI(selectedImageUri);
            Log.d("********Img Path", selectedImagePath);

            Toast.makeText(this, "Image not Inserted due to Memory Limitations.\n\nFile stored at Location:\n "+ selectedImagePath+"\n\nUse \"Gallery Upload\" to se this image as your Profile Picture.", Toast.LENGTH_LONG).show();
            File imgFile = new  File(selectedImagePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                //Drawable d = new BitmapDrawable(getResources(), myBitmap);
                Bitmap resized1 = Bitmap.createScaledBitmap(myBitmap,(int)(myBitmap.getWidth()*0.05), (int)(myBitmap.getHeight()*0.05), true);
//                bitmapString = BitMapToString(resized1);
//                Bitmap db_bitmap = StringToBitMap(bitmapString);
//
//                profileImage.setImageBitmap(db_bitmap);
//                ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
//                myImage.setImageBitmap(myBitmap);

            }

//            Bitmap resized = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.5), (int)(bitmap.getHeight()*0.5), true);
//
//
//
//
//
//            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//
//            Bitmap resized = Bitmap.createScaledBitmap(imageBitmap,(int)(imageBitmap.getWidth()*0.01), (int)(imageBitmap.getHeight()*0.01), true);
//            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
//            //Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
////            ImageView test = (ImageView) findViewById(R.id.testIMG);
////            test.setImageBitmap(resized);
//            profileImage.setImageBitmap(resized);
//            //Compress & Convert Bitmap to String
//            //bitmapString = BitMapToString(imageBitmap);
//            bitmapString = BitMapToString(resized);
//
//
//            //Convert String to Bitmap
//            Bitmap db_bitmap = StringToBitMap(bitmapString);
//            Toast.makeText(this, ""+bitmapString, Toast.LENGTH_LONG).show();
//
//
//            try {
//                //Set Bitmap into ButtonImage Profile Pic
////                ImageView test = (ImageView) findViewById(R.id.testIMG);
////                test.setImageBitmap(db_bitmap);
////              profileImage.setImageBitmap(db_bitmap);
//                profileImage.setAlpha((float) 1 );
//                Toast.makeText(this, "Image Inserted", Toast.LENGTH_LONG).show();
//
//            }
//            catch (Exception e){
//                Toast.makeText(this,"Insert Failed",Toast.LENGTH_LONG).show();
//            }
        }
    }

    //----------------------------------------
    /**
     * This method is used to get real path of file from from uri
     *
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



    private Bitmap loadImage(String imgPath) {
        BitmapFactory.Options options;
        try {
            options = new BitmapFactory.Options();
            options.inSampleSize = 4;// 1/4 of origin image size from width and height
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }



//    ****
    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    // TODO Auto-generated method stub
    //    super.onActivityResult(requestCode, resultCode, data);

    //    if (resultCode == RESULT_OK){
    //        Uri targetUri = data.getData();
    //        textTargetUri.setText(targetUri.toString());
    //        Bitmap bitmap;
    //        try {
    //            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
    //            targetImage.setImageBitmap(bitmap);
    //        } catch (FileNotFoundException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //    }
    //}

    //private ImageView myImage = (ImageView)findViewById(R.id.imageViewCamera);
    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //  if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
    //    Bundle extras = data.getExtras();
    //  Bitmap imageBitmap = (Bitmap) extras.get("data");
    //myImage.setImageBitmap(imageBitmap);
    //}
    //}

//*************************
    // GPS
    //*************************

    private boolean isGSPEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
            return true;
        }
        else{return false;}
    }

    private void EnableGPSIfPossible() {
        if ( !isGSPEnabled() ) {
            buildAlertMessageNoGps();
        }
        else {
            Toast.makeText(this,"GPS Active! Accessing your current location.",Toast.LENGTH_LONG).show();
            thread.start();}
    }


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

//    protected void createLocationRequest() {
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }

    protected void getGpsCoords(){

        GpsHandler gpsTracker = new GpsHandler(this);

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
//        textview = (TextView)findViewById(R.id.fieldLatitude);
//        textview.setText(stringLatitude);

            String stringLongitude = String.valueOf(gpsTracker.longitude);
//        textview = (TextView)findViewById(R.id.fieldLongitude);
//        textview.setText(stringLongitude);

            String country = gpsTracker.getCountryName(this);
//        textview = (TextView)findViewById(R.id.fieldCountry);
//        textview.setText(country);

            String city = gpsTracker.getLocality(this);
//        textview = (TextView)findViewById(R.id.fieldCity);
//        textview.setText(city);

            String postalCode = gpsTracker.getPostalCode(this);
//        textview = (TextView)findViewById(R.id.fieldPostalCode);
//        textview.setText(postalCode);

            String addressLine = gpsTracker.getAddressLine(this);
//        textview = (TextView)findViewById(R.id.fieldAddressLine);
//        textview.setText(addressLine);

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