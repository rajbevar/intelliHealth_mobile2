package com.healthcare.bosch.patientapp.utils.Interface;

import com.androidnetworking.error.ANError;

import org.json.JSONArray;

public interface APIJSONArrayResponse {
    void onResponse(JSONArray response);
    void onError(ANError error);
 }


