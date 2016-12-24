package com.mason.software.scheduler;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CourseList extends AppCompatActivity {
    DatabaseOpenHelper myDbHelper = new DatabaseOpenHelper(this);
    String[] drawerItems = new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Course List");

        DrawerLayout dl = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawer = (ListView) findViewById(R.id.left_drawer);
        drawerItems[0] = "Sign in with Google";
        dl.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawer.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, drawerItems));
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
        ActionBarDrawerToggle mDrawerToggle;
        mDrawerToggle = new ActionBarDrawerToggle(
                CourseList.this,                  /* host Activity */
                dl,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_closed /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle("Title");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Drawer Title");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        dl.addDrawerListener(mDrawerToggle);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNewCourse = new Intent(getBaseContext(),AddNewCourse.class);
                startActivity(addNewCourse);
            }
        });

        ArrayList al = myDbHelper.getCourses();
        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,al);

        final ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(arrayAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getBaseContext(), AssignmentsList.class);
                intent.putExtra("courseName", list.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                new AlertDialog.Builder(CourseList.this)
                        .setTitle("What would you like to do with " + list.getItemAtPosition(position).toString() + "?")
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getBaseContext(),EditCourse.class);
                                intent.putExtra("daysOfWeek", myDbHelper.getDaysofweek(list.getItemAtPosition(position).toString()));
                                intent.putExtra("courseName", list.getItemAtPosition(position).toString());
                                intent.putExtra("time", myDbHelper.getCourseTime(list.getItemAtPosition(position).toString()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new AlertDialog.Builder(CourseList.this)
                                        .setTitle("Are you sure you wish to delete all information regarding " + list.getItemAtPosition(position).toString()+"?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                myDbHelper.removeAllDataFromCourse(list.getItemAtPosition(position).toString());
                                                Toast.makeText(getBaseContext(),list.getItemAtPosition(position).toString() + " has been deleted", Toast.LENGTH_SHORT).show();
                                                recreate();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        })
                        .create()
                        .show();
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();

        ArrayList al = myDbHelper.getCourses();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,al);

        final ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed(){
    }
}
