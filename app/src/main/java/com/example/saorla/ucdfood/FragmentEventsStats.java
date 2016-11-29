package com.example.saorla.ucdfood;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.ListFragment;

import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_DATE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_EVENT_NAME;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_HOST_ID;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_HOST_SCORE;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_INVITE_NUM;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_EVENTS;
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
        userID = 0;
        dbHandler = new MyDBHandler(getActivity().getApplicationContext());

//        //Get values from database to populate the arrays
//        //Hosted
//        String[] h_titles = populateEventDetail(TABLE_EVENTS, COLUMN_EVENT_NAME, COLUMN_HOST_ID, userID);
//        String[] h_dates = populateEventDetail(TABLE_EVENTS, COLUMN_DATE, COLUMN_HOST_ID, userID);
//        String[] h_guests = populateEventDetail(TABLE_EVENTS, COLUMN_INVITE_NUM, COLUMN_HOST_ID, userID);
//
//        public void setInputs(String[] strArray){
//            int len = strArray.length();
//            String[] strArrayOut;
//
//            if (len == 0){
//                strArrayOut = ["no event", "no event", "no event"];
//            }
//            if (len == 1) {
//                strArrayOut = [strArray[0], "no event", "no event"];
//            }
//            if (len == 2) {
//                strArrayOut = [strArray[0], strArray[1], "no event"];
//            }
//            if (len >=3) {
//                strArrayOut = [strArray[0], strArray[1], strArray[2]];
//            }
//        }

        //Attended
//




        String[] hostEventArray = {
                "T - 2\nTitle: Burger Bash\nWednesday 19th Oct\nGuests: 1",
                "T - 4\nTitle: Pasta Party\nFriday 21st Oct\nGuests: 4",
                "T - 6\nTitle: French Flair\nSunday 23th Oct\nGuests: 5",
        };

        //Create Placeholder ATTENDING Data
        String[] attendEventArray = {
                "T - 1\nTitle: BBQ Blitz\nTuesday 18th Oct\nGuests: 3",
                "T - 3\nTitle: Spaintastic\nThursday 20th Oct\nGuests: 2",
                "T - 6\nTitle: Bit of Ita\nSunday 23th Oct\nGuests: 5",
        };

        //Convert the String array into a ListArray
        List<String> hostEventStats = new ArrayList<String>(Arrays.asList(hostEventArray));
        List<String> attendEventStats = new ArrayList<String>(Arrays.asList(attendEventArray));

        //Create an Array Addapter which is where the List Array will be bound to. Eg binding the list to another Activity(s)
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


//        ListView button = (ListView) rootViewEvents.findViewById(R.id.fv_listview_host_events);
//        button.setOnItemClickListener(new View.OnItemClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Toast.makeText(getActivity().getApplicationContext(), "tester", Toast.LENGTH_SHORT).show();
//            }
//        });


        return rootViewEvents;

//        deleteImage = (ImageButton) getActivity().findViewById(R.id.delete_btn);
//
//        deleteImage.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity().getApplicationContext(), "tester", Toast.LENGTH_SHORT).show();
//            }
//        });


    }



        //Create Toast
//        Context context = getActivity().getApplicationContext();
////        Context context = this;
//        CharSequence cancelText = ("Event Cancelled");
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast_cancel = Toast.makeText(context, cancelText, duration);
//        toast_cancel.setGravity(Gravity.TOP, 0, 200);
//
//        View toast_view = toast_cancel.getView();
//        toast_view.setBackgroundResource(R.drawable.border_background_reverse);
//        toast_cancel.setView(toast_view);

    public String populateCountDetails(String Table, String CountColumnName, String WhereColumnName, int WhereEqualsValue){
        return dbHandler.databaseCountByIDToString(Table, CountColumnName, WhereColumnName, WhereEqualsValue);
    }

    public String populateDetails(String Table, String Column, int ID){
        return dbHandler.databaseSelectByIDToString(Table, Column, ID);
    }




}
