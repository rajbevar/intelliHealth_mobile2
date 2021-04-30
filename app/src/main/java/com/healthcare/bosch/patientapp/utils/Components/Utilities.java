package com.healthcare.bosch.patientapp.utils.Components;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.core.app.ActivityCompat;

import com.androidnetworking.error.ANError;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.utils.Interface.AlertResponse;
import com.healthcare.bosch.patientapp.utils.Interface.UtilitiesInterfaces;
import com.healthcare.bosch.patientapp.utils.SessionManager.PreferenceManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.aviran.cookiebar2.CookieBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Utilities {


    public static void showActivity(Activity currentScreen, Class toRidrectScreen) {
        Intent toRedirect = new Intent(currentScreen, toRidrectScreen);
        currentScreen.startActivity(toRedirect);
        // Close the current screen and remove from stack.
        currentScreen.finish();
    }

    public static void showActivity(Activity currentScreen, Class toRidrectScreen, Bundle bundle) {
        Intent toRedirect = new Intent(currentScreen, toRidrectScreen);
        toRedirect.putExtras(bundle);
        currentScreen.startActivity(toRedirect);
        // Close the current screen and remove from stack.
        currentScreen.finish();
    }

    public static void showActivityClosePrevious(Activity currentScreen, Class toRidrectScreen) {
        Intent toRedirect = new Intent(currentScreen, toRidrectScreen);
        // set the new task and clear flags
        toRedirect.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentScreen.startActivity(toRedirect);
    }

    public static void showActivityNotFinished(Activity currentScreen, Class toRidrectScreen) {
        Intent toRedirect = new Intent(currentScreen, toRidrectScreen);
        currentScreen.startActivity(toRedirect);
    }

    public static void showActivityNotFinished(Activity currentScreen, Class toRidrectScreen, Bundle bundle) {
        Intent toRedirect = new Intent(currentScreen, toRidrectScreen);
        toRedirect.putExtras(bundle);
        currentScreen.startActivity(toRedirect);
    }

    /**
     * https://stackoverflow.com/questions/38382283/change-status-bar-text-color-when-primarydark-is-white
     *
     * @param activity
     */
    public static void fullscreenActivity(Activity activity) {
      /*  activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = activity.getWindow();
//            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        Window w = activity.getWindow();
        w.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        w.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            w.setStatusBarColor(Color.TRANSPARENT);
        }


    }

    public static void clearfullscreenActivity(Activity activity) {
      /*  activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = activity.getWindow();
            w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public static boolean haveInternet(Context thisActivity) {
        if (thisActivity != null) {
            NetworkInfo info = ((ConnectivityManager) thisActivity
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();

            if (info == null || !info.isConnected()) {
                return false;
            }
            if (info.isRoaming()) {
                return true;
            }

        }
        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    //http://stackoverflow.com/questions/28578701/create-android-shape-background-programmatically
    public static void setRoundedCorner(View view, float size) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(size);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(shape);
        }
    }

    public static void setRoundedCornerForViews(View views[], float size) {
        for (int loop = 0; loop < views.length; loop++) {
            GradientDrawable shape = new GradientDrawable();
            shape.setCornerRadius(size);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                views[loop].setBackground(shape);
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e("<-- Keyboard -->", e.toString(), e);
        }
    }


    public static HashMap<String, String> attachHeaderParams(Context mCtx) {
        HashMap<String, String> headerParams = new HashMap<>();

        Log.d("Utils", "attachHeaderParams: prefToken " + PreferenceManager.getStringValue(mCtx, PreferenceManager.AUTH_TOKEN));

        headerParams.put(Config.AUTH, "Bearer " + PreferenceManager.getStringValue(mCtx, PreferenceManager.AUTH_TOKEN));
        headerParams.put(Config.ACCEPT, "application/json");
        return headerParams;
    }

    public static HashMap<String, String> attachSpeakerHeaderParams(Context mCtx) {
        HashMap<String, String> headerParams = new HashMap<>();
        headerParams.put("Ocp-Apim-Subscription-Key", Config.SUB_SPEAKER_KEY);
        return headerParams;
    }

    public static SweetAlertDialog progressDialog(Context mCtx, String message) {
        SweetAlertDialog pDialog = new SweetAlertDialog(mCtx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(R.color.progressBarColor);
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        return pDialog;
    }

    public static void showProgressDialog(SweetAlertDialog dialog) {
        if (dialog != null)
            dialog.show();
    }


    public static JSONArray replaceNullWithDouble(JSONArray dataArray, String[] keys) {

        for (int i = 0; i < dataArray.length(); i++) {

            try {
                JSONObject jo = (JSONObject) dataArray.get(i);
                for (String key : keys) {
                    if (jo.has(key)) {
                        try {
                            if (jo.get(key) == null ||
                                    jo.getString(key).equals("null")) {
                                jo.put(key, 0.0);
                                dataArray.put(i, jo);
                                Log.e("replaceNullWithDouble", "onResponse: updated id " + jo.get("id") + " price " + jo.get(key));
                                break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return dataArray;
    }

    public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public static void hideProgressDialog(SweetAlertDialog dialog) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }


    /**
     * Check if textInputLayout contains editText view. If so, then return text value of the view.
     *
     * @param textInputLayout wrapper for the editText view.
     * @return text value of the editText view.
     */
    public static String getTextFromInputLayout(TextInputLayout textInputLayout) {
        if (textInputLayout != null && textInputLayout.getEditText() != null) {
            return textInputLayout.getEditText().getText().toString();
        } else {
            return null;
        }
    }


    /**
     * Check if textInputLayout contains editText view. If so, then set text value to the view.
     *
     * @param textInputLayout wrapper for the editText view where the text value should be set.
     * @param text            text value to display.
     */
    public static void setTextToInputLayout(TextInputLayout textInputLayout, String text) {
        if (textInputLayout != null && textInputLayout.getEditText() != null) {
            textInputLayout.getEditText().setText(text);
        } else {
            Log.d("Setting text to null ", "input wrapper, or without editText");
        }
    }

    public static void logError(String TAG, ANError error) {
        if (error.getErrorCode() != 0) {
            // received ANError from server
            // error.getErrorCode() - the ANError code from server
            // error.getErrorBody() - the ANError body from server
            // error.getErrorDetail() - just a ANError detaill
            Log.e(TAG, "onError errorCode : " + error.getErrorCode());
            Log.e(TAG, "onError errorBody : " + error.getErrorBody());
            Log.e(TAG, "onError errorDetail : " + error.getErrorDetail());

        } else {
            // error.getErrorDetail() : connectionError, parseError, requestCancelledError
            Log.e(TAG, "onError errorDetail : " + error.getErrorDetail());
        }
    }

    public static void errorMessageHandler(Activity activity, ANError error) {
        String errorMessage = error.getErrorBody();
        if (!TextUtils.isEmpty(errorMessage)) {
            try {
                JSONObject totResponseObj = new JSONObject(errorMessage);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /*public static void askPermissionWithReason(Context context, final PermissionRequest request, String messageToDisplay) {

        new AlertDialog.Builder(context)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageToDisplay)
                .show();

    }*/

    public static boolean hasPermissions(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;

    }

    public static String getCurrentDate() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;

    }


    public static String getDateWithTime(Calendar date, String format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String formattedDate = dateFormat.format(date.getTime());
        return formattedDate;

    }

    public static String getCurrentDateWithTime(String format) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;

    }

    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedDate = dateFormat.format(calendar.getTime());
        return formattedDate;

    }

    public static boolean validate(Context context, EditText inputField, String fieldToDisplay, Boolean isEmail, int minLength, int maxLength) {

        String inputString = inputField.getText().toString();
        String errorMsg = "";
        if (inputString.trim().length() == 0) {
            errorMsg = context.getString(R.string.error_should_not_empty, fieldToDisplay);
            inputField.setError(errorMsg);
            inputField.requestFocus();
            return false;
        } else if (isEmail && !isValidEmail(inputString)) {
            errorMsg = context.getString(R.string.error_invalid_email);
            inputField.setError(errorMsg);
            inputField.requestFocus();
            return false;
        } else if (inputString.length() < minLength) {
            errorMsg = context.getString(R.string.error_min_length, fieldToDisplay, minLength);
            inputField.setError(errorMsg);
            inputField.requestFocus();
            return false;
        } else if (inputString.length() > maxLength) {
            errorMsg = context.getString(R.string.error_max_length, fieldToDisplay, maxLength);
            inputField.setError(errorMsg);
            inputField.requestFocus();
            return false;
        }

        return true;
    }


    public static boolean validateWithCookieBar(Context context, EditText inputField, String fieldToDisplay, Boolean isEmail, int minLength, int maxLength) {

        String inputString = inputField.getText().toString();
        String errorMsg = "";
        if (inputString.trim().length() == 0) {
            errorMsg = context.getString(R.string.error_should_not_empty, fieldToDisplay);
            Utilities.showCookieBar((Activity) context, errorMsg, context.getString(R.string.validation_error), false).show();
            inputField.requestFocus();
            return false;
        } else if (isEmail && !isValidEmail(inputString)) {
            errorMsg = context.getString(R.string.error_invalid_email);
            Utilities.showCookieBar((Activity) context, errorMsg, context.getString(R.string.validation_error), false).show();
            inputField.requestFocus();
            return false;
        } else if (inputString.length() < minLength) {
            errorMsg = context.getString(R.string.error_min_length, fieldToDisplay, minLength);
            Utilities.showCookieBar((Activity) context, errorMsg, context.getString(R.string.validation_error), false).show();
            inputField.requestFocus();
            return false;
        } else if (inputString.length() > maxLength) {
            Utilities.showCookieBar((Activity) context, errorMsg, context.getString(R.string.validation_error), false).show();
            inputField.setError(errorMsg);
            inputField.requestFocus();
            return false;
        }

        return true;
    }

    public static CookieBar.Builder showCookieBar(Activity context, String message, String title, Boolean isInternetNA) {
        long duration = 1300;
        if (isInternetNA) {
            duration = 5000;
        }
        return CookieBar.build(context)
                .setTitle(title).setMessage(message)
                .setIcon(R.drawable.caduceus)
                .setDuration(duration);
    }


    public static void showAlertToOpenPlayStore(final Activity activity) {


        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("New version available")
                .setCancelable(false)
                .setMessage("Please, update app to new version to continue.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String appPackageName = activity.getPackageName();
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(
                                        "https://play.google.com/store/apps/details?id=" + Config.APP_ID));
                                intent.setPackage("com.android.vending");
                                activity.startActivity(intent);
                                activity.finish();
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        }).create();
        dialog.show();


    }


    /**
     * Get String From model class
     *
     * @param object - input class to convert
     * @param <T>    - Generic
     * @return - String from model class
     */

    public static <T> String convertModelToString(T object) {
        return new Gson().toJson(object);
    }


    /**
     * @param jsonString - String to convert to model class
     * @param classType  - return class type
     * @param <T>        - Generic class
     * @return - Model for the string
     */

    public static <T> T convertStringToModel(String jsonString, Class<T> classType) {
        return new Gson().fromJson(jsonString, classType);
    }

    /**
     * @param value
     * @param <T>
     * @return
     */
    public static <T> T convertListToString(List<T> value) {
        Gson gson = new Gson();
        String json = gson.toJson(value);
        Log.d("Codesnippet", "convertListToString: json " + json);
        return null;
    }

    /**
     * @param listInString
     * @param <T>
     * @return
     */
    public static <T> T convertStringToList(String listInString) {
        Gson gson = new Gson();
        return gson.fromJson(listInString, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * https://stackoverflow.com/questions/12673357/how-to-get-only-city-state-country-from-lat-long-in-android/20233832
     *
     * @param context
     * @param latitude
     * @param longitude
     * @return
     */

    public static String getState(Context context, double latitude, double longitude) {
        return null;
    }

    public static List<Address> getCountryName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses;
            }
            return null;
        } catch (IOException ignored) {
            //do something
        }
        return null;
    }

    public static String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public static void showSingleAlertConfirm(final Activity activity,
                                              String title, String message, String posBtnText, final AlertResponse alertResponse) {

//1. create a dialog object 'dialog'
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(posBtnText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertResponse.okayPressed();
                                dialog.dismiss();
                            }
                        }).create();
        //2. now setup to change color of the button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });

        dialog.show();


    }

    public static void showAlertConfirm(final Activity activity, final AlertResponse alertResponse,
                                        String title, String message, String posBtnText, String negBtnText) {

//1. create a dialog object 'dialog'
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton(posBtnText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertResponse.okayPressed();
                            }
                        }).setNegativeButton(negBtnText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        }).create();
        //2. now setup to change color of the button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });

        dialog.show();


    }


    public static void requestPermissions(final Activity fromActivity, String[] permissions, UtilitiesInterfaces.PermissionCallback permissionCallback) {

        // To Receive the permission status as callbacks
        final UtilitiesInterfaces.PermissionCallback callback = permissionCallback;

        // Check Private Permissions if >= Mashmallow
        if (Build.VERSION.SDK_INT >= 23) {

            Dexter.withActivity(fromActivity).withPermissions(permissions).withListener(new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {


                    if (report.areAllPermissionsGranted()) {
                        // Perform action
                        callback.receivePermissionStatus(true);
                    } else {

                        // Ask user to enable permission from App's Settings page
                        Utilities.showAlertDialogWithOptions(fromActivity, "This app needs permission to use this feature. You can grant them in app settings.", new String[]{"Cancel", "OK"}, new UtilitiesInterfaces.AlertCallback() {
                            @Override
                            public void onOptionClick(DialogInterface dialog, int buttonIndex) {

                                if (buttonIndex == 1) {

                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", fromActivity.getPackageName(), null);
                                    intent.setData(uri);
                                    fromActivity.startActivityForResult(intent, 101);
                                    // return the callback to perform the action again
                                    // ask the permission again in resume
                                    // callback.receivePermissionStatus(true);

                                } else {
                                    // return the callback to perform the action again
                                    callback.receivePermissionStatus(false);
                                }

                            }
                        });

                    }

                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                    // Ask user to enable
                    token.continuePermissionRequest();
                }

            }).withErrorListener(new PermissionRequestErrorListener() {
                @Override
                public void onError(DexterError error) {
                    Log.e("For Privacy Permission", "There was an error: " + error.toString());
                }
            }).check();

        }
        // If Below Mashmallow return true - since it's not restricted to acess
        else {
            callback.receivePermissionStatus(true);
        }

    }

    /**
     * Create Alertview dynamically maxium of buttons -> 3 minimum buttons -> 1
     *
     * @param fromActivity
     * @param message
     * @param options
     * @param alertCallback
     */
    public static void showAlertDialogWithOptions(Activity fromActivity, String message, String[] options, UtilitiesInterfaces.AlertCallback alertCallback) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(fromActivity);
        alertDialog.setTitle(Config.AppName);
        alertDialog.setMessage(message);
        final UtilitiesInterfaces.AlertCallback callback = alertCallback;
        for (int count = 0; count < options.length; count++) {

            final int buttonIndex = count;
            if (options.length == 3 && count == 0) {
                alertDialog.setNeutralButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });

            } else if ((options.length == 2 && count == 0) || (options.length == 3 && count == 1)) {

                alertDialog.setNegativeButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });

            } else if (options.length == 1 || count == 2 || count == 1) {
                alertDialog.setPositiveButton(options[count], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onOptionClick(dialog, buttonIndex);
                    }
                });
            }

        }
        alertDialog.show();

    }


    //https://stackoverflow.com/questions/11701399/round-up-to-2-decimal-places-in-java
    public static String roundOffTo2DecPlaces(double val) {
        return String.format("%.2f", val);
    }


    /**
     * @param context
     * @return
     */

    public static boolean isUserLoggedIn(Context context) {

        if (PreferenceManager.getBooleanValue(context, PreferenceManager.IS_LOGGED_IN)) {
            return true;
        } else {
            return false;
        }
    }

    public static String EndPoint() {
        String EndPointURl = APIEndPoint.BASE_URL;
        if (Config.ENV.equalsIgnoreCase("DEV")) {
            EndPointURl = APIEndPoint.DEV_BASE_URL;
        }
        return EndPointURl;
    }


    /**
     * @param context
     * @param timeField
     * @return
     */
    public static TimePickerDialog showTimePickerDialog(Context context, final TextView timeField, Date assignDate) {

        // Use the current time as the default values for the picker
        final Calendar c = Utilities.getCalDate(assignDate);//Calendar.getInstance();
        int mhour = c.get(Calendar.HOUR_OF_DAY);
        int mminute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String format = "";
                if (hourOfDay == 0) {
                    hourOfDay += 12;
                    format = "AM";
                } else if (hourOfDay == 12) {
                    format = "PM";
                } else if (hourOfDay > 12) {
                    hourOfDay -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                } //https://stackoverflow.com/questions/8935414/getminutes-0-9-how-to-with-two-numbers
                timeField.setText(new StringBuilder().append((hourOfDay < 10 ? "0" : "") + hourOfDay).append(":").append((minute < 10 ? "0" : "") + minute)
                        .append("").append(format));
            }
        }, mhour, mminute,
                DateFormat.is24HourFormat(context));
    }

    /**
     * @param context
     * @param dateField
     * @param assignDate
     * @return
     */
    public static DatePickerDialog showDatePickerDialog(Context context, final TextView dateField, Date assignDate) {

        // Use the current date as the default values for the picker
        final Calendar c = getCalDate(assignDate);
        int myear = c.get(Calendar.YEAR);
        int mmonth = c.get(Calendar.MONTH);
        int mday = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateField.setText(new StringBuilder().append((dayOfMonth < 10 ? "0" : "") + dayOfMonth).append("/")
                        .append((month < 10 ? "0" : "") + (month + 1)).append("/").append(year));
            }
        }, myear, mmonth, mday);

        DatePicker datePicker = dpicker.getDatePicker();
        datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
        //datePicker.updateDate();


        return dpicker;

    }

    /**
     * @param newDate
     * @return
     */
    public static Calendar getCalDate(Date newDate) {

        if (newDate != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(newDate);
            return c;
        }

        return Calendar.getInstance();
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
     *
     * @param context
     * @param bitmapImage
     * @return
     */
    public static String saveToInternalStorage(Context context, Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(context);
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DCIM), "");
        if (!file.mkdirs()) {
            Log.e("error ===>", "Directory not created");
        }
        // Create imageDir
        File mypath = new File(file, "current.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }


    public static int getRandomInteger(int maximum, int minimum){
        return ((int) (Math.random()*(maximum - minimum))) + minimum;
    }
}




