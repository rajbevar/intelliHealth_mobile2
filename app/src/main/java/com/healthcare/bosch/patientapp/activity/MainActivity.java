package com.healthcare.bosch.patientapp.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.error.ANError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.healthcare.bosch.patientapp.AlarmReceiver;
import com.healthcare.bosch.patientapp.Application.CustomApplication;
import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.libs.calendardayview.CalendarDayView;
import com.healthcare.bosch.patientapp.utils.Components.Config;
import com.healthcare.bosch.patientapp.utils.Components.Constants;
import com.healthcare.bosch.patientapp.utils.Components.Utilities;
import com.healthcare.bosch.patientapp.utils.Interface.APIResponse;
import com.healthcare.bosch.patientapp.utils.RealmHelper.RealmManager;
import com.healthcare.bosch.patientapp.utils.RestCall.RestCallAPI;
import com.healthcare.bosch.patientapp.utils.SessionManager.PreferenceManager;
import com.healthcare.bosch.patientapp.utils.dbhelper.DBManager;
import com.healthcare.bosch.patientapp.utils.model.MedicationRealmModel;
import com.healthcare.bosch.patientapp.utils.model.Popup;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class MainActivity extends AppCompatActivity {
    HorizontalCalendarView calendarView1;
    CalendarDayView dayView;
    String patientId, from;
    String periodUnit = "";
    String durationUnit = "";
    ArrayList<Popup> popups;
    private String TAG = MainActivity.class.getSimpleName();
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBundleData();

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmManager.getInstance().clearRealmData();
                PreferenceManager.setBooleanValue(MainActivity.this, patientId, false);
                PreferenceManager.setBooleanValue(MainActivity.this, "2645014", false);
                PreferenceManager.setBooleanValue(MainActivity.this, "2645017", false);

                getDataFromFHIRNew();
//                getDataFromFHIR();
            }
        });
        // buildUI();


        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();


        startDate.add(Calendar.DATE, -2);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 2);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView1)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                fetchPopupByDate(Utilities.getDateWithTime(date, "yyyy-MM-dd"));
                dayView.setPopups(popups);
                dayView.refresh();
            }
        });
    }

    private void getBundleData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patientId = extras.getString("patientId");
            from = extras.getString("from");

            new PostTask().execute();
        }


    }

    private void getDataFromFHIRNew() {
        RestCallAPI.getInstance().makeGETRequest(null, null, new APIResponse() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                if (response.has("result")) {
                                    popups = new ArrayList<>();
                                    String title = "";
                                    String startDate = "";
                                    String endDate = "";
                                    String usageWhen = "";
                                    String frequencys = "";
                                    JSONObject responseObj = response.getJSONObject("result");

                                    JSONArray jsonArray = responseObj.getJSONArray("results");
                                    if (jsonArray != null) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject eventObj = jsonArray.getJSONObject(i);
                                            String medicationId = eventObj.getString("id");
                                            String coding = eventObj.getString("code");
                                            String name = eventObj.getString("name");
                                            String frenquency = eventObj.getString("frenquency");
                                            String doage = eventObj.getString("doage");
                                            frequencys = eventObj.getString("duration");
                                            String frenquencyInDay = eventObj.getString("frenquencyInDay");
                                            String frequencyTimings = eventObj.getString("frequencyTimings");
                                            String effectiveDate = eventObj.getString("effectiveDate");

                                            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                                            DateTime stDate = formatter.parseDateTime(effectiveDate.length() > 10 ? effectiveDate.substring(0, 10) : effectiveDate);
                                            for (int k = 0; k < Integer.parseInt(frequencys) + 1; k++) {
                                                DateTime stDate1 = stDate.plusDays(k);
                                                for (int kk = 0; kk < Integer.parseInt(frenquencyInDay); kk++) {

                                                    int hourOfDay = 8;
                                                    if (frenquency.contains("breakfast") || frenquency.contains("dinner") || frenquency.contains("lunch")) {
                                                        usageWhen = frenquency;
                                                        if (usageWhen.contains("lunch")) {
                                                            hourOfDay = Utilities.getRandomInteger(12, 8);
                                                        } else if (usageWhen.equalsIgnoreCase("AFTN")) {
                                                            hourOfDay = Utilities.getRandomInteger(16, 12);
                                                        } else if (usageWhen.equalsIgnoreCase("EVE")) {
                                                            hourOfDay = Utilities.getRandomInteger(20, 16);
                                                        } else if (usageWhen.equalsIgnoreCase("dinner")) {
                                                            hourOfDay = Utilities.getRandomInteger(24, 20);
                                                        }
                                                    }

                                                    // parse and store data
                                                    RealmManager.getInstance().beginTrans();
                                                    MedicationRealmModel medicationRealmModel = new MedicationRealmModel();
                                                    //medicationRealmModel.setEndDate(stDate.plus(k).toString(formatter));

                                                    medicationRealmModel.setStartDate(stDate1.plusHours(hourOfDay).toString(formatter));

                                                    medicationRealmModel.setEndDate(stDate1.plusHours(hourOfDay).toString(formatter));
                                                    medicationRealmModel.setFrequency(frequencys);
                                                    medicationRealmModel.setMedicineDesc(coding);
                                                    medicationRealmModel.setMedicineName(name);
                                                    medicationRealmModel.setStatus("not taken1");
                                                    medicationRealmModel.setWhen(usageWhen);
                                                    medicationRealmModel.setPendingIntentId(Integer.parseInt(patientId));
                                                    Random r = new Random();
                                                    int notification_id = 100000 + r.nextInt(900000);
                                                    medicationRealmModel.setId(Integer.parseInt(patientId) + notification_id);

                                                    CustomApplication.getRealmContext().copyToRealmOrUpdate(medicationRealmModel);
                                                    RealmManager.getInstance().commitTrans();

                                                    PreferenceManager.setBooleanValue(MainActivity.this, patientId, true);

                                                }
                                            }
                                        }
                                    }
                                }

                                fetchPopupByDate(Utilities.getCurrentDate());
                                setRemainder(Utilities.getCurrentDate());
                                dayView.setPopups(popups);
                                dayView.refresh();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.d(TAG, error.toString());
                    }
                }, MainActivity.this,
                TAG,
                String.format(Config.patientMedicationAPI, patientId), true, "Fetching Details...");


    }

    private void getDataFromFHIR() {
//http://hapi.fhir.org/baseDstu3/MedicationRequest?patient=2645014

        RestCallAPI.getInstance().makeGETRequest(null, null, new APIResponse() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                if (response.has("entry")) {
                                    popups = new ArrayList<>();
                                    String title = "";
                                    String startDate = "";
                                    String endDate = "";
                                    String usageWhen = "";
                                    String frequencys = "";

                                    JSONArray jsonArray = response.getJSONArray("entry");
                                    if (jsonArray != null) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject eventObj = jsonArray.getJSONObject(i);
                                            JSONObject resource = eventObj.getJSONObject("resource");
                                            String medicationId = resource.getString("id");
                                            String status = resource.getString("status");
                                            JSONArray coding = null;
                                            if (status.equalsIgnoreCase("active")) {
                                                if (resource.has("category")) {
                                                    JSONObject category = resource.getJSONObject("category");
                                                    coding = category.getJSONArray("coding");
                                                }
                                                if (resource.has("dosageInstruction")) {
                                                    periodUnit = "";
                                                    JSONArray dosageInstruction = resource.getJSONArray("dosageInstruction");
                                                    for (int iii = 0; iii < dosageInstruction.length(); iii++) {
                                                        JSONObject dosageObj = dosageInstruction.getJSONObject(iii);
                                                        if (dosageObj != null) {
                                                            String text = dosageObj.getString("text");
                                                            if (coding != null) {
                                                                title = coding.getJSONObject(iii).getString("display");
                                                            }
                                                            if (dosageObj.has("timing")) {
                                                                JSONObject timingObj = dosageObj.getJSONObject("timing");
                                                                if (timingObj != null) {
                                                                    JSONObject repeatObj = timingObj.getJSONObject("repeat");
                                                                    JSONObject boundsPeriod = repeatObj.getJSONObject("boundsPeriod");
                                                                    if (boundsPeriod != null) {
                                                                        String start = boundsPeriod.getString("start");
                                                                        String end = boundsPeriod.getString("end");
                                                                        startDate = start;
                                                                        endDate = end;
                                                                    }
                                                                    String frequency = repeatObj.getString("frequency");
                                                                    String period = repeatObj.getString("period");
                                                                    if (repeatObj.has("periodUnit")) {
                                                                        periodUnit = repeatObj.getString("periodUnit");
                                                                    }
                                                                    if (repeatObj.has("durationUnit")) {
                                                                        durationUnit = repeatObj.getString("durationUnit");
                                                                    }

                                                                    frequencys = frequency;

                                                                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
                                                                    DateTime stDate = formatter.parseDateTime(startDate.length() > 10 ? startDate.substring(0, 10) : startDate);
                                                                    DateTime enDate = formatter.parseDateTime(endDate.length() > 10 ? endDate.substring(0, 10) : endDate);
                                                                    for (int k = 0; k < Integer.parseInt(frequencys) + 1; k++) {
                                                                        DateTime stDate1 = stDate.plusDays(k);
                                                                        for (int kk = 0; kk < Integer.parseInt(period); kk++) {

                                                                            int hourOfDay = 8;
                                                                            if (repeatObj.has("when")) {

                                                                                String when = repeatObj.getJSONArray("when").getString(kk);
                                                                                usageWhen = when;
                                                                                if (usageWhen.equalsIgnoreCase("MORN")) {
                                                                                    hourOfDay = Utilities.getRandomInteger(12, 8);
                                                                                } else if (usageWhen.equalsIgnoreCase("AFTN")) {
                                                                                    hourOfDay = Utilities.getRandomInteger(16, 12);
                                                                                } else if (usageWhen.equalsIgnoreCase("EVE")) {
                                                                                    hourOfDay = Utilities.getRandomInteger(20, 16);
                                                                                } else if (usageWhen.equalsIgnoreCase("NIGHT")) {
                                                                                    hourOfDay = Utilities.getRandomInteger(24, 20);
                                                                                }


                                                                                // parse and store data
                                                                                RealmManager.getInstance().beginTrans();
                                                                                MedicationRealmModel medicationRealmModel = new MedicationRealmModel();
                                                                                //medicationRealmModel.setEndDate(stDate.plus(k).toString(formatter));

                                                                                medicationRealmModel.setStartDate(stDate1.plusHours(hourOfDay).toString(formatter));

                                                                                medicationRealmModel.setEndDate(endDate);
                                                                                medicationRealmModel.setFrequency(frequencys);
                                                                                medicationRealmModel.setMedicineDesc("");
                                                                                medicationRealmModel.setMedicineName(title);
                                                                                medicationRealmModel.setStatus("not taken");
                                                                                medicationRealmModel.setWhen(usageWhen);
                                                                                medicationRealmModel.setPendingIntentId(Integer.parseInt(patientId));
                                                                                Random r = new Random();
                                                                                int notification_id = 100000 + r.nextInt(900000);
                                                                                medicationRealmModel.setId(Integer.parseInt(patientId) + notification_id + Integer.parseInt(medicationId));

                                                                                CustomApplication.getRealmContext().copyToRealmOrUpdate(medicationRealmModel);
                                                                                RealmManager.getInstance().commitTrans();

                                                                                PreferenceManager.setBooleanValue(MainActivity.this, patientId, true);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                // Set popup
//                                                RealmManager.getInstance().beginTrans();
                                            }
                                        }
                                    }
                                }
                                fetchPopupByDate(Utilities.getCurrentDate());
                                setRemainder(Utilities.getCurrentDate());
                                dayView.setPopups(popups);
                                dayView.refresh();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.d(TAG, error.toString());
                    }
                }, MainActivity.this,
                TAG, "http://hapi.fhir.org/baseDstu3/MedicationRequest?patient=" + patientId, true, "Fetching Details...");

    }

    private void setRemainder(String currentDate) {


        ArrayList<MedicationRealmModel> arrayList = RealmManager.getInstance().getMedicationReqByDate(Integer.parseInt(patientId), currentDate);
        for (int i = 0; i < arrayList.size(); i++) {
            int when = getWhen(arrayList.get(i).getWhen());

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            DateTime stDate = formatter.parseDateTime(currentDate);
            stDate = stDate.withHourOfDay(when);
            Calendar calendar = Calendar.getInstance();
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //creating a new intent specifying the broadcast receiver
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            intent.putExtra("patientId", arrayList.get(i).getPendingIntentId());
            intent.putExtra("id", arrayList.get(i).getId());
            //creating a pending intent using the intent
            PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, arrayList.get(i).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //setting the   alarm that will be fired
            calendar.set(Calendar.HOUR_OF_DAY, stDate.minusMinutes(10).hourOfDay().get());
            calendar.set(Calendar.MINUTE, stDate.minusMinutes(10).getMinuteOfHour());
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            am.set(AlarmManager.RTC, calendar.getTimeInMillis(), pi);
        }
    }


    // The definition of our task class
    private class PostTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            return "All Done!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            buildUI();
            if (from != null) {
                if (from.equalsIgnoreCase("notification")) {
                    fetchPopupByDate(Utilities.getCurrentDate());
                    dayView.setPopups(popups);
                    dayView.refresh();
                }
            } else {
                RealmManager.getInstance().clearRealmData();
                PreferenceManager.setBooleanValue(MainActivity.this, patientId, false);
                PreferenceManager.setBooleanValue(MainActivity.this, "2645014", false);
                PreferenceManager.setBooleanValue(MainActivity.this, "2645017", false);

                getDataFromFHIRNew();
//                getDataFromFHIR();

            }
        }
    }

    private void buildUI() {
        calendarView1 = findViewById(R.id.calendarView1);
        dayView = findViewById(R.id.dayView);
        dayView.setLimitTime(6, 22);
    }

    private void fetchPopupByDate(String date) {
        popups = new ArrayList<>();
        ArrayList<MedicationRealmModel> arrayList = RealmManager.getInstance().getMedicationReqByDate(Integer.parseInt(patientId), date);
        for (int i = 0; i < arrayList.size(); i++) {
            int when = getWhen(arrayList.get(i).getWhen());

            Calendar timeStart = Calendar.getInstance();
            timeStart.set(Calendar.HOUR_OF_DAY, when);
            timeStart.set(Calendar.MINUTE, 0);
            Calendar timeEnd = (Calendar) timeStart.clone();
            timeEnd.set(Calendar.HOUR_OF_DAY, when);
            timeEnd.set(Calendar.MINUTE, 30);

            Popup popup = new Popup();
            popup.setStartTime(timeStart);
            popup.setEndTime(timeEnd);
            popup.setTitle(arrayList.get(i).getMedicineName());
            popup.setDescription(arrayList.get(i).getMedicineName());
            popup.setQuote(arrayList.get(i).getStatus());
            popups.add(popup);
        }

    }

    public int getWhen(String usagewhen) {
        int when = 0;

        if (usagewhen.equalsIgnoreCase("MORN")) {
            return Config.MORN;
        } else if (usagewhen.equalsIgnoreCase("AFTN")) {
            return Config.AFTN;
        } else if (usagewhen.equalsIgnoreCase("EVE")) {
            return Config.EVE;
        } else if (usagewhen.equalsIgnoreCase("NIGHT")) {
            return Config.NIGHT;
        } else if (usagewhen.contains("breakfast and dinner")) {
            return Utilities.getRandomInteger(22, 8);
        } else if (usagewhen.contains("breakfast")) {
            return Utilities.getRandomInteger(12, 8);
        } else if (usagewhen.contains("lunch")) {
            return Utilities.getRandomInteger(17, 12);
        } else if (usagewhen.contains("dinner")) {
            return Utilities.getRandomInteger(22, 17);
        }

        return when;
    }

}
