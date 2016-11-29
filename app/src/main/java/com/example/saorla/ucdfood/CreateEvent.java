package com.example.saorla.ucdfood;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TimePicker;
import java.util.UUID;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Collections;
import java.util.List;


/**
 * Created by Cometa on 06/11/2016.
 */
public class CreateEvent extends AppCompatActivity implements View.OnClickListener {
    public final static String EXTRA_MESSAGE = "com.example.saorla.ucdfood.MESSAGE";
    public String[] stringArray(String string_name){
        String[] strArray = string_name.split(" ");
        return strArray;
    }
    String strName = "name this tune";
    String name = stringArray(strName)[0];
    String user_name = name;

    private final String EV_LOG = "Silvia's message!";
    private EditText Textdate,
                     Texthour,
                     Textevent,
                     Textlocation,
                     TextNoPeople,
                     Textdescription;
    private String date,
                   hour,
                   event,
                   location,
                   description;

    int noPeople;
    MyDBHandler helperevent = new MyDBHandler(this);
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private int  mHour,mMinute;
    private TimePickerDialog timePicker;
    private ImageButton addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

        findViewsById();
        setDateField();
        setTimeField();

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
                    // DO Something here for first option (like call a function / Show a Toast etc)
                    takePicture(null);
                }

                if (which == 1) {
                    pickGallery(null);
                }


            }
        });
        final AlertDialog dialog = builder.create();
        addImage = (ImageButton) findViewById(R.id.camera);

        addImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    /**
     * Called when the user clicks the Send button
     */

    private static final int REQUEST_IMAGE_CAPTURE = 1;



    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //Take a picc and pass result to onActivityResult
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
//        else {
//            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
//                    .show();
//        }
    }

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
//        super.onActivityResult(requestCode, resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //get the photo
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap)extras.get("data");
            saveToInternalStorage(photo);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                // Log.d(TAG, String.valueOf(bitmap));
//
////                ImageView imageView = (ImageView) findViewById(R.id.aep_insert_image);
////                imageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir

        File mypath=new File(directory,UUID.randomUUID().toString()+".jpg");

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
//    public final static String EVENT_TITLE = "com.example.saorla.ucdfood.MESSAGE";

    /**
     * Called when the user clicks the Send button
     */
//    public void sendMessage(View view) {
//        Intent intent = new Intent(this, printEventFields.class);
//        EditText editText = (EditText) findViewById(R.id.eventTitle);
////        transform the message to string
//        String message = editText.getText().toString();
////        adds the EditText's value to the intent. An Intent can carry data types as key-value pairs called extras.
//// Your key is a public constant EXTRA_MESSAGE because the next activity uses the key to retrive the text value.
//        intent.putExtra(EVENT_TITLE, message);
//        startActivity(intent);
//    }



    private void findViewsById() {

        Textdate = (EditText) findViewById(R.id.date);
        Textdate.requestFocus();
        Textdate.setTextIsSelectable(true);
//        dateText.setInputType(InputType.TYPE_NULL);

        Texthour = (EditText) findViewById(R.id.hour);
        Texthour.requestFocus();
        Texthour.setTextIsSelectable(true);

        Textevent = (EditText) findViewById(R.id.eventTitle);
        Textlocation = (EditText) findViewById(R.id.location);
        TextNoPeople = (EditText) findViewById(R.id.people);
        Textdescription = (EditText) findViewById(R.id.description);
    }

    // Store form values into corresponding variables
    private void getFormValues(){
        Log.i(EV_LOG,"getting form values");
        date = Textdate.getText().toString();
        hour = Texthour.getText().toString();
        event = Textevent.getText().toString();
        location = Textlocation.getText().toString();
        noPeople = Integer.parseInt(TextNoPeople.getText().toString());
        description = Textdescription.getText().toString();
    }

        //clear fields
    public void clearFields(){
        Textdate.getText().clear();
        Texthour.getText().clear();
        Textevent.getText().clear();
        Textlocation.getText().clear();
        TextNoPeople.getText().clear();
        Textdescription.getText().clear();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.date:
                datePicker.show();
                break;
            case R.id.hour:
                timePicker.show();
                break;
//
        }
    }
    public void setDateField() {
        Textdate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Textdate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


    public void setTimeField(){

        Texthour.setOnClickListener(this);
        // Process to get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        timePicker = new TimePickerDialog(this, new OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,int minutes) {
                // Display Selected time in textbox
                Texthour.setText(hourOfDay + ":" + minutes);
            }
        }, mHour, mMinute, false);}

        public void onAddEventClicked(View view){
            Log.i(EV_LOG,"I am here!");
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType("vnd.android.cursor.item/event");

            getFormValues();
//            long startTime = hour.toString().getTimeInMillis();
//

            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,date);
            intent.putExtra(CalendarContract.Events.DTSTART,hour);


            intent.putExtra(CalendarContract.Events.TITLE, event);
            intent.putExtra(CalendarContract.Events.DESCRIPTION,  description);
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);


            startActivity(intent);
        }


        public String getIdfromSharedPreference(){
            SharedPreferences prefs = getSharedPreferences("User_Id",0);
            String extractedText =  prefs.getString("shared_ref_id","No ID found");

            return extractedText;
        }

    public void sendEvent(View v){

        getFormValues();

        //insert the details into db
//        SharedPreferences prefs = getSharedPreferences("User_Id",0);
        String user = getIdfromSharedPreference();
        int id = Integer.parseInt(user);

        Events e = new Events();

        e.set_hid(id);
        e.setInvite_num(noPeople);
        e.setAvailable_num(noPeople);
        e.setEvent_name(event);
        e.setAddress(location);
        e.setDescription(description);
        e.setTime(hour);
        e.setDate(date);
//        e.setEvent_pic();
        helperevent.addEvent(e);
        helperevent.getPoints(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.event_list_menu, menu);
        return true;
    }
    /** Called when the user clicks the Profile quick-link */
    public void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
    }

    /** Called when the user clicks the Search Recipe quick-link */
    public void goToEvents() {
        Intent intent = new Intent(this, EventList.class);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //action when corresponding action-bar item is clicked
        switch(item.getItemId()) {

            case R.id.search_events_ql:
                goToEvents();
                return true;

            case R.id.profile_ql:
                goToProfile();
                return true;

            case R.id.search_recipe_ql:
                goToRecipe();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }




