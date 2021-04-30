package com.healthcare.bosch.patientapp.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.utils.Components.Utilities;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
             Utilities.showActivity(SplashActivity.this, LoginActivity.class);
      //          Utilities.showActivity(SplashActivity.this, CardRecyclerViewActivity.class);

            }

        }, SPLASH_TIME_OUT);
    }
}
