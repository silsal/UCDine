package com.example.saorla.ucdfood;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by user on 09/11/2016.
 */

public class Display extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        String username = getIntent().getStringExtra("Username");

        TextView tv = (TextView)findViewById(R.id.TVusername);
        tv.setText(username);
    }



}
