package com.example.saorla.ucdfood;

/**
 * Created by Paudi on 09/11/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
//import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ProfileEditActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.saorla.ucdfood.MESSAGE";

    EditText userName;
    EditText userEmail;
    EditText userCourse;
    EditText userAbout;
    EditText userDeetsInput;
    TextView userDeets;
    String input;
    public ImageView mImageView;
    //MyDBHandler dbHandler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        String message = intent.getStringExtra(ProfileActivity.EXTRA_MESSAGE);

        userName = (EditText) findViewById(R.id.aep_editUserName);
        userEmail = (EditText) findViewById(R.id.aep_editUserEmail);
        userCourse = (EditText) findViewById(R.id.aep_editUserCourse);
        //userDeets = (TextView) findViewById(R.id.aep_editUserCourseLabel);
        //userDeetsInput = (EditText) findViewById(R.id.aep_editUserName);
        String input = userName.getText().toString();
        //userAbout = (EditText) findViewById(R.id.aep_editUserAbout);
        //saorlasText = (TextView) findViewById(R.id.saorlasText);
        //dbHandler = new MyDBHandler(this, null, null, 2);
        //printDB();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        mImageView = (ImageView) findViewById(R.id.aep_insert_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }

    //public void Update()
    //{
    //    //MainMenu obj = new MainMenu();
    //    TextView tv = getTextView();
    //    tv.setText("hello");
//
    //  }



    //public void Update(){
    //    TextView txtView = (TextView) ((ProfileActivity)context).findViewById(R.id.aep_user_deets);
    //   txtView.setText("Hello");
    //}


    public void fillUserDetails(){

        String input = userName.getText().toString();
        userName.setText(input);
        Intent myIntent = new Intent(ProfileEditActivity.this, ProfileActivity.class);
        myIntent.putExtra("user_name", input);
        startActivity(myIntent);


        //String existing = userDeets.getText().toString();
        String tt_text;
        if (input !=""){
            tt_text = (input);}
        else {tt_text = ("New Text");}
        //Crete Toast;
        Context context = getApplicationContext();
        CharSequence text = (tt_text);
        int duration = Toast.LENGTH_SHORT;

        Toast toast_set = Toast.makeText(context, text, duration);
        toast_set.setGravity(Gravity.TOP, 0, 200);

        View toast_view = toast_set.getView();
        toast_view.setBackgroundResource(R.drawable.border_background_reverse);
        toast_set.setView(toast_view);


        toast_set.show();

        //userDeets.setText(tt_text);
    }

    public void reloadProfile() {


        userName.setHint(input);

        Intent myIntent = new Intent(ProfileEditActivity.this, ProfileActivity.class);
        myIntent.putExtra("user_name", input);
        startActivity(myIntent);

        Intent intent = new Intent(this, ProfileActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, input);
        startActivity(intent);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //action when corresponding action-bar item is clicked
        switch(item.getItemId()) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;


            case R.id.save:
                //fillUserDetails();
                reloadProfile();
                //startActivity(new Intent(this, ProfileActivity.class));
                //NavUtils.navigateUpFromSameTask(this);
                //userDeets.setText("ok");
                //Update();
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

    //    TODO ******** ENSURE CORRECT ACTIVITY NAME******************

    /** Called when the user clicks the Search Events quick-link */
    public void goToEvents() {
        Intent intent = new Intent(this, EventList.class);
        //intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
    }

    /** Called when the user clicks the Create Events quick-link */
    public void goToCreate() {
        Intent intent = new Intent(this, CreateEvent.class);
        //intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
    }

    /** Called when the user clicks the Search Recipe quick-link */
    public void goToRecipe() {
        Intent intent = new Intent(this, RecipeFinder.class);
        //intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
    }

    //public void printDB(){
    //    String dbString = dbHandler.databaseToString();
    //    saorlasText.setText(dbString);
    //    saorlasInput.setText("");
    //}

    //public void saveUpdates(View v){
    //Users users = new Users(saorlasInput.getText().toString(), first.getText().toString(), second.getText().toString(), email.getText().toString());
    //dbHandler.addUser(users);
    //Log.i(AGAIN, "GO ON!");
    //printDB();
    //}

    //public void deleteButtonClicked(View view){
    //    // dbHandler delete needs string to find in the db
    //    String inputText = first.getText().toString();
    //    dbHandler.deleteUser(inputText);
    //    printDB();
    //}
    private static final int CAMERA_REQUEST = 1;
    public void updateProfilePic(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            fillUserDetails();
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap == null){
                fillUserDetails();
            }
            mImageView.setImageBitmap(imageBitmap);
        }
    }

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



}