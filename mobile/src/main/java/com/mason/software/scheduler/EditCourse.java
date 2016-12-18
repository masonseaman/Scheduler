package com.mason.software.scheduler;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

public class EditCourse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Intent intent = getIntent();

        final EditText courseName = (EditText) findViewById(R.id.editName);
        courseName.setText(intent.getStringExtra("courseName"));

        final ToggleButton monday = (ToggleButton) findViewById(R.id.editMonday);
        final ToggleButton tuesday = (ToggleButton) findViewById(R.id.editTuesday);
        final ToggleButton wednesday = (ToggleButton) findViewById(R.id.editWednesday);
        final ToggleButton thursday = (ToggleButton) findViewById(R.id.editThursday);
        final ToggleButton friday = (ToggleButton) findViewById(R.id.editFriday);

        String time = intent.getStringExtra("time");
        String[] split = time.split("-");
        final TextView start = (TextView) findViewById(R.id.editStartTime);
        start.setText(split[0].trim());

        final TextView end = (TextView) findViewById(R.id.editEndTime);
        end.setText(split[1].trim());

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(EditCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        start.setText(hour + ":" + minute);
                    }
                }, hour, minute, false);
                time.setTitle("Select Time");
                time.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(EditCourse.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        end.setText(hour + ":" + minute);
                    }
                }, 0, 0, false);
                time.setTitle("Select Time");
                time.show();
            }
        });

        Button button = (Button) findViewById(R.id.update);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(EditCourse.this);
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

                if(TextUtils.isEmpty(courseName.getText().toString()))
                    Snackbar.make(view, "Add a course name", Snackbar.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(start.getText().toString()))
                    Snackbar.make(view, "Add a start time", Snackbar.LENGTH_SHORT).show();
                else if(TextUtils.isEmpty(end.getText().toString()))
                    Snackbar.make(view, "Add an end time", Snackbar.LENGTH_SHORT).show();
                else if(daysOfWeek.equals(""))
                    Snackbar.make(view, "Add days of week", Snackbar.LENGTH_SHORT).show();
                else
                {
                    //myDbHelper.updateEntry(courseName.getText().toString().toUpperCase(), daysOfWeek, start.getText().toString() + " - " + end.getText().toString(), "", "", "", "");
                    myDbHelper.setCourseTime(courseName.getText().toString(),start.getText().toString()+ " - " + end.getText().toString());
                    Intent intent = new Intent(getBaseContext(),CourseList.class);
                    startActivity(intent);
                }
            }
        });

    }
}
