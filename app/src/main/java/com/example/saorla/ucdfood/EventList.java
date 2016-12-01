package com.example.saorla.ucdfood;

/**
 * Created by shauna on 06/11/2016.
 */

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.saorla.ucdfood.R.layout.activity_eventlist;


public class EventList extends AppCompatActivity implements AsyncResponse{
    private final String EL_LOG = "PLEASE WORK!";
    private ArrayAdapter<String> eventAdapter;
    public final static String EXTRA_MESSAGE = "com.example.saorla.ucdfood.MESSAGE";
    MyDBHandler helper = new MyDBHandler(this);
    ListView lv;
    public final static String DETAILS_MESSAGE = "event_details";
    public final static String TITLE_MESSAGE = "event_title";
    public final static String HOST_MESSAGE = "host_details";
    public final static String TIME_MESSAGE = "time";
    public final static String DATE_MESSAGE = "date";
    public final static String ADDRESS_MESSAGE = "address";
    public final static String IS_ATTENDING_MESSAGE = "is_attending";
    public final static String CAN_ATTEND_MESSAGE = "can_attend";
    String[] host;
    String[] time;
    String[] title;
    String[] description;
    String[] is_attending;
    String[] can_attend;
    String[] points;
    String[] address;
    String[] date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GetEvents getEvents = new GetEvents();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventlist);
        eventAdapter = new ArrayAdapter<String>(this, R.layout.custom_event_layout, R.id.eventTextView);



//        responseView = (TextView) findViewById(R.id.responseView);
//        Button recipeButton = (Button) findViewById(R.id.recipeButton);
        lv = (ListView) findViewById(R.id.eventList);
        lv.setAdapter(eventAdapter);
//        new GetEvents().execute();
        getEvents.delegate = this;
        getEvents.execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
//                Toast.makeText(getApplicationContext(), eventDetailList[pos], Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), EventDetail.class);
                intent.putExtra(TITLE_MESSAGE, host[pos]);
                intent.putExtra(HOST_MESSAGE,title[pos]);
                intent.putExtra(DETAILS_MESSAGE, description[pos]);
                intent.putExtra(DETAILS_MESSAGE, date[pos]);
                intent.putExtra(DETAILS_MESSAGE, address[pos]);
                intent.putExtra(DETAILS_MESSAGE, is_attending[pos]);
                intent.putExtra(DETAILS_MESSAGE, can_attend[pos]);
                intent.putExtra(DETAILS_MESSAGE, points[pos]);
                startActivity(intent);
            }
        });

    }

    @Override
    public ArrayList<String[]> processFinish(ArrayList<String[]> output){return output;}

    class GetEvents extends AsyncTask<Void, Void, ArrayList<String[]>> {
        private Exception exception;

        public AsyncResponse delegate = null;

        @Override
        protected void onPreExecute() {
            //make progress bar visible
//            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<String[]> doInBackground(Void...params) {
//            List<List> eventinfo = helper.eventinfo();
            ArrayList<String[]> eventsString = helper.eventinfostr();


//            String[] events = eventsString.get(0);
//            String[] time = eventsString.get(2);
//            String[] host = eventsString.get(1);
//            String[] description = eventsString.get(5);
//
//            ArrayList<String[]> eventstuff = new ArrayList<>();
//
//            eventstuff.add(events);
//            eventstuff.add(time);
//            eventstuff.add(host);
//            eventstuff.add(description);


            return eventsString;


        }

        protected void onPostExecute(ArrayList<String[]> response) {
            int row_size = response.size();
            int response_size = response.get(0).length;
            Log.i(EL_LOG, response_size+" THIS IS THE SIZE");

            host = new String[response_size];
            time = new String[response_size];
            title = new String[response_size];
            description = new String[response_size];
            date= new String[response_size];
            can_attend = new String[response_size];
            is_attending = new String[response_size];
            points = new String[response_size];
            address = new String[response_size];

            for (int i=0; i<response_size; i++) {
                title[i] = (response.get(0)[i]);
                time[i] = (response.get(1)[i]);
                host[i] = (response.get(2)[i]);
                description[i] = (response.get(3)[i]);
                date[i] = (response.get(4)[i]);
                address[i] = (response.get(5)[i]);
                can_attend[i] = (response.get(6)[i]);
                is_attending[i] = (response.get(7)[i]);
//                points[i] = (response.get(8)[i]);

                Log.i(EL_LOG, response.get(0)[i]);
                Log.i(EL_LOG, response.get(1)[i]);
                Log.i(EL_LOG, response.get(2)[i]);

//                String title_what = response.get(0)[i];
                eventAdapter.add(title[i]);
            }

            Log.i(EL_LOG, "EVENTADAPTER IS "+eventAdapter);
            Log.i(EL_LOG, "IN ON POST ***********");
//            delegate.processFinish(response);
        }
    }

}

interface AsyncResponse{
    ArrayList<String[]> processFinish(ArrayList<String[]> output);
}