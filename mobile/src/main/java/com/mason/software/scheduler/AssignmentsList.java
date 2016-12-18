package com.mason.software.scheduler;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AssignmentsList extends AppCompatActivity {
    DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(this);
    String courseName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        courseName = intent.getStringExtra("courseName");
        ArrayList al = myDbHelper.getAssignments(courseName);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,al);
        final ListView list = (ListView) findViewById(R.id.assignmentsList);
        list.setAdapter(arrayAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),AddNewAssignment.class);
                intent.putExtra("courseName", courseName);
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(AssignmentsList.this)
                        .setTitle("What would you like to do with " + list.getItemAtPosition(position).toString() + "?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myDbHelper.removeAssignment(list.getItemAtPosition(position).toString(),courseName);
                                Toast.makeText(getBaseContext(),list.getItemAtPosition(position).toString() + " has been deleted", Toast.LENGTH_SHORT).show();
                                recreate();
                            }
                        })
                        .setNeutralButton("Mark as finished", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //list.getChildAt(position).setBackgroundColor(Color.GREEN);
                                myDbHelper.setDescription(list.getItemAtPosition(position).toString(),"COMPLETE");
                                dialogInterface.cancel();
                            }
                        })
                        .show();
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                new AlertDialog.Builder(AssignmentsList.this)
                        .setTitle(list.getItemAtPosition(position).toString())
                        .setMessage("Due Date: " + myDbHelper.getDueDate(list.getItemAtPosition(position).toString()) +
                            "\nDescription: " + myDbHelper.getDescription(list.getItemAtPosition(position).toString()))
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assignments_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.courseInfoMenuOption) {
            new AlertDialog.Builder(AssignmentsList.this)
                    .setTitle(courseName)
                    .setMessage("Days of Week = " + myDbHelper.getDaysofweek(courseName)
                        +"\nTime = " + myDbHelper.getCourseTime(courseName)
                        +"\nLocation = " + myDbHelper.getCourselocation(courseName))
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .create()
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        ArrayList al = myDbHelper.getAssignments(intent.getStringExtra("courseName"));
        ArrayAdapter arrayAdapter = new ArrayAdapter(AssignmentsList.this, android.R.layout.simple_list_item_1,al);

        final ListView list = (ListView) findViewById(R.id.assignmentsList);
        list.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getBaseContext(),CourseList.class);
        startActivity(intent);
    }

}
