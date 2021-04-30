package com.healthcare.bosch.patientapp.utils.RestCall;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.utils.Components.APIEndPoint;
import com.healthcare.bosch.patientapp.utils.Components.Config;
import com.healthcare.bosch.patientapp.utils.Components.Utilities;
import com.healthcare.bosch.patientapp.utils.Interface.APIHTTPResponse;
import com.healthcare.bosch.patientapp.utils.Interface.APIJSONArrayResponse;
import com.healthcare.bosch.patientapp.utils.Interface.APIResponse;
import com.healthcare.bosch.patientapp.utils.Interface.APIStringResponse;
import com.healthcare.bosch.patientapp.utils.Interface.ApiDownloadInterface;
import com.healthcare.bosch.patientapp.utils.Interface.ApiMultipartInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Response;


public class RestCallAPI {
    private static final RestCallAPI ourInstance = new RestCallAPI();

    public static RestCallAPI getInstance() {
        if (ourInstance != null) {
            return ourInstance;
        } else {
            return new RestCallAPI();
        }
    }

    public RestCallAPI() {

    }

    public String EndPoint(String endPoint) {
        String EndPointURl = APIEndPoint.BASE_URL;
        if (Config.ENV.equalsIgnoreCase("DEV")) {
            EndPointURl = APIEndPoint.DEV_BASE_URL;
        }
        if (endPoint.startsWith("https://")) {
            EndPointURl = "";
        }
        return EndPointURl;
    }

    public void makePOSTRequest(HashMap<String, String> params,
                                final APIResponse apiInterface,
                                final String TAG, Activity activity,
                                String endPoint, boolean showLoading,
                                String loaderText,
                                HashMap<String, String> headerParams) {

        if (!Utilities.haveInternet(activity)) {
            Utilities.showCookieBar(activity, activity.getString(R.string.internet_check), activity.getString(R.string.error_internet_check), true).show();
            return;
        }
        String EndPointURl = EndPoint(endPoint);
        final SweetAlertDialog dialog = Utilities.progressDialog(activity, loaderText);
        if (showLoading) {
            Utilities.showProgressDialog(dialog);
        }
        AndroidNetworking.post(EndPointURl + endPoint)
                .addBodyParameter(params)
                .addHeaders(headerParams)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onError(error);
                    }

                });
    }


    public void makePOSTRequestJSON(JSONObject params,
                                    final APIStringResponse apiInterface,
                                    final String TAG, Activity activity,
                                    String endPoint, boolean showLoading,
                                    String loaderText,
                                    HashMap<String, String> headerParams) {

        if (!Utilities.haveInternet(activity)) {
            Utilities.showCookieBar(activity, activity.getString(R.string.internet_check), activity.getString(R.string.error_internet_check), true).show();
            return;
        }
        String EndPointURl = EndPoint(endPoint);
        final SweetAlertDialog dialog = Utilities.progressDialog(activity, loaderText);
        if (showLoading) {
            Utilities.showProgressDialog(dialog);
        }
        AndroidNetworking.post(EndPointURl + endPoint)
                .addJSONObjectBody(params)
                .addHeaders(headerParams)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onError(error);
                    }

                });
    }


    public void makePOSTRequestFILE(HashMap<String, String> params,
                                    final APIResponse apiInterface,
                                    final String TAG, Activity activity,
                                    String endPoint, boolean showLoading,
                                    String loaderText,
                                    HashMap<String, String> headerParams, File file) {

        if (!Utilities.haveInternet(activity)) {
            Utilities.showCookieBar(activity, activity.getString(R.string.internet_check), activity.getString(R.string.error_internet_check), true).show();
            return;
        }
        final SweetAlertDialog dialog = Utilities.progressDialog(activity, loaderText);
        if (showLoading) {
            Utilities.showProgressDialog(dialog);
        }
        AndroidNetworking.post(endPoint).addFileBody(file)
                .addHeaders(headerParams)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onError(error);
                    }

                });
    }

    public void makePOSTRequestHTTPWithFILE(HashMap<String, String> params,
                                            final APIHTTPResponse apiInterface,
                                            final String TAG, Activity activity,
                                            String endPoint, boolean showLoading,
                                            String loaderText,
                                            HashMap<String, String> headerParams, File file) {

        if (!Utilities.haveInternet(activity)) {
            Utilities.showCookieBar(activity, activity.getString(R.string.internet_check), activity.getString(R.string.error_internet_check), true).show();
            return;
        }
        final SweetAlertDialog dialog = Utilities.progressDialog(activity, loaderText);
        if (showLoading) {
            Utilities.showProgressDialog(dialog);
        }

        AndroidNetworking.post(endPoint)
                .addFileBody(file)
                .addHeaders(headerParams)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                }).getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response response) {
                Utilities.hideProgressDialog(dialog);
                apiInterface.onResponse(response);
            }

            @Override
            public void onError(ANError anError) {
                Utilities.hideProgressDialog(dialog);
                apiInterface.onError(anError);
            }
        });

    }


    public void makeGETRequest(HashMap<String, String> params, HashMap<String, String> headerParams,
                               final APIResponse apiInterface,
                               Activity activity, final String TAG, String EndPointURl, boolean showLoading, String loaderText) {

        if (!Utilities.haveInternet(activity)) {
            Utilities.showCookieBar(activity, activity.getString(R.string.internet_check), activity.getString(R.string.error_internet_check), true).show();
            return;
        }
        final SweetAlertDialog dialog = Utilities.progressDialog(activity, loaderText);
        if (showLoading) {
            Utilities.showProgressDialog(dialog);
        }
        AndroidNetworking.get(EndPointURl).addPathParameter(params)
                .addHeaders(headerParams)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onError(error);
                    }
                });
    }


    public void makeGETRequestJSONArray(HashMap<String, String> params, HashMap<String, String> headerParams,
                                        final APIJSONArrayResponse apiInterface,
                                        Activity activity, final String TAG, String EndPointURl, boolean showLoading, String loaderText) {

        if (!Utilities.haveInternet(activity)) {
            Utilities.showCookieBar(activity, activity.getString(R.string.internet_check), activity.getString(R.string.error_internet_check), true).show();
            return;
        }
        final SweetAlertDialog dialog = Utilities.progressDialog(activity, loaderText);
        if (showLoading) {
            Utilities.showProgressDialog(dialog);
        }
        AndroidNetworking.get(EndPointURl).addPathParameter(params)
                .addHeaders(headerParams)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onError(error);
                    }
                });
    }


    public void multiPartRequest(HashMap<String, String> params, File file,
                                 final ApiMultipartInterface apiInterface,
                                 boolean attachHeaders,
                                 Context ctxt, final String TAG,
                                 Activity activity, String endPoint,
                                 String uploadName, HashMap<String, String> headerParams) {

        if (!Utilities.haveInternet(activity)) {
            Utilities.showCookieBar(activity, activity.getString(R.string.error_internet_check), activity.getString(R.string.internet_check), true).show();
            return;
        }
        String EndPointURl = EndPoint(endPoint);
        final SweetAlertDialog dialog = Utilities.progressDialog(activity, "Loading...");
        Utilities.showProgressDialog(dialog);
        ANRequest.MultiPartBuilder request = AndroidNetworking.upload(EndPointURl + endPoint)
                .addMultipartParameter(params)
                .addHeaders(headerParams)
                .setTag("FILEUPLOAD")
                .setPriority(Priority.HIGH);
        // check file is null or not
        if (file != null && !file.equals("")) {
            request.addMultipartFile(uploadName, file);
        }

        request.build().setUploadProgressListener(new UploadProgressListener() {
            @Override
            public void onProgress(long bytesUploaded, long totalBytes) {
                // do anything with progress
                apiInterface.onProgress(bytesUploaded, totalBytes);
            }
        })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        Utilities.hideProgressDialog(dialog);
                        apiInterface.onError(error);
                    }
                });
    }


    public void downloadRequest(HashMap<String, String> params, File file, final ApiDownloadInterface apiInterface, boolean attachHeaders, Context ctxt, final String TAG) {
        HashMap<String, String> headerParams = new HashMap<>();
        if (attachHeaders) {
            headerParams = Utilities.attachHeaderParams(ctxt);
        }
        String EndPointURl = EndPoint("");
        String dirPath = "";
        String fileName = "";
        AndroidNetworking.download(EndPointURl, dirPath, fileName)
                .setTag("downloadTest")
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        // do anything with progress
                        apiInterface.onProgress(bytesDownloaded, totalBytes);
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        // do anything after completion
                        apiInterface.onDownloadComplete();
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        apiInterface.onError(error);
                    }
                });
    }


    public void makePOSTMapRequest(HashMap<String, String> params, final APIResponse apiInterface, boolean attachHeaders, Context ctxt, final String TAG, Activity activity, String endPoint) {
        HashMap<String, String> headerParams = new HashMap<>();
        if (attachHeaders) {
            headerParams = Utilities.attachHeaderParams(ctxt);
        }
        if (!Utilities.haveInternet(activity)) {
            Utilities.showCookieBar(activity, activity.getString(R.string.error_internet_check), activity.getString(R.string.internet_check), true).show();
            return;
        }
        AndroidNetworking.post(endPoint)
                .addBodyParameter(params)
                .addHeaders(headerParams)
                .setPriority(Priority.MEDIUM)
                .build()
                .setAnalyticsListener(new AnalyticsListener() {
                    @Override
                    public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived, boolean isFromCache) {
                        Log.d(TAG, " timeTakenInMillis : " + timeTakenInMillis);
                        Log.d(TAG, " bytesSent : " + bytesSent);
                        Log.d(TAG, " bytesReceived : " + bytesReceived);
                        Log.d(TAG, " isFromCache : " + isFromCache);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        apiInterface.onResponse(response);
                    }

                    @Override
                    public void onError(ANError error) {
                        apiInterface.onError(error);
                    }

                });
    }

}
