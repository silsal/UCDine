package com.example.saorla.ucdfood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MyDBHandler helper = new MyDBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onButtonClick(View v){
        if(v.getId()== R.id.Blogin){

            EditText a = (EditText)findViewById(R.id.TFusername);
            String str = a.getText().toString();
            EditText b = (EditText)findViewById(R.id.TFpassword);
            String pass = b.getText().toString();

            String password = helper.searchPass(str);
            if(pass.equals(password)){
                Intent i = new Intent(MainActivity.this, Display.class);
                i.putExtra("Username",str);
                startActivity(i);
            }else{
                Toast temp = Toast.makeText(MainActivity.this, "Username and password don't match!",Toast.LENGTH_SHORT);
                temp.show();
            }


        }
        if(v.getId()==R.id.Bsignup){
            Intent i = new Intent(MainActivity.this, SignUp.class);
            startActivity(i);
        }
    }
}
