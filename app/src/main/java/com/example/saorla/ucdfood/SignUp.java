package com.example.saorla.ucdfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by user on 09/11/2016.
 */

public class SignUp extends Activity {

    MyDBHandler helper = new MyDBHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }


    public void onSignUpClick(View v){
        if (v.getId()==R.id.Bsignupbutton){
            EditText fname = (EditText)findViewById(R.id.TFname);
            EditText sname = (EditText)findViewById(R.id.TFsurname);
            EditText uname = (EditText)findViewById(R.id.TFusername);
            EditText email = (EditText)findViewById(R.id.TFemail);
            EditText pass1 = (EditText)findViewById(R.id.TFpass1);
            EditText pass2 = (EditText)findViewById(R.id.TFpass2);

            String fnamestr = fname.getText().toString();
            String snamestr = sname.getText().toString();
            String unamestr = uname.getText().toString();
            String emailstr = email.getText().toString();
            String pass1str = pass1.getText().toString();
            String pass2str = pass2.getText().toString();

            if(!pass1str.equals(pass2str)){
                //popup message
                Toast pass = Toast.makeText(SignUp.this, "Passwords don't match!",Toast.LENGTH_SHORT);
                pass.show();
            }
            else{
                //insert the details into db
                Users s = new Users();
                s.setFname(fnamestr);
                s.setSname(snamestr);
                s.setUname(unamestr);
                s.setEmail(emailstr);
                s.setPass(pass1str);

                helper.insertUsers(s);

            }


        }
        //bring back to login
        if(v.getId()==R.id.Bsignupbutton){
            Intent i = new Intent(SignUp.this, MainActivity.class);
            startActivity(i);
        }

    }






}
