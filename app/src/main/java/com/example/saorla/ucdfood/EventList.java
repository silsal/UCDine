package com.example.saorla.ucdfood;

/**
 * Created by shauna on 06/11/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


public class EventList extends AppCompatActivity {
    public final static String DETAILS_MESSAGE = "event_details";
    public final static String TITLE_MESSAGE = "event_title";
    public final static String HOST_MESSAGE = "host_details";
    private final String APP_LOG = "Saorla's message!";
    ListView lv;
    String[] eventTitleList={"Pizza Party", "Mexican Fiesta night", "Authentic Italian","5 course french feast"};
    String[] eventHostList={"Shauna26", "IanC87", "kimberly19","david66"};
    String[] eventTimeList={"2 days", "3 days", "6 days","2 weeks"};
    int[] eventImageList = {R.mipmap.pizzaparty, R.mipmap.mexican,R.mipmap.italian,R.mipmap.french};
    String[] eventDetailList = {"I am going to make peperoni pizza as well as margarita and a vegetarian pizza. I make my own base and sauce and hope you enjoy it"," 2 " +
            "I am going to make peperoni pizza as well as margarita and a vegetarian pizza. I make my own base and sauce and hope you enjoy it","I am going to make peperoni pizza as well as margarita and a vegetarian pizza. I make my own base and sauce and hope you enjoy it","I am going to make peperoni pizza as well as margarita and a vegetarian pizza. I make my own base and sauce and hope you enjoy it"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventlist);

        lv = (ListView) findViewById(R.id.eventList);

        EventAdapter adapter = new EventAdapter(this, eventTitleList, eventHostList, eventTimeList, eventImageList);
        Log.i(APP_LOG,adapter.getItem(0)+"");
        Log.i(APP_LOG,adapter.getItem(1)+"");
        Log.i(APP_LOG,adapter.getItem(2)+"");
        Log.i(APP_LOG,adapter.getItem(0)+"");
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
//                Toast.makeText(getApplicationContext(), eventDetailList[pos], Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), EventDetail.class);
                intent.putExtra(TITLE_MESSAGE, eventTitleList[pos]);
                intent.putExtra(HOST_MESSAGE, eventHostList[pos]);
                intent.putExtra(DETAILS_MESSAGE, eventDetailList[pos]);
                startActivity(intent);

            }
        });
    }

}

