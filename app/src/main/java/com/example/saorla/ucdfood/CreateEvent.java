package com.example.saorla.ucdfood;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;


/**
 * This activity will display a form that allows the user to create an event to his/her local calendar and to the local database.
 * When the user hits submit 3 points are going to be added to the his/her points. If the form is not completed a Toast prevents the
 *
 */
public class CreateEvent extends AppCompatActivity implements View.OnClickListener {


    private final String EV_LOG = "Silvia's message!";
    private EditText Textdate,
            Texthour,
            Textevent,
            Textlocation,
            TextNoPeople,
            Textdescription;
    private String date,
            hour,
            event,
            location,
            description,
            noPeople,
            picture;

    MyDBHandler helperevent = new MyDBHandler(this);
    private DatePickerDialog datePicker;
    private SimpleDateFormat dateFormatter;

    private TimePickerDialog timePicker;
    private ImageButton addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);

        findViewsById();
        setDateField();
        setTimeField();

        final String[] option = new String[]{"Take Photo", "Gallery Upload"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, option);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Log.e("Selected Item", String.valueOf(which));
                if (which == 0) {
                    // DO Something here for first option (like call a function / Show a Toast etc)
                    takePicture(null);
                }

                if (which == 1) {
                    pickGallery(null);
                }


            }
        });
        final AlertDialog dialog = builder.create();
        addImage = (ImageButton) findViewById(R.id.camera);

        addImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //Take a pic and pass result to onActivityResult
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }
    private int PICK_IMAGE_REQUEST = 2;

    public void pickGallery(View view) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //get the photo and compress it into a byte array
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 10, stream);
            byte[] byteArray = stream.toByteArray();
            //transfor the byte array into a string for facilitating the storage into the database
            picture = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //do the same if the picture is taken from a gallery
            Uri uri = data.getData();
            try {
                Bitmap gallery = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                gallery.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                byte[] byteArray = stream.toByteArray();
                picture = Base64.encodeToString(byteArray, Base64.DEFAULT);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void findViewsById() {
        //get all the edit text from the form and save them into a edit text
        Textdate = (EditText) findViewById(R.id.date);
        Textdate.requestFocus();
        Textdate.setTextIsSelectable(true);
        Texthour = (EditText) findViewById(R.id.hour);
        Texthour.requestFocus();
        Texthour.setTextIsSelectable(true);
        Textevent = (EditText) findViewById(R.id.eventTitle);
        Textlocation = (EditText) findViewById(R.id.location);
        TextNoPeople = (EditText) findViewById(R.id.people);
        Textdescription = (EditText) findViewById(R.id.description);
    }

    // Store form values into corresponding variables
    private void getFormValues() {
        Log.i(EV_LOG, "getting form values");
        date = Textdate.getText().toString();
        hour = Texthour.getText().toString();
        event = Textevent.getText().toString();
        location = Textlocation.getText().toString();
        noPeople = TextNoPeople.getText().toString();
        description = Textdescription.getText().toString();
    }

    //clear fields of the form
    public void clearFields() {
        Textdate.getText().clear();
        Texthour.getText().clear();
        Textevent.getText().clear();
        Textlocation.getText().clear();
        TextNoPeople.getText().clear();
        Textdescription.getText().clear();
    }

    @Override
    public void onClick(View view) {
        //activate the on click listener for the date picker of the time picker
        switch (view.getId()) {
            case R.id.date:
                datePicker.show();
                break;
            case R.id.hour:
                timePicker.show();
                break;

//
        }
    }

    public void setDateField() {
        //allow to call the calendar content provider for allowing the user to select the date of the event
        Textdate.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Textdate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    public void setTimeField() {
        //allow to call the calendar content provider for allowing the user to select the time
        Texthour.setOnClickListener(this);
        // Process to get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        timePicker = new TimePickerDialog(this, new OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                // Display Selected time in textbox
                Texthour.setText(hourOfDay + ":" + minutes);
            }
        }, mHour, mMinute, false);
    }

    public void onAddEventClicked(View view) {
        //if the form is complet send the event to the calendar content provider in order to allow the user to save the  event into
        //its local calendar
        Log.i(EV_LOG, "I am here!");
        try {
            getFormValues();
            if (validateForm()) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date);
                intent.putExtra(CalendarContract.Events.DTSTART, hour);
                intent.putExtra(CalendarContract.Events.TITLE, event);
                intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
                intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
                startActivity(intent);
            }else{
                Toast.makeText(getBaseContext(), "Fill all the field in the form, please!", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e) {
            Log.i(EV_LOG, e.getMessage(), e);
            Toast.makeText(getBaseContext(), "Fill all the field in the form, please!", Toast.LENGTH_LONG).show();
        }
    }


    public String getIdfromSharedPreference() {
        //function that retrive the id of the user
        SharedPreferences prefs = getSharedPreferences("User_Id", 0);
        String extractedText = prefs.getString("shared_ref_id", "No ID found");

        return extractedText;
    }

    public void sendEvent(View v) {
        //if the form is filled, send the event to the database, increase the user point by 3 and clear the field of the form after submission
        try {
            getFormValues();
            if (validateForm()) {
                String user = getIdfromSharedPreference();
                int id = Integer.parseInt(user);

                Events e = new Events();

                e.set_hid(id);
                e.setInvite_num(Integer.parseInt(noPeople));
                e.setAvailable_num(Integer.parseInt(noPeople));
                e.setEvent_name(event);
                e.setAddress(location);
                e.setDescription(description);
                e.setTime(hour);
                e.setDate(date);
                e.setEvent_pic(picture);
                helperevent.addEvent(e);
                helperevent.upPoints(id);
                AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                myAlert.setTitle("Your event has been created!")
                        .setIcon(R.drawable.star)
                        .setMessage("You earned 3 points! :)")
                        .setPositiveButton("OK!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                myAlert.show();
                clearFields();
            }
        } catch (Exception e) {
            //if not all the field of the form are complete, create a Toast for warning the user to fill in all the form
            Log.i(EV_LOG, e.getMessage(), e);
            Toast.makeText(getBaseContext(), "Fill all the field in the form, please!", Toast.LENGTH_LONG).show();
        }
    }





    //Check that all the fields  of the form have been filled
    private Boolean validateForm() {
        if (event.equals(getString(R.string.event_title))) {
            return false;
        }
        if (location.trim().equals(getString(R.string.location))) {
            return false;
        }
        if (date.equals(getString(R.string.date))) {
            return false;
        }
        if (noPeople.equals(getString(R.string.people))) {
            return false;
        }
        if (hour.equals(getString(R.string.hour))) {
            return false;
        }
        if (description.equals(getString(R.string.description))) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.create_event_menu, menu);
        return true;
    }
    /** Called when the user clicks the Profile quick-link */
    public void goToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Search Recipe quick-link */
    public void goToEvents() {
        Intent intent = new Intent(this, EventList.class);
        startActivity(intent);
        finish();
    }

    /** Called when the user clicks the Search Recipe quick-link */
    public void goToRecipe() {
        Intent intent = new Intent(this, RecipeFinder.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //action when corresponding action-bar item is clicked
        switch(item.getItemId()) {


            //"Back" button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;


            case R.id.search_events_ql:
                goToEvents();
                return true;

            case R.id.profile_ql:
                goToProfile();
                return true;

            case R.id.search_recipe_ql:
                goToRecipe();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }




