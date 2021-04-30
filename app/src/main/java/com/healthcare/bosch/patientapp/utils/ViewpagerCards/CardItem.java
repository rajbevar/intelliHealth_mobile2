package com.healthcare.bosch.patientapp.utils.ViewpagerCards;


public class CardItem {

    private String mTextResource;
    private String mTitleResource;
    private int mImgResource;


    public CardItem(String title, String text, int img) {
        mTitleResource = title;
        mTextResource = text;
        mImgResource = img;
    }

    public String getText() {
        return mTextResource;
    }

    public String getTitle() {
        return mTitleResource;
    }

    public int getImage() {
        return mImgResource;
    }
}
