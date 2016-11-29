package com.example.saorla.ucdfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saorla.ucdfood.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shauna on 15/11/2016.
 */
public class EventAdapter extends ArrayAdapter<String> {
//    int[] eventImageList = {};
//    String[] eventTitleList = {};
//    String[] eventHostList = {};
//    String[] eventTimeList = {};
    List<String> eventTitleList = new ArrayList<String>();
    List<String> eventHostList = new ArrayList<String>();
    List<String> eventTimeList = new ArrayList<String>();



    Context c;
    LayoutInflater inflater;

//    public EventAdapter(Context context, String[] eventTitleList, String[] eventHostList, String[] eventTimeList, int[] eventImageList) {
    public EventAdapter(Context context, List<String> eventTitleList, List<String> eventHostList, List<String> eventTimeList) {

        super(context, R.layout.custom_event_layout, eventTitleList);

        this.c = context;
        this.eventTitleList = eventTitleList;
        this.eventHostList = eventHostList;
        this.eventTimeList = eventTimeList;
//        this.eventImageList = eventImageList;
    }

    //    class which holds the view for each row
    public class viewHolder {
        TextView titleTV;
        TextView hostTV;
        TextView timeTV;
//        ImageView imageTV;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        if (convertView == null) {
//            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = inflater.inflate(R.layout.custom_event_layout, null);
//        }
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v;
        if (convertView == null) {
            v = inflater.inflate(R.layout.custom_event_layout, null);

        } else {
            v = convertView;
        }
        //viewholder object
        final viewHolder holder = new viewHolder();

        //initialise viewholder object
        holder.titleTV = (TextView) v.findViewById(R.id.eventTextView);
        holder.hostTV = (TextView) v.findViewById(R.id.hostTextVIew);
        holder.timeTV = (TextView) v.findViewById(R.id.timeTextVIew);
//        holder.imageTV = (ImageView) v.findViewById(R.id.eventImageView);

        //assign the data to the view
//        holder.imageTV.setImageResource(eventImageList[position]);
        holder.titleTV.setText(eventTitleList.get(position));
        holder.hostTV.setText(eventHostList.get(position));
        holder.timeTV.setText(eventTimeList.get(position));

        return  v;
    }
}

