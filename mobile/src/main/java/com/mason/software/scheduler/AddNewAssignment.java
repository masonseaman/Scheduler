package com.mason.software.scheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddNewAssignment extends AppCompatActivity {
DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_assignment);

        Intent intent = getIntent();
        final String courseName=  intent.getStringExtra("courseName");

        final EditText assignmentTitle = (EditText) findViewById(R.id.assignmentTitle);
        final EditText dueDate = (EditText) findViewById(R.id.dueDate);
        final EditText dueTime = (EditText) findViewById(R.id.dueTime);
        final EditText description = (EditText) findViewById(R.id.assignmentDescription);

        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog date = new DatePickerDialog(AddNewAssignment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int day, int month, int year) {
                        dueDate.setText(day+"/"+(month+1)+"/"+year);
                    }
                },0,0,0);
                date.setTitle("Select Date");
                date.show();
            }
        });

        dueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog time = new TimePickerDialog(AddNewAssignment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        dueTime.setText(hour + ":" + minute);
                    }
                }, hour, minute, false);
                time.setTitle("Select Time");
                time.show();
            }
        });

        Button createAssignment = (Button) findViewById(R.id.create);
        createAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDbHelper.addToDatabase(courseName,
                        "",
                        "",
                        "",
                        assignmentTitle.getText().toString(),
                        dueDate.getText().toString() + dueTime.getText().toString(),
                        description.getText().toString());
            }
        });
    }
}
