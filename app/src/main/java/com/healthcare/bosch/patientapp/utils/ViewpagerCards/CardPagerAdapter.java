package com.healthcare.bosch.patientapp.utils.ViewpagerCards;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.utils.Components.Utilities;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {


    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;
    Activity ctx;

    public CardPagerAdapter(Activity ctx) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        this.ctx = ctx;
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public void updateCardItem(List<CardItem> mData) {
        this.mData = mData;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);
        bind(position, mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Onclick", String.valueOf(position));


            }
        });

        Utilities.setRoundedCorner(cardView, 20);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(int position, CardItem item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView contentTextView = (TextView) view.findViewById(R.id.contentTextView);
        ImageView bgImageView = (ImageView) view.findViewById(R.id.imageView);
        Log.d("POSSS", String.valueOf(position));
        if (position == 0) {
            titleTextView.setTextColor(Color.BLACK);
            contentTextView.setTextColor(Color.GRAY);
        } else {
            titleTextView.setTextColor(Color.WHITE);
            contentTextView.setTextColor(Color.WHITE);
        }
        titleTextView.setText(item.getTitle());
        contentTextView.setText(item.getText());
        bgImageView.setImageResource(item.getImage());
        Utilities.setRoundedCorner(bgImageView,30);

    }

}
