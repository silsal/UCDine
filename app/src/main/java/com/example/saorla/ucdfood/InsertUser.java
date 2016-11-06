package com.example.saorla.ucdfood;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class InsertUser extends AppCompatActivity {
//    public final static String EXTRA_MESSAGE = "com.example.saorla.ucdfood.MESSAGE";
    private static final String AGAIN = "PLEASE WORK!";
    EditText saorlasInput;
    EditText first;
    EditText second;
    EditText email;
    TextView saorlasText;
    MyDBHandler dbHandler;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user);
        saorlasInput = (EditText) findViewById(R.id.saorlasInput);
        first = (EditText) findViewById(R.id.firstname);
        second = (EditText) findViewById(R.id.surname);
        email = (EditText) findViewById(R.id.email);
        saorlasText = (TextView) findViewById(R.id.saorlasText);
        dbHandler = new MyDBHandler(this, null, null, 2);
        printDB();
    }

    public void printDB(){
        String dbString = dbHandler.databaseToString();
        saorlasText.setText(dbString);
        saorlasInput.setText("");
    }

    public void addButtonClicked(View v){
        Users users = new Users(saorlasInput.getText().toString(), first.getText().toString(), second.getText().toString(), email.getText().toString());
        dbHandler.addUser(users);
        Log.i(AGAIN, "GO ON!");
        printDB();
    }

    public void deleteButtonClicked(View view){
        // dbHandler delete needs string to find in the db
        String inputText = first.getText().toString();
        dbHandler.deleteUser(inputText);
        printDB();
    }


}
