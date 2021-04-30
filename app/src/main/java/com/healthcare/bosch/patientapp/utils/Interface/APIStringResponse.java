package com.healthcare.bosch.patientapp.utils.Interface;

import com.androidnetworking.error.ANError;

public interface APIStringResponse {
    void onResponse(String response);
    void onError(ANError error);
 }


