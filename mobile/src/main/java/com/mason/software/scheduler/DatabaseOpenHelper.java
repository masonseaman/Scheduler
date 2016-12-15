package com.mason.software.scheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    final private static String NAME = "artist_db";
    final private static Integer VERSION = 1;
    final private Context context;

    public static final String DATABASE_NAME = "schedule.db";
    public static final String TABLE_NAME = "schedule";
    public static final String COURSETITLE = "courseTitle";
    public static final String DAYSOFWEEK = "daysOfWeek";
    public static final String TIME = "time";
    public static final String COURSELOCATION = "courseLocation";
    public static final String ASSIGNMENTTITLE = "assignmentTitle";
    public static final String DUEDUATE = "dueDate";
    public static final String DESCRIPTION = "assignmentDescription";


    public DatabaseOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table schedule " +
                        "(courseTitle text, daysOfWeek text, time text, courseLocation text, assignmentTitle text, dueDate text, description text)"
        );
    }

    public boolean addToDatabase(String courseTitle, String daysOfWeek, String time, String courseLocation, String assignmentTitle, String dueDate, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues(4);
        contentValues.put("courseTitle", courseTitle);
        contentValues.put("daysOfWeek", daysOfWeek);
        contentValues.put("time", time);
        contentValues.put("courseLocation", courseLocation);
        contentValues.put("assignmentTitle", assignmentTitle);
        contentValues.put("dueDate", dueDate);
        contentValues.put("description", description);

        db.insert("schedule", null, contentValues);
        return true;
    }

    public ArrayList<String> getCourses(){
        ArrayList<String> al = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select distinct courseTitle from schedule", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            al.add(res.getString(res.getColumnIndex(COURSETITLE)));
            res.moveToNext();
        }
        return al;
    }

    public ArrayList<String> getAssignments(String courseName){
        ArrayList<String> al = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select assignmentTitle from schedule where courseTitle='" + courseName + "'",null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            if(res.getString(res.getColumnIndex(ASSIGNMENTTITLE)).equals(""))
                al.add(res.getString(res.getColumnIndex(ASSIGNMENTTITLE)));
            res.moveToNext();
        }
        return al;
    }

    public void removeAllDataFromCourse(String courseName){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor res = db.rawQuery("delete from schedule where courseTitle='" + courseName + "'", null);
        while(res.isAfterLast()==false){
            res.moveToNext();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    void deleteDatabase ( ) {
        context.deleteDatabase(NAME);
    }
}
