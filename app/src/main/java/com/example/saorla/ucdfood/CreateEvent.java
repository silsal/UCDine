package com.example.saorla.ucdfood;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.TimePicker;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Collections;
import java.util.List;


/**
 * Created by Cometa on 06/11/2016.
 */
public class CreateEvent extends AppCompatActivity implements View.OnClickListener {
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
//                    noPeople,
                   description;

    int noPeople;
    MyDBHandler helperevent = new MyDBHandler(this);
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private int  mHour,mMinute;
    private TimePickerDialog timePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

        findViewsById();
        setDateField();
        setTimeField();


    }

    /**
     * Called when the user clicks the Send button
     */

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
//        else {
//            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
//                    .show();
//        }
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


        public String getIdfromSharedPrefernece(){
            SharedPreferences prefs = getSharedPreferences("User_Id",0);
            String extractedText =  prefs.getString("shared_ref_id","No ID found");

            return extractedText;
        }

    public void sendEvent(View v){

        getFormValues();

        //insert the details into db
//        SharedPreferences prefs = getSharedPreferences("User_Id",0);
        String user = getIdfromSharedPrefernece();
        int id = Integer.parseInt(user);
//        Log.i(EV_LOG, "SENDING SENDING SENDING");
//        Events e = new Events();
////        e.set_eid(2);
//        e.set_hid(1);
//        e.setInvite_num(Integer.parseInt(TextNoPeople.getText().toString()));
//        e.setDate(Textdate.getText().toString());
//        e.setTime(Texthour.getText().toString());
//        e.setDescription(Textdescription.getText().toString());
//        e.setAddress(Textlocation.getText().toString());
//        Log.i(EV_LOG, e + "");
//        helperevent.addEvent(e);
//
//        String tableEvents = "TABLE_EVENTS";
//        String[] columnNames = {"COLUMN_HOST_ID",
//                                "COLUMN_INVITE_NUM",
//                                "COLUMN_EVENT_NAME",
//                                "COLUMN_ADDRESS",
//                                "COLUMN_DESCRIPTION",
//                                "COLUMN_TIME",
//                                "COLUMN_DATE"};
//        String[] values = {user,noPeople,event, location,description,hour,date};
//        Log.i(EV_LOG, values+"");
//        SQLiteDatabase db = getWritableDatabase())
        Events e = new Events();
//       values.put(COLUMN_EVENT_ID, events.get_eid());
        e.set_hid(id);
        e.setInvite_num(noPeople);
        e.setEvent_name(event);
        e.setAddress(location);
        e.setDescription(description);
        e.setTime(hour);
        e.setDate(date);
        helperevent.addEvent(e);
//        Log.i(EV_LOG, e+"");
//         helperevent.insertNewEvent(tableEvents,columnNames,values);
    }


    }




