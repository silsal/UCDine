/*This file contains.... Parts of the code have been adapted from David Coyle's Sunshine Starter and various tutorials
*Code to show/hide keyboard from http://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
*
* */

package com.example.saorla.ucdfood;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.TextView;
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
    public final static String EXTRA_MESSAGE = "com.example.saorla.ucdfood.MESSAGE";
    public String[] stringArray(String string_name){
        String[] strArray = string_name.split(" ");
        return strArray;
    }
    String strName = "name this tune";
    String name = stringArray(strName)[0];
    String user_name = name;

    //initialise variables, views and other components which will be required
    private final String APP_LOG = "Saorla's message!";
    private static final String RECIPE_RESPONSE = "RECIPE_RESPONSE";
    private ListView listview;
    private ArrayAdapter<String> recipeAdapter;
    ProgressBar progressBar;

    EditText dishName;
    //    TextView responseView;
    //set variables for the API and API key
    private static final String API_KEY = "1ab4790ba9350f1af172e0146399d0c9";
    static final String API_URL = "http://food2fork.com/api/search?key=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_finder);
        //set up
        recipeAdapter = new ArrayAdapter<String>(this, R.layout.recipe_item_finder, R.id.recipe_item_finder_textview, new ArrayList<String>());
        listview = (ListView) findViewById(R.id.recipe_list_view);
        listview.setAdapter(recipeAdapter);
        dishName = (EditText) findViewById(R.id.dishName);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        responseView = (TextView) findViewById(R.id.responseView);
        Button recipeButton = (Button) findViewById(R.id.recipeButton);
        //when recipe button is clicked, call AsyncTask to send API request
        Log.i(APP_LOG, "Calling Async");
        recipeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // Check if no view has focus and hide keyboard:
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                new QueryRecipeAPI().execute(dishName.getText().toString());
            }
        });

    }


    //subclass of RecipeFinder which handles HTTPConnection to the API
    //returns a string array of recipe results
    class QueryRecipeAPI extends AsyncTask<String, Void, String[]> {
        private Exception exception;

        @Override
        protected void onPreExecute() {
            //make progress bar visible
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
                //create url and set up httpconnection. Must replace all blanks with %20 for the api
                String temp = API_URL + API_KEY + "&q=" + dish;
                temp = temp.replaceAll(" ", "%20");
                URL url = new URL(temp);
                Log.i(APP_LOG, temp + " is the url");
//                    URL url = new URL(API_URL + API_KEY + "&q=" + dish);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
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

        protected void onPostExecute(String[] response) {
            Log.i(APP_LOG, "response is "+response);
            //if the server has not responded or there was an error parsing the recipes, print a message for the user
            if (response == null) {
                progressBar.setVisibility(View.GONE);
                recipeAdapter.add("An error has occurred; no recipes have been found. Please try again later!\n\n - The UCDine Team");
            }
            else {
                progressBar.setVisibility(View.GONE);
                Log.i(APP_LOG, "This is the response!" + response[1]);
                //if there were already recipes in the textview, clear them
                recipeAdapter.clear();

                //split up each string in the response into strings and URIs
                for (String nextRecipeStr : response) {
                    recipeAdapter.add(nextRecipeStr);
                }
            }

        }
    }

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

    private String[] getRecipesFromJson(String recipeJSONStr)
            throws JSONException {

        final String LIST_NUM = "count";
        final String RECIPES = "recipes";
        final String TITLE = "title";
        final String SOURCE = "source_url";

        //create new JSON object
        JSONObject recipeJSON = new JSONObject(recipeJSONStr);
        JSONArray recipeArray = recipeJSON.getJSONArray(RECIPES);
        Log.i(APP_LOG, recipeJSON.toString(4));


        //find the number of recipes which were returned
        int recipeCount = recipeJSON.getInt(LIST_NUM);
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

            recipeResults[i] = title + "\n" + source;
            //this is working!
            Log.i(APP_LOG, recipeResults[i]);
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
        //release something we don't need when paused
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.recipe_finder_menu, menu);
        return true;
    }
    /** Called when the user clicks the Search Events quick-link */
    public void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
        finish();
    }

    /** Called when the user clicks the Create Events quick-link */
    public void goToCreate() {
        Intent intent = new Intent(this, CreateEvent.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
        startActivity(intent);
        finish();
    }

    /** Called when the user clicks the Search Recipe quick-link */
    public void goToEvents() {
        Intent intent = new Intent(this, EventList.class);
        intent.putExtra(EXTRA_MESSAGE, user_name);
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

            case R.id.search_events_ql:
                goToEvents();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}




