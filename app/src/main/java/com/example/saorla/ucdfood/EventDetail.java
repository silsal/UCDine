package com.example.saorla.ucdfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
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
//    private static Button button_attend;
//    public final static String PICTURE_MESSAGE = "event_pic";
//    public final static String TITLE_MESSAGE = "event_title";
//    public final static String HOST_MESSAGE = "event_host";
//    public final static String DETAILS_MESSAGE = "event_details";
//    public int points = 3;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.event_detail);
//        onButtonClickListener();
//        String title ="";
//        String host = "";
//        String details="";
//        Intent intent = getIntent();
//        if (null != intent) {
//            title = intent.getStringExtra(MainActivity.TITLE_MESSAGE);
//            host = intent.getStringExtra(MainActivity.HOST_MESSAGE);
//            details = intent.getStringExtra(MainActivity.DETAILS_MESSAGE);
//        }
//
//        TextView titleTxt = (TextView) findViewById(R.id.title);
//        titleTxt.setText(title);
//
//        TextView hostTxt = (TextView) findViewById(R.id.host);
//        hostTxt.setText("Hosted by : " + host);
//
//        TextView descriptionTxt = (TextView) findViewById(R.id.description);
//        descriptionTxt.setText(details);
//
//        TextView dateTxt = (TextView) findViewById(R.id.Date);
//        descriptionTxt.setText("Date : "+ host);
//        TextView timeTxt = (TextView) findViewById(R.id.Time);
//        descriptionTxt.setText("Time : "+ host);
//        TextView addressTxt = (TextView) findViewById(R.id.address);
//        descriptionTxt.setText("Address : "+ host);
//
//
//// confirmation alert box
//    }
//    public void onButtonClickListener() {
//        button_attend = (Button) findViewById(R.id.attend);
//        button_attend.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog.Builder a_builder = new AlertDialog.Builder(eventDetail.this);
//                        a_builder.setMessage("Are you sure you want to attend this event? One point will be deducted from you").setCancelable(false)
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (points < 1){
//                                            Toast.makeText(getApplicationContext(), "Sorry, you dont have any points left on your account, try hosting an event to earn points!", Toast.LENGTH_LONG).show();
//                                        }else {
//                                            points -= 1;
//                                            Toast.makeText(getApplicationContext(), "One point has been deducted from your account", Toast.LENGTH_LONG).show();
//                                            dialog.cancel();
//                                        }}
//                                })
//                                .setNegativeButton("No",new DialogInterface.OnClickListener(){
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog alert = a_builder.create();
//                        alert.setTitle("Confirmation");
//                        alert.show();
//                    }
//                }
//        );
//
//    }
//    public void startMap(View view) {
//        Intent intent = new Intent(this, MapsActivity.class);
//        startActivity(intent);
//    }
}
