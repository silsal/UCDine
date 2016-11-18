package com.example.saorla.ucdfood;

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

/**
 * Created by Paudi on 09/11/2016.
 */

public class FragmentEventsStats extends Fragment{
    public  FragmentEventsStats(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootViewEvents = inflater.inflate(R.layout.fragment_view_events_stats, container, false);

        //Create Placeholder HOSTING Data
        String[] hostInfoArray = {
                "T - 2\nWednesday 19th Oct\nGuests: 1\nGenre: English",
                "T - 4\nFriday 21st Oct\nGuests: 4\nGenre: Irish",
                "T - 6\nSunday 23th Oct\nGuests: 5\nGenre: Italian",
        };

        //Create Placeholder ATTENDING Data
        String[] attendInfoArray = {
                "T - 1\nTuesday 18th Oct\nGuests: 3\nGenre: French",
                "T - 3\nThursday 20th Oct\nGuests: 2\nGenre: Irish",
                "T - 6\nSunday 23th Oct\nGuests: 5\nGenre: Italian",
        };

        //Convert the String array into a ListArray
        List<String> hostStats = new ArrayList<String>(Arrays.asList(hostInfoArray));
        List<String> attendStats = new ArrayList<String>(Arrays.asList(attendInfoArray));

        //Create an Array Addapter which is where the List Array will be bound to. Eg binding the list to another Activity(s)
        ArrayAdapter<String> hostAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_view_activity_stats_list, //name of xml file (Activity) that the <TextView> element is in
                R.id.list_item_events_textview, //name (id) of the <TextView> element
                hostStats);

        ArrayAdapter<String> attendAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_view_activity_stats_list, //name of xml file (Activity) that the <TextView> element is in
                R.id.list_item_events_textview, //name (id) of the <TextView> element
                attendStats);

        //Create A List View for the <ListView> element in an activity (here: fragment_forecast)
        ListView hostListView = (ListView) rootViewEvents.findViewById(R.id.fv_listview_host_events);
        ListView attendListView = (ListView) rootViewEvents.findViewById(R.id.fv_listview_attend_events);

        //Link the Created List-View to the Array Adapter
        hostListView.setAdapter(hostAdapter);
        attendListView.setAdapter(attendAdapter);

        return rootViewEvents;
    }


}
