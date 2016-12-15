package com.mason.software.scheduler;

import android.app.TimePickerDialog;
import java.util.Calendar;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class AddNewCourse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        final DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(this);

        final EditText courseName = (EditText) findViewById(R.id.courseName);

        final ToggleButton monday = (ToggleButton) findViewById(R.id.monday);
        final ToggleButton tuesday = (ToggleButton) findViewById(R.id.tuesday);
        final ToggleButton wednesday = (ToggleButton) findViewById(R.id.wednesday);
        final ToggleButton thursday = (ToggleButton) findViewById(R.id.thursday);
        final ToggleButton friday = (ToggleButton) findViewById(R.id.friday);

        final EditText startTime = (EditText) findViewById(R.id.startTime);
        final EditText endTime = (EditText) findViewById(R.id.endTime);
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
                }, hour, minute, false);
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
                }, hour, minute, false);
                time.setTitle("Select Time");
                time.show();
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

                if (myDbHelper.getCourses().contains(courseName.getText().toString().toUpperCase())) {
                    Snackbar.make(view, "Course Already Exists", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    myDbHelper.addToDatabase(courseName.getText().toString().toUpperCase(), daysOfWeek, startTime.getText().toString() + " - " + endTime.getText().toString(), "", "", "", "");
                    Intent intent = new Intent(getBaseContext(),CourseList.class);
                    startActivity(intent);
                }
            }

        });
    }
}
