package com.healthcare.bosch.patientapp.utils.Interface;

import com.androidnetworking.error.ANError;

import org.json.JSONObject;

public interface APIResponse {
    void onResponse(JSONObject response);
    void onError(ANError error);
 }


