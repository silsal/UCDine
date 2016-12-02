package com.example.saorla.ucdfood;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.ListFragment;

import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_DATE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_EVENT_ATTENDED_ID;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_EVENT_ID;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_EVENT_NAME;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_HOST_ID;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_HOST_SCORE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_INVITE_NUM;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_EVENTS;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_MY_EVENTS;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_USERS;

/**
 * Created by Paudi on 09/11/2016.
 */

public class FragmentEventsStats extends Fragment{


    public  FragmentEventsStats(){

    }

    int userID;
    MyDBHandler dbHandler;


    ImageButton deleteImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootViewEvents = inflater.inflate(R.layout.fragment_view_events_stats, container, false);

        //        userID = getPreferenceFunction();
        userID = Integer.parseInt(getIdfromSharedPreference());
        dbHandler = new MyDBHandler(getActivity().getApplicationContext());

//        //Get values from database to populate the arrays
//        //Hosted
        String[] h_titles = setInputs(populateEventDetails(TABLE_EVENTS, COLUMN_EVENT_NAME, COLUMN_HOST_ID, userID));
        String[] h_dates = setInputs(populateEventDetails(TABLE_EVENTS, COLUMN_DATE, COLUMN_HOST_ID, userID));
        String[] h_guests = setInputs(populateEventDetails(TABLE_EVENTS, COLUMN_INVITE_NUM, COLUMN_HOST_ID, userID));

//            String[] h_titles = new String[] {"Burger Bash", "Pasta Party", "French Flair"};
//
//        //Attended
        String[] a_titles = setInputs(populateEventAttdDetail(TABLE_EVENTS, COLUMN_EVENT_NAME, TABLE_MY_EVENTS, COLUMN_EVENT_ID, COLUMN_EVENT_ATTENDED_ID));
        String[] a_dates = setInputs(populateEventAttdDetail(TABLE_EVENTS, COLUMN_DATE, TABLE_MY_EVENTS, COLUMN_EVENT_ID, COLUMN_EVENT_ATTENDED_ID));
        String[] a_guests = setInputs(populateEventAttdDetail(TABLE_EVENTS, COLUMN_INVITE_NUM, TABLE_MY_EVENTS, COLUMN_EVENT_ID, COLUMN_EVENT_ATTENDED_ID));



        String[] hostEventArray = {
                ""+ h_titles[0] +"\n\n"+ h_dates[0] +"\n\nGuests: "+ h_guests[0] + "",
                ""+ h_titles[1] +"\n\n"+ h_dates[1] +"\n\nGuests: "+ h_guests[1] + "",
                ""+ h_titles[2] +"\n\n"+ h_dates[2] +"\n\nGuests: "+ h_guests[2] + "",
        };

        //Create Placeholder ATTENDING Data
        String[] attendEventArray = {
                ""+ a_titles[0] +"\n\n"+ a_dates[0] +"\n\nGuests: "+ a_guests[0] + "",
                ""+ a_titles[1] +"\n\n"+ a_dates[1] +"\n\nGuests: "+ a_guests[1] + "",
                ""+ a_titles[2] +"\n\n"+ a_dates[2] +"\n\nGuests: "+ a_guests[2] + "",
        };

        //Convert the String array into a ListArray
        List<String> hostEventStats = new ArrayList<String>(Arrays.asList(hostEventArray));
        List<String> attendEventStats = new ArrayList<String>(Arrays.asList(attendEventArray));

        //Create an Array Adapter which is where the List Array will be bound to. Eg binding the list to another Activity(s)
        ArrayAdapter<String> hostEventAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_view_events_stats_list, //name of xml file (Activity) that the <TextView> element is in
                R.id.list_item_events_textview, //name (id) of the <TextView> element
                hostEventStats);

        ArrayAdapter<String> attendEventAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_view_events_stats_list, //name of xml file (Activity) that the <TextView> element is in
                R.id.list_item_events_textview, //name (id) of the <TextView> element
                attendEventStats);

        //Create A List View for the <ListView> element in an activity (here: fragment_forecast)
        ListView hostEventListView = (ListView) rootViewEvents.findViewById(R.id.fv_listview_host_events);
        ListView attendEventListView = (ListView) rootViewEvents.findViewById(R.id.fv_listview_attend_events);

        //Link the Created List-View to the Array Adapter
        hostEventListView.setAdapter(hostEventAdapter);
        attendEventListView.setAdapter(attendEventAdapter);


        return rootViewEvents;


    }



    public String populateCountDetails(String Table, String CountColumnName, String WhereColumnName, String EqualityMeasure, int WhereEqualsValue){
        return dbHandler.databaseCountByIDToString(Table, CountColumnName, WhereColumnName, EqualityMeasure, WhereEqualsValue);
    }

    public String[] populateEventAttdDetail(String Table_1_Name, String ColumnNameSelect, String Table_2_Name, String Column_1_NameEquals, String Column_2_NameEquals){
        return dbHandler.databaseSelectJoinByIDToArray(Table_1_Name, ColumnNameSelect, Table_2_Name, Column_1_NameEquals, Column_2_NameEquals);
    }

    public String[] populateEventDetails(String Table, String ColumnSelect, String ColumnEquals, int ID){
        return dbHandler.databaseSelectByIDToArray(Table, ColumnSelect, ColumnEquals, ID);
    }

    public String[] setInputs(String[] strArray){
        int len = strArray.length;
        String[] strArrayOut = new String[3];

        if (len == 0){
            strArrayOut[0] = "no event";
            strArrayOut[1] = "no event";
            strArrayOut[2] = "no event";
        }
        if (len == 1) {
            strArrayOut[0] = strArray[0];
            strArrayOut[1] = "no event";
            strArrayOut[2] = "no event";

        }
        if (len == 2) {
            strArrayOut[0] = strArray[0];
            strArrayOut[1] = strArray[1];
            strArrayOut[2] = "no event";
        }
        if (len >=3) {
            strArrayOut[0] = strArray[0];
            strArrayOut[1] = strArray[1];
            strArrayOut[2] = strArray[2];
        }

        return strArrayOut;
    }

//**************************
    //GENERAL HELPER FUNCTIONS
    //**************************

    public String getIdfromSharedPreference(){
        SharedPreferences prefs = getActivity().getApplicationContext().getSharedPreferences("User_Id",0);
        String extractedText =  prefs.getString("shared_ref_id","No ID found");

        return extractedText;
    }


}
