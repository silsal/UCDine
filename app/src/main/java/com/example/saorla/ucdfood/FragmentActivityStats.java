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

public class FragmentActivityStats extends Fragment{
    public  FragmentActivityStats(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootViewActivity = inflater.inflate(R.layout.fragment_view_activity_stats, container, false);

        //Create Placeholder HOSTING Data
        String[] hostInfoArray = {
                "Hosted: 7",
                "Host Rating: 6/10",
                "Host Reviews: 3"
        };

        //Create Placeholder ATTENDING Data
        String[] attendInfoArray = {
                "Attended 7",
                "Guest Rating: 6/10",
                "Guest Reviews: 3"
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


}
