package com.healthcare.bosch.patientapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.adapter.ObservationListAdapter;
import com.healthcare.bosch.patientapp.utils.Components.Utilities;
import com.healthcare.bosch.patientapp.utils.Interface.APIResponse;
import com.healthcare.bosch.patientapp.utils.RestCall.RestCallAPI;
import com.healthcare.bosch.patientapp.utils.model.Observation;
import com.healthcare.bosch.patientapp.utils.model.UserPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SummaryActivity extends AppCompatActivity {
    private static final String TAG = SummaryActivity.class.getSimpleName();
    TextView tv_title, tv_description, tv_qty;
    int position = 0;
    RecyclerView recyclerView;
    ObservationListAdapter adapter;
    ArrayList<Observation> lists;
    ArrayList<UserPreference> userPreferences;
    String patientId;
    LinearLayout lnrEmpty;

    Observation glucose;

    HashMap<String, Observation> data = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SummaryActivity.this.finish();
            }
        });

        setUpToolbarColor(true, "");

        tv_title = findViewById(R.id.tv_title);
        tv_description = findViewById(R.id.tv_description);
        tv_qty = findViewById(R.id.tv_qty);
        recyclerView = findViewById(R.id.recyclerView);
        lnrEmpty = findViewById(R.id.lnrEmptyView);
        lists = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("position");
            tv_title.setText(extras.getString("patientMedicalRecord"));
            tv_description.setText(extras.getString("patientName"));
            tv_qty.setText(extras.getString("patientId"));
            //buildTempData(position);//
            patientId = extras.getString("patientId");
            buildUI();
           // getDataFromFHIR();
             buildRecylerDataNew();
        }


    }

    private void getDataFromFHIR() {

        RestCallAPI.getInstance().makeGETRequest(null, null, new APIResponse() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                if (response.has("entry")) {

                                    JSONArray jsonArray = response.getJSONArray("entry");
                                    if (jsonArray != null) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject eventObj = jsonArray.getJSONObject(i);
                                            JSONObject resource = eventObj.getJSONObject("resource");
                                            //get status
                                            String status = resource.getString("status");
                                            if (resource.has("code")) {
                                                // glucose
                                                JSONObject interpretation;
                                                JSONArray codingArr1;
                                                String rangeDisplayCode = "L";
                                                JSONObject codeObj = resource.getJSONObject("code");
                                                JSONArray codingArr = codeObj.getJSONArray("coding");
                                                String nameObj = codingArr.getJSONObject(0).getString("display");
                                                String code = codingArr.getJSONObject(0).getString("code");
                                                JSONObject valueQuantity = null;
                                                JSONObject valueQuantity1 = null;
                                                if (resource.has("valueQuantity")) {
                                                    valueQuantity = resource.getJSONObject("valueQuantity");
                                                } else {
                                                    if (resource.has("component")) {
                                                        JSONObject compomemt = resource.getJSONArray("component").getJSONObject(0);
                                                        JSONObject compomemt1 = resource.getJSONArray("component").getJSONObject(1);
                                                        valueQuantity = compomemt.getJSONObject("valueQuantity");
                                                        valueQuantity1 = compomemt1.getJSONObject("valueQuantity");

                                                    }
                                                }
                                                String val = valueQuantity.getString("value") + " " + valueQuantity.getString("unit");
                                                if (valueQuantity1 != null) {
                                                    val = valueQuantity.getString("value") + "/" + valueQuantity1.getString("value") + " " + valueQuantity.getString("unit");
                                                }

                                                String effectiveDateTime = Utilities.getCurrentDate();
                                                if (resource.has("effectiveDateTime")) {
                                                    effectiveDateTime = resource.getString("effectiveDateTime");
                                                }

                                                if (resource.has("issued")) {
                                                    effectiveDateTime = resource.getString("issued").substring(0, 10);
                                                }
                                                int prefVal = 0;
                                                int drawable = R.drawable.sugar_glucose;
                                                int bgCOlor = R.color.color1;
                                                if (nameObj.contains("Heart")) {
                                                    drawable = R.drawable.cardio;
                                                    bgCOlor = R.color.colorDanger2;
                                                    prefVal = getPrefVal("heart_rate");
                                                } else if (nameObj.contains("Blood pressure")) {
                                                    drawable = R.drawable.blood_pressure;
                                                    interpretation = resource.getJSONObject("interpretation");
                                                    codingArr1 = interpretation.getJSONArray("coding");
                                                    rangeDisplayCode = codingArr1.getJSONObject(0).getString("code");
                                                    bgCOlor = R.color.colorDanger1;
                                                    prefVal = getPrefVal("bp");
                                                } else if (nameObj.contains("Glucose")) {
                                                    drawable = R.drawable.sugar_glucose;
                                                    interpretation = resource.getJSONObject("interpretation");
                                                    codingArr1 = interpretation.getJSONArray("coding");
                                                    rangeDisplayCode = codingArr1.getJSONObject(0).getString("code");
                                                    bgCOlor = R.color.colorDanger2;
                                                    prefVal = getPrefVal("glucose");
                                                } else if (nameObj.contains("Body temperature")) {
                                                    drawable = R.drawable.thermometer;
                                                    interpretation = resource.getJSONObject("interpretation");
                                                    codingArr1 = interpretation.getJSONArray("coding");
                                                    rangeDisplayCode = codingArr1.getJSONObject(0).getString("code");
                                                    bgCOlor = R.color.colorDanger1;
                                                    prefVal = getPrefVal("temp");

                                                }
                                                Log.d("Code value " , code);
                                                data.put(getObservationName(nameObj), new Observation(getObservationName(nameObj), val, bgCOlor, rangeDisplayCode.equalsIgnoreCase("H") ? R.color.colorDanger : R.color.colorDanger2, drawable, "vital", code, prefVal, effectiveDateTime));
                                            }

                                        }
                                    }

                                    lnrEmpty.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    buildRecylerData();
                                } else {
                                   //buildRecylerDataNew();

                                    lnrEmpty.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.d(TAG, error.toString());
                    }
                }, SummaryActivity.this,
                TAG,
                "http://hapi.fhir.org/baseDstu3/Observation?subject=Patient/" + patientId + "&_sort=date", true, "Fetching Observations...");

    }


    private void buildRecylerDataNew() {
        lists = new ArrayList<>();
        lists.add(new Observation("Medications", "Clonidine 10mg", R.color.upcomingMedication, R.color.upcomingMedication, R.drawable.medication, "medications", "", 5, "10AM"));
       // lists.add(new Observation("Lab Results", "", R.color.labResults, R.color.labResults, R.drawable.analytical, "lab", "", 5, ""));
        Collections.sort(lists, Observation.prefComparator);

        adapter.updatelistData(lists);
        adapter.notifyDataSetChanged();
    }
    private void buildRecylerData() {
        try {
            JSONObject obj = new JSONObject(readJSONFromAsset());

            if (obj != null) {
                //get patient pref
                if (obj.has(tv_title.getText().toString())) {
                    JSONObject pref = obj.getJSONObject(tv_title.getText().toString());
                    Log.d("PREF", pref.toString());

                    userPreferences = new ArrayList<>();

                    int glucose = pref.getInt("glucose");
                    int bp = pref.getInt("bp");
                    int temp = pref.getInt("temp");
                    int heart_rate = pref.getInt("heart_rate");
                    userPreferences.add(new UserPreference(glucose, "glucose"));
                    userPreferences.add(new UserPreference(bp, "bp"));
                    userPreferences.add(new UserPreference(temp, "temp"));
                    userPreferences.add(new UserPreference(heart_rate, "heart_rate"));

                    Collections.sort(userPreferences, UserPreference.prefComparator);
                    lists = new ArrayList<>();
                    for (int i = 0; i < userPreferences.size(); i++) {
                        lists.add(getObservationPreference(userPreferences.get(i).getmName()));
                    }
                    lists.add(new Observation("Medications", "Clonidine 10mg", R.color.upcomingMedication, R.color.upcomingMedication, R.drawable.medication, "medications", "", 5, "10AM"));
                    lists.add(new Observation("Lab Results", "", R.color.labResults, R.color.labResults, R.drawable.analytical, "lab", "", 5, ""));
                }
                Collections.sort(lists, Observation.prefComparator);

                adapter.updatelistData(lists);
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void buildUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //setting custom drawable as a divider
        adapter = new ObservationListAdapter(this, getApplicationContext(), lists, tv_qty.getText().toString(), tv_description.getText().toString());
        recyclerView.setAdapter(adapter);
        //assignValues();
    }

    private void setUpToolbarColor(boolean b, String s) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(" ");

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(tv_title.getText().toString());

//                    collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
//                    collapsingToolbar.setExpandedTitleColor(Color.WHITE);
//                    toolbar.setNavigationIcon(R.drawable.ic_cancel_grey_24dp);

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    //   toolbar.setNavigationIcon(R.drawable.ic_close);
                    isShow = false;
                }
            }
        });
    }

    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("patientPref.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    private Observation getObservationPreference(String pref) {
        Observation observation = null;
        if (pref.equalsIgnoreCase("glucose")) {
            observation = data.get("Glucose");
        } else if (pref.equalsIgnoreCase("bp")) {
            observation = data.get("Heart Rate");// new Observation("Blood pressure", "107 mmHg", R.color.color3, R.color.color3, R.drawable.blood_pressure, false);
        } else if (pref.equalsIgnoreCase("heart_rate")) {
            observation = data.get("Blood pressure");//new Observation("Heart Rate", "98 beats/minute", R.color.color4, R.color.color4, R.drawable.cardio, false);
        } else if (pref.equalsIgnoreCase("temp")) {
            observation = data.get("Body temperature");//new Observation("Temperature", "39 degrees C", R.color.color1, R.color.color1, R.drawable.thermometer, false);
        }
        return observation;
    }

    private String getObservationName(String name) {
        if (name.contains("Glucose")) {
            return "Glucose";
        } else if (name.contains("Heart")) {
            return "Heart Rate";
        } else if (name.contains("Blood pressure")) {
            return "Blood pressure";
        } else if (name.contains("Body temperature")) {
            return "Body temperature";
        }
        return "";
    }

    private int getPrefVal(String prefV) {
        try {
            JSONObject obj = new JSONObject(readJSONFromAsset());

            if (obj != null) {
                //get patient pref
                if (obj.has(tv_title.getText().toString())) {
                    JSONObject pref = obj.getJSONObject(tv_title.getText().toString());
                    return pref.getInt(prefV);

                }
            }
        } catch (Exception e) {

        }
        return 0;
    }
}
