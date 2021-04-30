package com.healthcare.bosch.patientapp.utils.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    public static final String KEY = "Innovation_Speech";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String IDENTITY_ID = "IDENTITY_ID";
    public static final String IS_LOGGED_IN = "IS_LOGGED_IN";


    public static String getStringValue(Context context, String key) {
        String user_id = "";
        if (context != null && context.getSharedPreferences(KEY, Context.MODE_PRIVATE) != null) {
            SharedPreferences user_pref = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
            user_id = user_pref.getString(key, "");
        }
        return user_id;
    }

    public static void setStringValue(Context ctx, String key, String value) {
        if (ctx != null) {
            SharedPreferences.Editor edt = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
            edt.putString(key, value);
            edt.commit();
        }
    }

    public static boolean getBooleanValue(Context context, String key) {
        boolean isVal = false;
        if (context != null && context.getSharedPreferences(KEY, Context.MODE_PRIVATE) != null) {
            SharedPreferences user_pref = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
            isVal = user_pref.getBoolean(key, false);
        }
        return isVal;
    }

    public static void setBooleanValue(Context ctx, String key, boolean value) {
        if (ctx != null) {
            SharedPreferences.Editor edt = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
            edt.putBoolean(key, value);
            edt.commit();
        }
    }

    public static void clearPreferences(Context ctx) {
        if (ctx != null) {
            SharedPreferences.Editor edt = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
            edt.clear();
            edt.commit();
        }
    }


    public static void saveUserInfo(Context ctx, String name, String email, String password, String regNo, String contact) {
        if (ctx != null) {
            SharedPreferences.Editor edt = ctx.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
            edt.putString("name", name);
            edt.putString("regNo", regNo);
            edt.putString("contact", contact);
            edt.putString("email", email);
            edt.putString("password", password);
            edt.commit();
        }
    }


}
