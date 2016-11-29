package com.example.saorla.ucdfood;

/**
 * Created by Paudi on 18/11/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_HOST_ID;
import static com.example.saorla.ucdfood.MyDBHandler.COLUMN_HOST_SCORE;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_EVENTS;
import static com.example.saorla.ucdfood.MyDBHandler.TABLE_USERS;



public class FragmentActivityStats extends Fragment{
    public  FragmentActivityStats(){
    }
    int userID;
    MyDBHandler dbHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootViewActivity = inflater.inflate(R.layout.fragment_view_activity_stats, container, false);

//        userID = getPreferenceFunction();
        userID = 0;
        dbHandler = new MyDBHandler(getActivity().getApplicationContext());

        //Get values from database to populate the arrays
        //Hosted
        String hosted = populateCountDetails(TABLE_EVENTS, COLUMN_HOST_ID, COLUMN_HOST_ID, userID);
        String h_rating = populateDetails(TABLE_USERS, COLUMN_HOST_SCORE, userID);
        //String h_reviews = populateCountDetails(TABLE_REVIEWS, COLUMN_HOST_ID, COLUMN_HOST_ID, userID);
        String h_reviews = "3";

        //Attended
//        String attended = populateDetails(TABLE_USERS, COLUMN_ATTENDED, userID);
//        String a_rating = populateDetails(TABLE_USERS, COLUMN_GUEST_SCORE, userID);
//        String a_reviews = populateCountDetails(TABLE_REVIEWS, COLUMN_GUEST_ID, COLUMN_GUEST_ID, userID);
        String attended = "4";
        String a_rating = "7";
        String a_reviews = "1";

        //Create Placeholder HOSTING Data
        String[] hostInfoArray = {
                "Hosted: " + hosted + "",
                "Host Rating: " + h_rating + "/10",
                "Host Reviews: " + h_reviews + ""
        };

        //Create Placeholder ATTENDING Data
        String[] attendInfoArray = {
                "Attended " + attended + "",
                "Guest Rating: "+ a_rating + "/10",
                "Guest Reviews: " + a_reviews +""
        };

        //Convert the String array into a ListArray
        List<String> hostStats = new ArrayList<String>(Arrays.asList(hostInfoArray));
        List<String> attendStats = new ArrayList<String>(Arrays.asList(attendInfoArray));

        //Create an Array Addapter which is where the List Array will be bound to. Eg binding the list to another Activity(s)
        ArrayAdapter<String> hostAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_view_activity_stats_list, //name of xml file (Activity) that the <TextView> element is in
                R.id.list_item_activity_textview, //name (id) of the <TextView> element
                hostStats);

        ArrayAdapter<String> attendAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_view_activity_stats_list, //name of xml file (Activity) that the <TextView> element is in
                R.id.list_item_activity_textview, //name (id) of the <TextView> element
                attendStats);

        //Create A List View for the <ListView> element in an activity (here: fragment_view_activity_stats)
        ListView hostListView = (ListView) rootViewActivity.findViewById(R.id.fv_listview_host_activity);
        ListView attendListView = (ListView) rootViewActivity.findViewById(R.id.fv_listview_attend_activity);

        //Link the Created List-View to the Array Adapter
        hostListView.setAdapter(hostAdapter);
        attendListView.setAdapter(attendAdapter);

        return rootViewActivity;
    }


    public String populateCountDetails(String Table, String CountColumnName, String WhereColumnName, int WhereEqualsValue){
        return dbHandler.databaseCountByIDToString(Table, CountColumnName, WhereColumnName, WhereEqualsValue);
    }

    public String populateDetails(String Table, String Column, int ID){
        return dbHandler.databaseSelectByIDToString(Table, Column, ID);
    }
}
