package com.healthcare.bosch.patientapp.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.healthcare.bosch.patientapp.AlarmReceiver;
import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.utils.Components.Config;
import com.healthcare.bosch.patientapp.utils.Components.Utilities;
import com.healthcare.bosch.patientapp.utils.Interface.APIResponse;
import com.healthcare.bosch.patientapp.utils.RestCall.RestCallAPI;
import com.healthcare.bosch.patientapp.utils.SessionManager.PreferenceManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    EditText eTxtEmail, eTxtPassword;
    TextView txtViewForgetPassword, txtViewRegister;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);
        outlets();
        buildUI();

    }


    private void outlets() {
        eTxtEmail = findViewById(R.id.eTxtEmail);
        eTxtPassword = findViewById(R.id.eTxtPassword);
        txtViewForgetPassword = findViewById(R.id.txtViewForgetPassword);
        txtViewRegister = findViewById(R.id.txtViewRegister);
        fab = findViewById(R.id.fab);
        defaultValue();
    }

    private void buildUI() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    //api call to server
                    if (checkCred()) {
                        loginAPICall();
                    }
                }

            }
        });
        txtViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private boolean validate() {
        if (!Utilities.validateWithCookieBar(this, eTxtEmail, "Email", true, 1, 100)) {
            return false;
        } else if (!Utilities.validateWithCookieBar(this, eTxtPassword, "Password", false, 6, 100)) {
            return false;
        }
        return true;
    }


    private void loginAPICall() {
        RestCallAPI.getInstance().makeGETRequest(null, null, new APIResponse() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            Bundle bundle = new Bundle();

                            if (response != null) {
                                try {
                                    if (!JSONObject.NULL.equals(response.get("result"))) {

                                        JSONObject resultObj = response.getJSONObject("result");

                                        if (resultObj != null) {
                                            bundle.putInt("position", 0);
                                            bundle.putString("patientId", resultObj.getString("externalId"));
                                            bundle.putString("patientName", resultObj.getString("firstname") + " " + resultObj.getString("lastname"));
                                            bundle.putString("patientMedicalRecord", "Heart Attack");
                                            Utilities.showActivity(LoginActivity.this, SummaryActivity.class, bundle);
                                        }
                                    } else {
                                        Utilities.showCookieBar(LoginActivity.this, "User not exist", "Warning", false).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.d(TAG, error.toString());
                    }
                }, LoginActivity.this,
                TAG,
                String.format(Config.patientAPI, eTxtEmail.getText().

                        toString()), true, "Verifying...");


    }

    private void defaultValue() {
        eTxtEmail.setText("");
        eTxtPassword.setText("");
    }

    private boolean checkCred() {
        if (!Utilities.validateWithCookieBar(this, eTxtEmail, "Email", true, 1, 100)) {
            return false;
        }

        if (!Utilities.validateWithCookieBar(this, eTxtPassword, "Password", false, 5, 100)) {
            return false;
        }


        return true;
    }
}
