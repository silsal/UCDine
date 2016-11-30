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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.saorla.ucdfood.R.layout.activity_eventlist;


public class EventList extends AppCompatActivity implements AsyncResponse{


    public final static String EXTRA_MESSAGE = "com.example.saorla.ucdfood.MESSAGE";
    MyDBHandler helper = new MyDBHandler(this);
    String table_events = "TABLE_EVENTS";
    String[] columnNames = {"COLUMN_HOST_ID",
            "COLUMN_INVITE_NUM",
            "COLUMN_EVENT_NAME",
            "COLUMN_ADDRESS",
            "COLUMN_DESCRIPTION",
            "COLUMN_TIME",
            "COLUMN_DATE"};
    private TextView Textdate,
            Texthour,
            Textevent,
            TextHost,
            Textlocation,
            TextNoPeople,
            Textdescription;
    private String date,
            hour,
            event,
            location,
    //                    noPeople,
    description;
    int noPeople;
    int host;
    MyDBHandler helperevent = new MyDBHandler(this);
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;
    private int  mHour,mMinute;
    private TimePickerDialog timePicker;


    public final static String DETAILS_MESSAGE = "event_details";
    public final static String TITLE_MESSAGE = "event_title";
    public final static String HOST_MESSAGE = "host_details";
    ListView lv;



    private void findViewsById() {
        Textevent = (TextView) findViewById(R.id.eventTextView);
        TextHost = (TextView) findViewById(R.id.hostTextVIew);
        Textdescription = (TextView) findViewById(R.id.description);
    }
    public String getIdfromSharedPrefernece(){
        SharedPreferences prefs = getSharedPreferences("_eid",0);
        String extractedText =  prefs.getString("shared_ref_id","No ID found");

        return extractedText;
    }

    // Store form values into corresponding variables
    private void getFormValues(){
        event = Textevent.getText().toString();
        host = Integer.parseInt(TextHost.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GetEvents getEvents = new GetEvents();
        super.onCreate(savedInstanceState);
        setContentView(activity_eventlist);
        lv = (ListView) findViewById(R.id.eventList);
//        new GetEvents().execute();
        getEvents.delegate = this;
        getEvents.execute();

        final ArrayList<String[]> eventslist = (ArrayList<String[]>) getEvents.delegate;


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
//                Toast.makeText(getApplicationContext(), eventDetailList[pos], Toast.LENGTH_LONG).show();
                

                Intent intent = new Intent(getApplicationContext(), EventDetail.class);
                intent.putExtra(TITLE_MESSAGE, eventslist.get(0)[pos]);
                intent.putExtra(HOST_MESSAGE, eventslist.get(2)[pos]);
                intent.putExtra(DETAILS_MESSAGE, eventslist.get(3)[pos]);
                startActivity(intent);

            }
        });
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.event_list_menu, menu);
        return true;
    }
    /** Called when the user clicks the Profile quick-link */
    public void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Create Events quick-link */
    public void goToCreate() {
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
        finish();
    }

    /** Called when the user clicks the Search Recipe quick-link */
    public void goToRecipe() {
        Intent intent = new Intent(this, RecipeFinder.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //action when corresponding action-bar item is clicked
        switch(item.getItemId()) {

            case R.id.create_events_ql:
                goToCreate();
                return true;

            case R.id.profile_ql:
                goToProfile();
                return true;

            case R.id.search_recipe_ql:
                goToRecipe();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    public void DBcall(String[] events,String[] time,String[]hosts, String[] details) {
//        lv = (ListView) findViewById(R.id.eventList);
//
//        events = helper.selectEventNames();
//        time = helper.selectEventTime();
//        hosts = helper.selectHostName();
//        details = helper.selectEventDetails();
//    }
//    public void createAdapter(List<String>events,List<String>time,List<String>hosts,List<String>details){
//        EventAdapter adapter = new EventAdapter(this, events, time, hosts);
//        lv.setAdapter(adapter);
//    }
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


            String[] events = helper.selectEventNames();
            String[] time = helper.selectEventTime();
            String[] host = helper.selectHostName();
            String[] description = helper.selectEventDetails();

            ArrayList<String[]> eventstuff = new ArrayList<>();

            eventstuff.add(events);
            eventstuff.add(time);
            eventstuff.add(host);
            eventstuff.add(description);


            return eventstuff;



        }

        protected void onPostExecute(ArrayList<String[]> response) {
//            EventAdapter adapter = new EventAdapter(this, time, host,description);
            EventAdapter adapter = new EventAdapter(EventList.this,response.get(0), response.get(1), response.get(2));
            lv.setAdapter(adapter);
            delegate.processFinish(response);


        }
    }

}

interface AsyncResponse{
    ArrayList<String[]> processFinish(ArrayList<String[]> output);
}