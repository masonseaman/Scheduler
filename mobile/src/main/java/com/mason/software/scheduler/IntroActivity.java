package com.mason.software.scheduler;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean firstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isfirstrun",true);
        if(firstRun==false){
            Intent intent = new Intent(getBaseContext(),CourseList.class);
            startActivity(intent);
        }

        addSlide(AppIntroFragment.newInstance("Welcome to Scheduler","An app to help you organize your courses and assignments", R.drawable.penpaper, ContextCompat.getColor(getApplicationContext(),R.color.colorAccent)));
        addSlide(AppIntroFragment.newInstance("Location permissions","Scheduler uses your location to find classroom buildings near you", R.drawable.gps, ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Enjoy Scheduler!","Get Started", R.drawable.check,Color.DKGRAY));

        askForPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);

        setFadeAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment){
        super.onSkipPressed(currentFragment);
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isfirstrun",false).commit();
        Intent intent = new Intent(getBaseContext(),CourseList.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment){
        super.onSkipPressed(currentFragment);
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isfirstrun",false).commit();
        Intent intent = new Intent(getBaseContext(),CourseList.class);
        startActivity(intent);
    }

}
