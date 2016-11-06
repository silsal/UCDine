package com.example.saorla.ucdfood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//imports for JSON parsing
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//imports for API call
import java.net.HttpURLConnection;
import java.net.URL;

//This class is for the RecipeFinder activity. It sends HTTP requests to an external API and returns the results in JSON format
public class RecipeFinder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_finder);

//        final Button getRecipeData = (Button) findViewById();
//        getRecipeData.setOnClickListener(new onClickListener(){
//
//            @Override
//            public void onClick(){
//
//            }
//
//        });
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

    public void sendRecipe(View button){

    }
}
