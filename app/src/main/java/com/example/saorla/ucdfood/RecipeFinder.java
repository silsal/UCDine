
package com.example.saorla.ucdfood;

/*This file contains the code to create the RecipeFinder activity in UCDine, which allows users to search for recipes through
the food2fork web API. It makes use of the AsyncTask class to allow the API call to be performed on a background thread, and
implements error handling to catch any exceptions thrown due to incorrect user input or an external server error.

Parts of the code have been adapted from David Coyle's Sunshine Starter, in particular those for parsing JSON returned from the API
Code to show/hide keyboard adapted from http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
* */

//imports for components
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

//imports for JSON parsing
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//imports for API call
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


//This class is for the RecipeFinder activity. It sends HTTP requests to an external API and returns the results in JSON format
public class RecipeFinder extends AppCompatActivity {
    //initialise variables, views and other components which will be required later
    private final String APP_LOG = "Test message!";
    private static final String RECIPE_RESPONSE = "RECIPE_RESPONSE";
    private ListView listview;
    private ArrayAdapter<String> recipeAdapter;
    ProgressBar progressBar;
    EditText dishName;

    //set variables for the food2fork API link and our API key
    private static final String API_KEY = "1ab4790ba9350f1af172e0146399d0c9";
    static final String API_URL = "http://food2fork.com/api/search?key=";

    //method to create the activity onload
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_finder);
        //create an array adapter to store the API response in later
        recipeAdapter = new ArrayAdapter<String>(this, R.layout.recipe_item_finder, R.id.recipe_item_finder_textview, new ArrayList<String>());
        listview = (ListView) findViewById(R.id.recipe_list_view);
        //set the adapter to the listview
        listview.setAdapter(recipeAdapter);
        dishName = (EditText) findViewById(R.id.dishName);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button recipeButton = (Button) findViewById(R.id.recipeButton);
        //when recipe button is clicked, call AsyncTask QueryRecipeAPI to send API request
        Log.i(APP_LOG, "Calling Async");
        recipeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // Check if view has focus and hide keyboard if so
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                //send the user input to the AsyncTask
                new QueryRecipeAPI().execute(dishName.getText().toString());
            }
        });

    }

    //method to create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.recipe_finder_menu, menu);
        return true;
    }

    //method which instructs operations to be performed on-click of Menu Options.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //action when corresponding action-bar item is clicked
        switch(item.getItemId()) {

            //"Back" button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.create_events_ql:
                goToCreate();
                return true;

            case R.id.profile_ql:
                goToProfile();
                return true;

            case R.id.search_events_ql:
                goToEvents();
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

    /** Called when the user clicks the Create Events quick-link */
    public void goToEvents() {
        Intent intent = new Intent(this, EventList.class);
        startActivity(intent);
    }


    //Subclass of RecipeFinder which handles HTTPConnection to the API. Returns a string array of recipe results
    class QueryRecipeAPI extends AsyncTask<String, Void, String[]> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            //make progress bar visible when asynctask is called
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            //return null if no dish has been entered
            if (params.length == 0) {
                return null;
            }

            //get dish for the url
            String dish = params[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String recipeJSONStr = null;


            try {
                //create url and set up httpconnection. Must replace all blanks with %20 for the API
                String temp = API_URL + API_KEY + "&q=" + dish;
                temp = temp.replaceAll(" ", "%20");
                URL url = new URL(temp);
                //set up connection and open it
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                //if the server returns 200
                if (responseCode == 200) {
                    Log.i(APP_LOG, "200 okay!");
                    //store input stream received
                    InputStream inputStream = urlConnection.getInputStream();
                    //read this input stream into a string to be passed for parsing
                    recipeJSONStr = readStream(inputStream);
                }
                else{
                    Log.i(APP_LOG, responseCode+"");
                    return null;
                }
            }
            //catch any exceptions thrown
            catch (IOException e) {
                Log.i(APP_LOG, e.getMessage(), e);
                return null;
            }
            finally {
                //close the connection when finished
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(APP_LOG, "An error has occurred; closing the buffer stream!", e);
                    }
                }
            }

            try {
                //return the recipes as strings
                return getRecipesFromJson(recipeJSONStr);
            }
            catch (JSONException e) {
                Log.e(APP_LOG, e.getMessage(), e);
                e.printStackTrace();
            }

            // Return null if there is an error parsing the recipes
            return null;
        }

        //method called when the JSON string is returned from the background thread
        protected void onPostExecute(String[] response) {
            Log.i(APP_LOG, "response is "+response);
            progressBar.setVisibility(View.GONE);
            recipeAdapter.clear();
            //if the server has not responded or there was an error parsing the recipes, print a message for the user
            if (response == null) {
                recipeAdapter.add("An error has occurred; no recipes have been found. Please try again later!\n\n - The UCDine Team");
            }
            //if the app user has sent a bad request to the api
            else if(response[0] == "400"){
                recipeAdapter.add("Invalid search parameters - please try again!");
            }
            else{
                //if there were already recipes in the textview, clear them
                recipeAdapter.clear();

                //split up each string in the response into strings and URIs and pass them back to the arrayadapter
                for (String nextRecipeStr : response) {
                    recipeAdapter.add(nextRecipeStr);
                }
            }

        }
    }

    //method which reads in the data input stream and handles exceptions
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer("");
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            Log.e(APP_LOG, "IOException");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data.toString();
    }

    //method which parses the input as a JSON object and extracts each recipe title and link
    private String[] getRecipesFromJson(String recipeJSONStr)
            throws JSONException {

        final String LIST_NUM = "count";
        final String RECIPES = "recipes";
        final String TITLE = "title";
        final String SOURCE = "source_url";

        //create new JSON object
        JSONObject recipeJSON = new JSONObject(recipeJSONStr);
        JSONArray recipeArray = recipeJSON.getJSONArray(RECIPES);

        //find the number of recipes which were returned
        int recipeCount = recipeJSON.getInt(LIST_NUM);
        if (recipeCount == 0){
            String [] bad_request = new String[]{"400"};
            return bad_request;
        }
        //create a string array of this size
        String[] recipeResults = new String[recipeCount];
        //go through array of results
        for(int i = 0; i < recipeArray.length(); i++) {
            //declare variables to store results in
            String title, source;

            // Get the JSON object for each recipe returned
            JSONObject currentRecipe = recipeArray.getJSONObject(i);
            //get the contents of each string we need
            title = currentRecipe.getString(TITLE);
            source = currentRecipe.getString(SOURCE);
            //add each recipe string to the array
            recipeResults[i] = title + "\n" + source;
        }
        return recipeResults;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }



}
