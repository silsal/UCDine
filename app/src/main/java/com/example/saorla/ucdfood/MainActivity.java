package com.example.saorla.ucdfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.Toast;

/**
    Opening activity java file
    Includes:
    Sign in method
    Link to sign up activity
    Shared preference method to store user id
 */
public class MainActivity extends AppCompatActivity {
    //Initialise database helper
    MyDBHandler helper = new MyDBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_profile, R.anim.slide_out_profile);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
        Method to animate logo on click &
        Produce 'Baaa' sound
     */
    public void baaflash(View v) {
        //Set up animation
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(5);
        animation.setRepeatMode(Animation.REVERSE);

        //Set up media player
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.sheep);

        if(v.getId()==R.id.WelcomeSheep){
            mp.start();
            v.startAnimation(animation);
            Toast.makeText(getBaseContext(), "Baaaa, welcome!", Toast.LENGTH_LONG).show();
        }

    }


    /**
     Method to sign in
     Check if username and password match and also exist in database
     If not flags users using toast
     Stores user id so other activities can access specific user's information
     */
    public void onButtonClick(View v){
        if(v.getId()== R.id.Blogin){

            EditText a = (EditText)findViewById(R.id.TFusername);
            String str = a.getText().toString().trim();
            EditText b = (EditText)findViewById(R.id.TFpassword);
            String pass = b.getText().toString();

            String password = helper.searchPass(str);
            String id = helper.searchId(str);

            //Store Value into shared preference
            storeIdToSharedPreference(id);

            //Check if credentials match bring to menu page
            if(pass.equals(password)){
                Intent i = new Intent(MainActivity.this, Display.class);
                startActivity(i);
                finish();
            //Else alert user with toast
            }else{
                Toast temp = Toast.makeText(MainActivity.this, "Username and password don't match!",Toast.LENGTH_SHORT);
                temp.show();
            }

        }
        if(v.getId()==R.id.Bsignup){
            Intent i = new Intent(MainActivity.this, SignUp.class);
            startActivity(i);
            finish();
        }

    }

    /**
     Shared preference that stores user id
     */
    public void storeIdToSharedPreference(String id){
        SharedPreferences prefs = getSharedPreferences("User_Id",0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("shared_ref_id",id);
        editor.commit();
    }


}
