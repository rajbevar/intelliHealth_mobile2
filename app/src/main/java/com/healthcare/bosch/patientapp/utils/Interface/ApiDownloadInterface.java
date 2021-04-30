package com.healthcare.bosch.patientapp.utils.Interface;

import com.androidnetworking.error.ANError;

public interface ApiDownloadInterface {
    void onDownloadComplete();
    void onError(ANError error);
    void onProgress(long bytesUploaded, long totalBytes);
}