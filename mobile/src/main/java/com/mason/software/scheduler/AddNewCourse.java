package com.mason.software.scheduler;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import java.util.Calendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;

public class AddNewCourse extends AppCompatActivity implements OnConnectionFailedListener{
    TextView location;
    String loc="";
    private GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        mGoogleApiClient=new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();

        location = (TextView) findViewById(R.id.location);

        final DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(this);

        final EditText courseName = (EditText) findViewById(R.id.courseName);

        final ToggleButton monday = (ToggleButton) findViewById(R.id.monday);
        final ToggleButton tuesday = (ToggleButton) findViewById(R.id.tuesday);
        final ToggleButton wednesday = (ToggleButton) findViewById(R.id.wednesday);
        final ToggleButton thursday = (ToggleButton) findViewById(R.id.thursday);
        final ToggleButton friday = (ToggleButton) findViewById(R.id.friday);

        final TextView startTime = (TextView) findViewById(R.id.startTime);
        final TextView endTime = (TextView) findViewById(R.id.endTime);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(AddNewCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        startTime.setText(hour + ":" + minute);
                    }
                }, 0, 0, false);
                time.setTitle("Select Time");
                time.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(AddNewCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        endTime.setText(hour + ":" + minute);
                    }
                }, 0, 0, false);
                time.setTitle("Select Time");
                time.show();
            }
        });

        Button setLocation = (Button) findViewById(R.id.set_location);
        setLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int PLACE_PICKER_REQUEST = 1;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(AddNewCourse.this),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        Button addCourse = (Button) findViewById(R.id.addCourse);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String daysOfWeek = "";
                if (monday.isChecked())
                    daysOfWeek += " monday";
                if (tuesday.isChecked())
                    daysOfWeek += " tuesday";
                if (wednesday.isChecked())
                    daysOfWeek += " wednesday";
                if (thursday.isChecked())
                    daysOfWeek += " thursday";
                if (friday.isChecked())
                    daysOfWeek += " friday";

                if (myDbHelper.getCourses().contains(courseName.getText().toString().toUpperCase()))
                    Snackbar.make(view, "Course Already Exists", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                else if(TextUtils.isEmpty(courseName.getText().toString()))
                    Snackbar.make(view, "Add a course name", Snackbar.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(startTime.getText().toString()))
                    Snackbar.make(view, "Add a start time", Snackbar.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(endTime.getText().toString()))
                    Snackbar.make(view, "Add an end time", Snackbar.LENGTH_SHORT).show();
                else if(daysOfWeek.equals(""))
                    Snackbar.make(view, "Add days of week", Snackbar.LENGTH_SHORT).show();
                else
                {
                    myDbHelper.addToDatabase(courseName.getText().toString().toUpperCase(), daysOfWeek, startTime.getText().toString() + " - " + endTime.getText().toString(),loc,"" , "", "");
                    Intent intent = new Intent(getBaseContext(),CourseList.class);
                    startActivity(intent);
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1){
            if(resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getBaseContext(),data);
                loc = place.getLatLng().toString();
                loc +="\n" + place.getName();
                location.setText(loc);
            }

        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getBaseContext(),CourseList.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
