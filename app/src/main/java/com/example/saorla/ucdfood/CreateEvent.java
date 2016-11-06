package com.example.saorla.ucdfood;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Cometa on 06/11/2016.
 */
public class CreateEvent extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    /**
     * Called when the user clicks the Send button
     */

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
//        else {
//            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
//                    .show();
//        }
    }
}
