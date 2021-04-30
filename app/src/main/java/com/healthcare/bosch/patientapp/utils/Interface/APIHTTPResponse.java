package com.healthcare.bosch.patientapp.utils.Interface;

import com.androidnetworking.error.ANError;

import okhttp3.Response;

public interface APIHTTPResponse {
    void onResponse(Response response);
    void onError(ANError error);
 }


