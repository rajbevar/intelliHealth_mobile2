package com.healthcare.bosch.patientapp.utils.model;


import java.util.Comparator;

public class Observation {

    private int pref;

    private String mName;
    private String mValue;
    private String code;
    private String effectiveDateTime;
    private int mColor;
    private int bgColor;
    private int imgToShow;
    private String viewType;

    public Observation(String mName, String mValue, int mColor, int bgColor, int imgToShow, String viewType, String code, int pref, String effectiveDateTime) {
        this.mName = mName;
        this.mValue = mValue;
        this.mColor = mColor;
        this.bgColor = bgColor;
        this.imgToShow = imgToShow;
        this.viewType = viewType;
        this.code = code;
        this.pref = pref;
        this.effectiveDateTime = effectiveDateTime;
    }

    public String getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public void setEffectiveDateTime(String effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }

    public int getPref() {
        return pref;
    }

    public void setPref(int pref) {
        this.pref = pref;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }

    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getImgToShow() {
        return imgToShow;
    }

    public void setImgToShow(int imgToShow) {
        this.imgToShow = imgToShow;
    }

    public String isViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public static Comparator<Observation> prefComparator = new Comparator<Observation>() {

        @Override
        public int compare(Observation o1, Observation o2) {
            int pref1 = o1.getPref();
            int pref2 = o2.getPref();
            return pref1 - pref2;
        }
    };
}
