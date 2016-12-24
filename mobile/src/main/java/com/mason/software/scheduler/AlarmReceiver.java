package com.mason.software.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String time = intent.getStringExtra("alarmTime");
        String days = intent.getStringExtra("daysOfWeek");

        Calendar alarm = Calendar.getInstance();

        String[] day =  days.split(" ");
        for(int i = 0; i<day.length;i++){
            day[i].trim().equals("monday");
        }
    }
}
