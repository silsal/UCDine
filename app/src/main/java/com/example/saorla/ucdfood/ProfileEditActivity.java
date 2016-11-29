package com.example.saorla.ucdfood;

/**
 * Created by Paudi on 09/11/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.io.File;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.widget.Toast.LENGTH_SHORT;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_BIO;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_COURSE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_EMAIL;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_USERNAME;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_USERS;
//import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ProfileEditActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.saorla.ucdfood.MESSAGE";

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
    //String path = getFilesDir().getAbsolutePath();
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        String message = intent.getStringExtra(ProfileActivity.EXTRA_MESSAGE);

        Resources res = getResources();
        dbHandler = new MyDBHandler(this);
        //        userID = getSharedPreferenceFunction();
        userID = 0;


        //Get the views
        userName = (EditText) findViewById(R.id.aep_editUserName);
        userEmail = (EditText) findViewById(R.id.aep_editUserEmail);
        userCourse = (EditText) findViewById(R.id.aep_editUserCourse);
        userBio = (EditText) findViewById(R.id.aep_editUserBio);
        //Set the Hints
        setHint(TABLE_USERS, COLUMN_USERNAME, userName, String.format(res.getString(R.string.user_name)));
        setHint(TABLE_USERS, COLUMN_EMAIL, userEmail,  String.format(res.getString(R.string.user_email),"email?"));
        setHint(TABLE_USERS, COLUMN_COURSE, userCourse,  String.format(res.getString(R.string.user_course),"course?"));
        setHint(TABLE_USERS, COLUMN_BIO, userBio,  String.format(res.getString(R.string.user_bio)));


        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        mImageView = (ImageView) findViewById(R.id.aep_insert_image);




        /**
         * open dialog for choose camera
         */

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
                    //Crete Toast;
//                    Context context = getApplicationContext();
//                    CharSequence text = ("take pic");
//                    int duration = LENGTH_SHORT;
//
//                    Toast toast_pic = Toast.makeText(context, text, duration);
//                    toast_pic.setGravity(Gravity.TOP, 0, 200);
//
//                    View toast_view = toast_pic.getView();
//                    toast_view.setBackgroundResource(R.drawable.border_background_reverse);
//                    toast_pic.setView(toast_view);
//
//
//                    toast_pic.show();
                    updateProfilePic(null);
                }

                if (which == 1) {
                    pickGallery(null);
                    Toast.makeText(getApplicationContext(),"Gallery" , LENGTH_SHORT);
                }


            }
        });
        final AlertDialog dialog = builder.create();
        addImage = (ImageButton) findViewById(R.id.aep_behindImageButton);

        //Disable the button if the device has no camera
        if(!hasCamera()){
            addImage.setEnabled(false);
            Toast.makeText(this,"Feature not available: NO DEVICE CAMERA",Toast.LENGTH_LONG).show();

        }

        addImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });



    }

    public boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
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

    public void setHint(String Table, String Column, EditText view, String default_hint) {
        String hint = populateDetails(Table, Column, userID);
        if (hint.length() >0){
            view.setHint(hint);
        }
        else {view.setHint(default_hint);}
    }

    public void updateUserName() {
        String name_input = userName.getText().toString();
        if (name_input.length() >0){
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_USERNAME, name_input, userID);
            updateMsg.concat("User Name ");
        }
    }

    public void updateUserEmail() {
        String email_input = userEmail.getText().toString();
        if (email_input.length() >0){
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_EMAIL, email_input, userID);
            updateMsg.concat("User Email ");
        }
    }

    public void updateUserCourse() {
        String course_input = userCourse.getText().toString();
        if (course_input.length() >0){
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_EMAIL, course_input, userID);
            updateMsg.concat("User Course ");
        }
    }

    public void updateUserBio() {
        String bio_input = userBio.getText().toString();
        if (bio_input.length() >0){
            dbHandler.databaseUpdateByIDToString(TABLE_USERS, COLUMN_EMAIL, bio_input, userID);
            updateMsg.concat("User Bio ");
        }
    }

    public void saveAllUpdates(){
        updateUserName();
        updateUserEmail();
        updateUserCourse();
        updateUserBio();
        if (updateMsg.equals("Updated: ")){
            updateMsg.concat("No Updates!");
        }
    }

    public String populateDetails(String Table, String Column, int ID){
        return dbHandler.databaseSelectByIDToString(Table, Column, ID);
    }

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
        int duration = LENGTH_SHORT;

        Toast toast_set = Toast.makeText(context, text, duration);
        toast_set.setGravity(Gravity.TOP, 0, 200);

        View toast_view = toast_set.getView();
        toast_view.setBackgroundResource(R.drawable.border_background_reverse);
        toast_set.setView(toast_view);


        toast_set.show();

        //userDeets.setText(tt_text);
    }

    public void reloadProfile() {


//        userName.setHint(input);
//
//        Intent myIntent = new Intent(ProfileEditActivity.this, ProfileActivity.class);
//        myIntent.putExtra("user_name", input);
//        startActivity(myIntent);

        Intent intent = new Intent(this, ProfileActivity.class);
        //intent.putExtra(EXTRA_MESSAGE, input);
        startActivity(intent);
        finish();
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
                saveAllUpdates();
                Toast.makeText(getApplicationContext(),updateMsg , LENGTH_SHORT);
                //Toast.makeText(getApplicationContext(), dbHandler.databaseSelectByIDToString(TABLE_USERS,COLUMN_USERNAME,0),Toast.LENGTH_LONG).show();
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
    private static final int CAMERA_REQUEST = 1337;

    public Uri imageToUploadUri;

    public void updateProfilePic(View view) {
//        public void updateProfilePic() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File f = new File(Environment.getExternalStorageDirectory(), "PROFILE_IMAGE.jpg");
//        File f = new File(getApplicationContext().getFilesDir(), "PROFILE_IMAGE.jpg");


        File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));

        imageToUploadUri = Uri.fromFile(f);

//        output=new File(dir, "CameraContentDemo.jpeg");
//        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent data) {
//        if (requestCode == CAMERA_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Intent i=new Intent(Intent.ACTION_VIEW);
//
//                i.setDataAndType(Uri.fromFile(output), "image/jpeg");
//                startActivity(i);
//                finish();
//            }
//        }
//    }

//    ****
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            fillUserDetails();
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            if (imageBitmap == null){
//                fillUserDetails();
//            }
//            mImageView.setImageBitmap(imageBitmap);
//        }
//    }

    private int PICK_IMAGE_REQUEST = 1;
    public void pickGallery(View view) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.aep_insert_image);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//            //fillUserDetails();
//            Bundle extras = data.getExtras();
//
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            if (imageBitmap == null){
//                //fillUserDetails();
//            }
//            ImageView imageView = (ImageView) findViewById(R.id.aep_insert_image);
//            imageView.setImageBitmap(imageBitmap);
//
//            //mImageView.setImageBitmap(imageBitmap);
//        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            if(imageToUploadUri != null){
                Uri selectedImage = imageToUploadUri;
                getContentResolver().notifyChange(selectedImage, null);
                Bitmap reducedSizeBitmap = getBitmap(imageToUploadUri.getPath());
                if(reducedSizeBitmap != null){
                    ImageView imageView = (ImageView) findViewById(R.id.aep_insert_image);
                    imageView.setImageBitmap(reducedSizeBitmap);

//                    ImgPhoto.setImageBitmap(reducedSizeBitmap);
//                    Button uploadImageButton = (Button) findViewById(R.id.uploadUserImageButton);
//                    uploadImageButton.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(this,"Stg_2: Error while capturing Image",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this,"Stg_1: Error while capturing Image",Toast.LENGTH_LONG).show();
            }
        }



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



}