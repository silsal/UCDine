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
import android.support.v4.app.NavUtils;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.saorla.ucdfood.R.layout.activity_eventlist;


public class EventList extends AppCompatActivity{
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
    public final static String POINTS_MESSAGE = "points";
    public final static String EID_MESSAGE = "eid";
    String[] host;
    String[] time;
    String[] title;
    String[] description;
    String[] is_attending;
    String[] can_attend;
    String[] points;
    String[] address;
    String[] date;
    String[] eid;

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

        getEvents.execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
//                Toast.makeText(getApplicationContext(), eventDetailList[pos], Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), EventDetail.class);
                intent.putExtra(TITLE_MESSAGE, title[pos]);
                intent.putExtra(HOST_MESSAGE,host[pos]);
                intent.putExtra(DETAILS_MESSAGE, description[pos]);
                intent.putExtra(DATE_MESSAGE, date[pos]);
                intent.putExtra(TIME_MESSAGE, time[pos]);
                intent.putExtra(ADDRESS_MESSAGE, address[pos]);
                intent.putExtra(IS_ATTENDING_MESSAGE, is_attending[pos]);
                intent.putExtra(CAN_ATTEND_MESSAGE, can_attend[pos]);
                intent.putExtra(POINTS_MESSAGE, points[pos]);
                intent.putExtra(EID_MESSAGE, eid[pos]);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.event_list_menu, menu);
        return true;
    }

    //Function (Menu Options Click) that instructs operations to be performed on-click of Menu Options.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //action when corresponding action-bar item is clicked
        switch(item.getItemId()) {

            //"Back" button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;


            //Drop-down: Create Event
            case R.id.create_events_ql:
                goToCreate();
                return true;

            //Drop-down: Search Recipe
            case R.id.search_recipe_ql:
                goToRecipe();
                return true;

            case R.id.profile_ql:
                goToProfile();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Called when the user clicks the Profile quick-link */
    public void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
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




    class GetEvents extends AsyncTask<Void, Void, ArrayList<String[]>> {
        private Exception exception;



        @Override
        protected void onPreExecute() {
            //make progress bar visible
//            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<String[]> doInBackground(Void...params) {
//            List<List> eventinfo = helper.eventinfo();
            ArrayList<String[]> eventsString = helper.eventinfostr();


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
            eid = new String[response_size];
            for (int i=0; i<response_size; i++) {
                title[i] = (response.get(0)[i]);
                time[i] = (response.get(2)[i]);
                host[i] = (response.get(1)[i]);
                description[i] = (response.get(4)[i]);
                date[i] = (response.get(3)[i]);
                address[i] = (response.get(5)[i]);
                can_attend[i] = (response.get(7)[i]);
                is_attending[i] = (response.get(6)[i]);
                eid[i] = (response.get(8)[i]);
                points[i] = (response.get(9)[i]);
                eventAdapter.add(title[i]);
            }

//            Log.i(EL_LOG, "EVENTADAPTER IS "+eventAdapter);
//            Log.i(EL_LOG, "EID"+response.get(8)[0]);
//            Log.i(EL_LOG, "points"+response.get(9));
////            delegate.processFinish(response);
        }
    }

}

