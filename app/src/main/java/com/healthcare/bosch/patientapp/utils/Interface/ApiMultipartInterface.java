package com.healthcare.bosch.patientapp.utils.Interface;

import com.androidnetworking.error.ANError;

import org.json.JSONObject;

public interface ApiMultipartInterface {
    void onResponse(JSONObject response);
    void onError(ANError error);
    void onProgress(long bytesUploaded, long totalBytes);
}