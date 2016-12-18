package com.mason.software.scheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    final private static String NAME = "schedule";
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
    public static final String DESCRIPTION = "description";


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

    public String getDaysofweek(String courseTitle){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select daysOfWeek from schedule where courseTitle='" + courseTitle + "'", null);
        res.moveToFirst();
        return res.getString(res.getColumnIndex(DAYSOFWEEK));
    }

    public String getCourseTime(String courseTitle){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select time from schedule where courseTitle = '" + courseTitle + "'",null);
        res.moveToFirst();
        return res.getString(res.getColumnIndex(TIME));
    }

    public String getCourselocation(String courseTitle){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select courseLocation from schedule where courseTitle = '" + courseTitle + "'",null);
        res.moveToFirst();
        return res.getString(res.getColumnIndex(COURSELOCATION));
    }

    public String getDueDate(String assignmentTitle){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select dueDate from schedule where assignmentTitle = '" + assignmentTitle + "'", null);
        res.moveToFirst();
        return res.getString(res.getColumnIndex(DUEDUATE));
    }

    public String getDescription(String assignmentTitle){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select description from schedule where assignmentTitle = '" + assignmentTitle + "'", null);
        res.moveToFirst();
        return res.getString(res.getColumnIndex(DESCRIPTION));
    }

    public void setDescription(String assignmentTitle, String newDescription){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("description", newDescription);
        db.update(NAME, cv, "assignmentTitle ='" + assignmentTitle + "'",null);
    }

    public void setCourseTime(String courseTitle, String newCourseTime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("time", newCourseTime);
        db.update(NAME, cv, "time ='" + newCourseTime + "'",null);
    }


    public boolean updateEntry(String courseTitle, String daysOfWeek, String time, String courseLocation, String assignmentTitle, String dueDate, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues(4);
        contentValues.put("courseTitle", courseTitle);
        contentValues.put("daysOfWeek", daysOfWeek);
        contentValues.put("time", time);
        contentValues.put("courseLocation", courseLocation);
        contentValues.put("assignmentTitle", assignmentTitle);
        contentValues.put("dueDate", dueDate);
        contentValues.put("description", description);

        db.update("schedule", contentValues, "courseTitle='"+courseTitle+"'", null);
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

        while(res.isAfterLast() == false) {
            //if(res.getString(res.getColumnIndex(ASSIGNMENTTITLE)).equals(""))
            al.add(res.getString(res.getColumnIndex(ASSIGNMENTTITLE)));
            res.moveToNext();
        }
        for(int i=0;i<al.size();i++) {
            System.out.println(al.get(i));
            if(al.get(i).equals(""))
                al.remove(i);
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

    public void removeAssignment(String assignmentName, String courseName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("delete from schedule where assignmentTitle='" + assignmentName + "' and courseTitle='" + courseName + "'", null);
        while(res.isAfterLast()==false)
            res.moveToNext();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

    void deleteDatabase ( ) {
        context.deleteDatabase(NAME);
    }
}
