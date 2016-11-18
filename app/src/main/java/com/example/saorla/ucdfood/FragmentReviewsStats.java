package com.example.saorla.ucdfood;

import android.content.res.Resources;
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

public class FragmentReviewsStats extends Fragment{
    public  FragmentReviewsStats(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootViewReviews = inflater.inflate(R.layout.fragment_view_reviews_stats, container, false);
        Resources res = getResources();
        String review_1 = String.format(res.getString(R.string.review_text_1));
        String review_2 = String.format(res.getString(R.string.review_text_2));
        String review_3 = String.format(res.getString(R.string.review_text_3));

        //Create Placeholder HOSTING Data
        String[] reviewInfoArray = {
                "Review 1:\n".concat(review_1),
                "Review 2:\n".concat(review_2),
                "Review 3:\n ".concat(review_3),
        };

        //Convert the String array into a ListArray
        List<String> reviewStats = new ArrayList<String>(Arrays.asList(reviewInfoArray));

        //Create an Array Adapter which is where the List Array will be bound to. Eg binding the list to another Activity(s)
        ArrayAdapter<String> reviewAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.fragment_view_activity_stats_list, //name of xml file (Activity) that the <TextView> element is in
                R.id.list_reviews_textview, //name (id) of the <TextView> element
                reviewStats);


        //Create A List View for the <ListView> element in an activity (here: fragment_forecast)
        ListView ReviewListView = (ListView) rootViewReviews.findViewById(R.id.fv_listview_reviews);

        //Link the Created List-View to the Array Adapter
        ReviewListView.setAdapter(reviewAdapter);

        return rootViewReviews;
    }


}