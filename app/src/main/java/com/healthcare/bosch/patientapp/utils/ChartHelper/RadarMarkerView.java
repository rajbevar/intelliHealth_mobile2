package com.healthcare.bosch.patientapp.utils.ChartHelper;


import android.content.Context;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.healthcare.bosch.patientapp.R;

import java.text.DecimalFormat;

public class RadarMarkerView extends MarkerView {

    private TextView tvContent, tvContent1;
    private ImageView imgArrow;

    private DecimalFormat format = new DecimalFormat("##0");

    public RadarMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
        tvContent1 = findViewById(R.id.tvContent1);
        imgArrow = findViewById(R.id.imgArrow);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(String.valueOf(e.getY()));
        tvContent1.setText("testing");
        imgArrow.setImageResource(R.drawable.caduceus);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }


}