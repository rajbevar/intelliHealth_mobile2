package com.healthcare.bosch.patientapp.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.utils.ChartHelper.RadarMarkerView;
import com.healthcare.bosch.patientapp.utils.ViewpagerCards.CardItem;
import com.healthcare.bosch.patientapp.utils.ViewpagerCards.CardPagerAdapter;
import com.healthcare.bosch.patientapp.utils.ViewpagerCards.ShadowTransformer;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 3000;

    private ViewPager mViewPager;
    boolean isSecondTime = false;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        mViewPager = findViewById(R.id.viewPager);
        mChart = findViewById(R.id.mChart);
        buildUI();
        drawChartData();

    }

    private void drawChartData() {
        mChart.setDrawGridBackground(false);
        // no description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        LineData data = getData();
        // add some transparency to the color with "& 0x90FFFFFF"
        setupChart(mChart, data, Color.rgb(137, 230, 81));
    }


    private void setupChart(LineChart chart, LineData data, int color) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleColorHole(Color.WHITE);
        // no description text
        chart.getDescription().setEnabled(false);
        // enable touch gestures
        chart.setTouchEnabled(true);
        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);
        chart.setBackgroundColor(Color.TRANSPARENT); //set whatever color you prefer
        chart.setDrawGridBackground(false);// this is a must
        //chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(30, 0, 30, 0);
        // add data
        chart.setData(data);
        // custom marker view
        MarkerView mv = new RadarMarkerView(this, R.layout.custom_chart_marker_view);
        chart.setMarker(mv);
        chart.setDrawMarkers(true);
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);


        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setSpaceTop(40);
        chart.getAxisLeft().setSpaceBottom(40);
        chart.getAxisRight().setEnabled(false);

        chart.getXAxis().setEnabled(false);

        // animate calls invalidate()...
        chart.animateX(2500);
    }


    private LineData getData() {

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(0, Float.parseFloat("3210.00")));
        yVals.add(new Entry(1, Float.parseFloat("3240.25")));
        yVals.add(new Entry(2, Float.parseFloat("3010.00")));
        yVals.add(new Entry(3, Float.parseFloat("3320.75")));
        yVals.add(new Entry(4, Float.parseFloat("3250.00")));
        yVals.add(new Entry(5, Float.parseFloat("3050.60")));

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");

        set1.setLineWidth(3f);
        set1.setCircleRadius(6f);
        set1.setCircleHoleRadius(3f);
        set1.setCircleColorHole(Color.TRANSPARENT);
        set1.setColor(getResources().getColor(R.color.color1));
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.TRANSPARENT);
        set1.setDrawValues(false);
        set1.setValueTextColor(Color.WHITE);
        set1.setValueTextSize(10);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        // create a data object with the datasets
        LineData data = new LineData(set1);

        return data;
    }


    private LineData getDataNew() {

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        yVals.add(new Entry(0, Float.parseFloat("3110.00")));
        yVals.add(new Entry(1, Float.parseFloat("3210.25")));
        yVals.add(new Entry(2, Float.parseFloat("3110.00")));
        yVals.add(new Entry(3, Float.parseFloat("3520.75")));
        yVals.add(new Entry(4, Float.parseFloat("3270.00")));
        yVals.add(new Entry(5, Float.parseFloat("3350.00")));

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");

        set1.setLineWidth(3f);
        set1.setCircleRadius(6f);
        set1.setCircleHoleRadius(3f);
        set1.setCircleColorHole(Color.TRANSPARENT);
        set1.setColor(getResources().getColor(R.color.color2));
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.TRANSPARENT);
        set1.setDrawValues(false);
        set1.setValueTextColor(Color.WHITE);
        set1.setValueTextSize(10);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        // create a data object with the datasets
        LineData data = new LineData(set1);

        return data;
    }

    private void buildUI() {

        mCardAdapter = new CardPagerAdapter(this);
        mCardAdapter.addCardItem(new CardItem("Blood Pressure", "", R.drawable.bp_bg));
        mCardAdapter.addCardItem(new CardItem("Global Trends", "", R.drawable.almonds_bg));
        mCardAdapter.addCardItem(new CardItem("Order Status", "", R.drawable.apple_bg));
        mCardAdapter.addCardItem(new CardItem("Sales Summary", "", R.drawable.chats_bg));
        isSecondTime = true;

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                resetLine(position);
                updateChart(position);
            }
        });
    }

    private void updateChart(int position) {

        if (position % 2 == 0) {
            setupChart(mChart, getData(), Color.rgb(137, 230, 81));
        } else {
            setupChart(mChart, getDataNew(), Color.rgb(137, 230, 81));
        }
        mChart.notifyDataSetChanged();

    }


    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }


    private void resetLine(int position) {


        //Added by prabhu for page indiactor -start

        findViewById(R.id.line1TextView).setBackgroundResource(R.drawable.line_default);
        findViewById(R.id.line2TextView).setBackgroundResource(R.drawable.line_default);
        findViewById(R.id.line3TextView).setBackgroundResource(R.drawable.line_default);
        findViewById(R.id.line4TextView).setBackgroundResource(R.drawable.line_default);

        //end

        if (position == 0) {
            findViewById(R.id.line1TextView).setBackgroundResource(R.drawable.line_selection);
        } else if (position == 1) {
            findViewById(R.id.line2TextView).setBackgroundResource(R.drawable.line_selection);
        } else if (position == 2) {
            findViewById(R.id.line3TextView).setBackgroundResource(R.drawable.line_selection);
        } else if (position == 3) {
            findViewById(R.id.line4TextView).setBackgroundResource(R.drawable.line_selection);
        }


    }

}
