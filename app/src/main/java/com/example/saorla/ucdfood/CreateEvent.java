package com.example.saorla.ucdfood;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.InputType;
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
    private EditText date;
    public EditText hour;
    private EditText event;
    private EditText location;
    private EditText noPeople;
    private EditText description;
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


    public final static String EVENT_TITLE = "com.example.saorla.ucdfood.MESSAGE";

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

        date = (EditText) findViewById(R.id.date);
        date.requestFocus();
        date.setTextIsSelectable(true);
//        dateText.setInputType(InputType.TYPE_NULL);

        hour = (EditText) findViewById(R.id.hour);
        hour.requestFocus();
        hour.setTextIsSelectable(true);

        event = (EditText) findViewById(R.id.eventTitle);
        location = (EditText) findViewById(R.id.location);
        noPeople = (EditText) findViewById(R.id.people);
        description = (EditText) findViewById(R.id.description);
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
        date.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


    public void setTimeField(){
        hour.setOnClickListener(this);
        // Process to get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        timePicker = new TimePickerDialog(this, new OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay,int minutes) {
                // Display Selected time in textbox
                hour.setText(hourOfDay + ":" + minutes);
            }
        }, mHour, mMinute, false);}

        public void onAddEventClicked(View view){
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType("vnd.android.cursor.item/event");


//            long startTime = hour.toString().getTimeInMillis();
//

            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,date.toString());
            intent.putExtra(CalendarContract.Events.DTSTART,hour.toString());


            intent.putExtra(CalendarContract.Events.TITLE, event.toString());
            intent.putExtra(CalendarContract.Events.DESCRIPTION,  description.toString());
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.toString());


            startActivity(intent);
        }
    }




