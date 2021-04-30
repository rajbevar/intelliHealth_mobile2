package com.healthcare.bosch.patientapp.utils.model;


import java.util.Comparator;

public class UserPreference {

    private int pref;
    private String mName;


    public UserPreference(int pref, String mName) {
        this.pref = pref;
        this.mName = mName;
    }

    public int getPref() {
        return pref;
    }

    public void setPref(int pref) {
        this.pref = pref;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public static Comparator<UserPreference> prefComparator = new Comparator<UserPreference>() {

        @Override
        public int compare(UserPreference o1, UserPreference o2) {
            int pref1 = o1.getPref();
            int pref2 = o2.getPref();
            return pref1 - pref2;
        }
    };


}
