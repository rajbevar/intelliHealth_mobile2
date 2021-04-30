package com.healthcare.bosch.patientapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.androidnetworking.error.ANError;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.utils.Components.Utilities;
import com.healthcare.bosch.patientapp.utils.Interface.APIResponse;
import com.healthcare.bosch.patientapp.utils.RestCall.RestCallAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DetailedActivity extends AppCompatActivity {

    BarChart chart;
    String patientId, code, name, lastReading;
    private String TAG = DetailedActivity.class.getSimpleName();
    TextView txtPatientName, txtPatientId;
    private LineChart chartLine;
    float barWidth;
    float barSpace;
    float groupSpace;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);
        SegmentedButtonGroup segmentedButtonGroup = (SegmentedButtonGroup) findViewById(R.id.buttonGroup_lordOfTheRings);

        segmentedButtonGroup.setPosition(1, true);
        //getting the toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtPatientName = (TextView) findViewById(R.id.txtPatientName);
        txtPatientId = (TextView) findViewById(R.id.txtPatientId);


        barWidth = 0.3f;
        barSpace = 0f;
        groupSpace = 0.4f;
//        groupSpace = 0.5f;
//        barSpace = 0.05f; // x2 dataset
//        barWidth = 0.46f; // x2 dataset


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailedActivity.this.finish();
            }
        });
        toolbar.setTitle("");

        //setting the title
        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);

        chart = findViewById(R.id.barchart);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(30);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(true);

        chart.setDrawGridBackground(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);


        //test for live chart - ECG
        chartLine = findViewById(R.id.chartLine);
        chartLine.setDrawGridBackground(false);
        chartLine.getDescription().setEnabled(false);
        chartLine.setNoDataText("No chart data available. Use the menu to add entries and data sets!");
        chartLine.invalidate();

        getBundleData();
        addEntry();


    }

    private final int[] colors = ColorTemplate.VORDIPLOM_COLORS;

    private void getBundleData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            patientId = extras.getString("patientId");
            code = extras.getString("code");
            name = extras.getString("name");
            toolbar.setTitle(extras.getString("patientName"));

        }
        //get data from FHIR
        getDataFromFHIR(code);
    }

    private void getDataFromFHIR(String code) {
        //http://hapi.fhir.org/baseDstu3/Observation?patient=2645014&code=85354-9   // bp
        RestCallAPI.getInstance().makeGETRequest(null, null, new APIResponse() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            if (code.equalsIgnoreCase("85354-9")) {
                                parseBPData(response);
                            } else {
                                parseSimpleBarData(response);
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {

                        Log.d(TAG, error.toString());
                    }
                }, DetailedActivity.this,
                TAG,
                "http://hapi.fhir.org/baseDstu3/Observation?subject=Patient/" + patientId + "&code=" + code + "&_sort=date", true, "Fetching Details...");


    }

    private void parseBPData(JSONObject response) {
        ArrayList<BarEntry> values = new ArrayList<>();
        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList();

        try {
            if (response.has("entry")) {
                JSONArray jsonArray = response.getJSONArray("entry");
                if (jsonArray != null) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject eventObj = jsonArray.getJSONObject(i);
                        JSONObject resource = eventObj.getJSONObject("resource");
                        //get status
                        String status = resource.getString("status");
                        if (resource.has("code")) {
                            // glucose
                            JSONObject interpretation;
                            JSONArray codingArr1;
                            String rangeDisplayCode = "L";
                            JSONObject codeObj = resource.getJSONObject("code");
                            JSONArray codingArr = codeObj.getJSONArray("coding");
                            String nameObj = codingArr.getJSONObject(0).getString("display");
                            String code = codingArr.getJSONObject(0).getString("code");
                            JSONObject valueQuantity = null;
                            JSONObject valueQuantity1 = null;
                            if (resource.has("valueQuantity")) {
                                valueQuantity = resource.getJSONObject("valueQuantity");
                            } else {
                                if (resource.has("component")) {
                                    JSONObject compomemt = resource.getJSONArray("component").getJSONObject(0);
                                    JSONObject compomemt1 = resource.getJSONArray("component").getJSONObject(1);
                                    valueQuantity = compomemt.getJSONObject("valueQuantity");
                                    valueQuantity1 = compomemt1.getJSONObject("valueQuantity");

                                }
                            }
                            String effectiveDateTime = Utilities.getCurrentDate();
                            if (resource.has("effectiveDateTime")) {
                                effectiveDateTime = resource.getString("effectiveDateTime");
                                xVals.add(effectiveDateTime);
                            }
                            values.add(new BarEntry(i, Float.parseFloat(valueQuantity.getString("value"))));
                            values1.add(new BarEntry(i, Float.parseFloat(valueQuantity1.getString("value"))));
                            lastReading = valueQuantity.getString("value") + " " + valueQuantity.getString("unit");
                            if (valueQuantity1 != null) {
                                lastReading = valueQuantity.getString("value") + "/" + valueQuantity1.getString("value") + " " + valueQuantity.getString("unit");
                            }

                        }

                    }
                }

            }
            setDataBarGroup(values, values1, xVals);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setDataBarGroup(ArrayList<BarEntry> yVals1, ArrayList<BarEntry> yVals2, ArrayList<String> xVals) {
        BarDataSet set1, set2;
        set1 = new BarDataSet(yVals1, "SYS");
        set1.setColor(getResources().getColor(R.color.yellow_200));
        set2 = new BarDataSet(yVals2, "DYS");
        set2.setColor(getResources().getColor(R.color.blue_grey_700));

        BarData data = new BarData(set1, set2);
        data.setValueFormatter(new LargeValueFormatter());
        chart.setData(data);
        chart.getBarData().setBarWidth(barWidth);
        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * yVals1.size());
        chart.groupBars(0, groupSpace, barSpace);
        chart.getData().setHighlightEnabled(false);
        chart.invalidate();

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setYOffset(20f);
        l.setXOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        //X-axis
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMaximum(6);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
//Y-axis
        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);

//        data.groupBars(0, groupSpace, barSpace);
//
//        chart.setData(data);
//        chart.getBarData().setBarWidth(barWidth);
//        chart.getXAxis().setAxisMinimum(0);
//        chart.getXAxis().setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * yVals1.size());
//        chart.groupBars(0, groupSpace, barSpace);
//
//        chart.getData().setHighlightEnabled(false);
//
//
//        Legend l = chart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(true);
//        l.setYOffset(20f);
//        l.setXOffset(0f);
//        l.setYEntrySpace(0f);
//        l.setTextSize(8f);
//
//        //X-axis
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setGranularity(1f);
//        xAxis.setGranularityEnabled(true);
//        xAxis.setCenterAxisLabels(false);
//        xAxis.setDrawGridLines(false);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisMinimum(0);
//        xAxis.setAxisMaximum(0 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * yVals1.size());
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
//
////Y-axis
//        chart.getAxisRight().setEnabled(false);
//        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setValueFormatter(new LargeValueFormatter());
//        leftAxis.setDrawGridLines(true);
//        leftAxis.setSpaceTop(35f);
//        leftAxis.setAxisMinimum(0f);
//        chart.getLegend().setEnabled(true);


        chart.animateXY(1000, 1000);
        chart.invalidate();


        txtPatientId.setText(name);
        txtPatientName.setText(lastReading);
        txtPatientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //addEntry();
            }
        });
    }

    private void parseSimpleBarData(JSONObject response) {
        ArrayList<BarEntry> values = new ArrayList<>();
        ArrayList<String> xAxis = new ArrayList<>();
        try {
            if (response.has("entry")) {
                JSONArray jsonArray = response.getJSONArray("entry");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject eventObj = jsonArray.getJSONObject(i);
                        JSONObject resource = eventObj.getJSONObject("resource");
                        //get status
                        String status = resource.getString("status");
                        if (resource.has("code")) {
                            // glucose
                            JSONObject interpretation;
                            JSONArray codingArr1;
                            String rangeDisplayCode = "L";
                            JSONObject codeObj = resource.getJSONObject("code");
                            JSONArray codingArr = codeObj.getJSONArray("coding");
                            String nameObj = codingArr.getJSONObject(0).getString("display");
                            String code = codingArr.getJSONObject(0).getString("code");
                            JSONObject valueQuantity = null;
                            if (resource.has("valueQuantity")) {
                                valueQuantity = resource.getJSONObject("valueQuantity");
                            } else {
                                if (resource.has("component")) {
                                    JSONObject compomemt = resource.getJSONArray("component").getJSONObject(0);
                                    valueQuantity = compomemt.getJSONObject("valueQuantity");
                                }
                            }
                            if (resource.has("issued")) {
                                String issuedDate = resource.getString("issued");
                                xAxis.add(issuedDate.substring(0, 10));
                            }

                            if (resource.has("effectiveDateTime")) {
                                String issuedDate = resource.getString("effectiveDateTime");
                                xAxis.add(issuedDate);
                            }

                            String val = valueQuantity.getString("value") + " " + valueQuantity.getString("unit");
                            lastReading = val;
                            values.add(new BarEntry(i, Float.parseFloat(valueQuantity.getString("value"))));
                        }

                    }
                }
            }
            setData(values, xAxis);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData(ArrayList<BarEntry> values, ArrayList<String> xAxisLabels) {

        BarDataSet set1;

        set1 = new BarDataSet(values, name);

        set1.setDrawIcons(false);
        //  set1.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        data.setBarWidth(0.25f);
        chart.setData(data);
        chart.getData().notifyDataChanged();
        chart.notifyDataSetChanged();
        XAxis xAxis = chart.getXAxis();
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(xAxisLabels);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(formatter);
        txtPatientId.setText(name);
        txtPatientName.setText(lastReading);
        chart.getLegend().setEnabled(false);   // Hide the legend

        chart.animateXY(1000, 1000);
        chart.invalidate();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


    private void addEntry() {

        LineData data = chartLine.getData();

        if (data == null) {
            data = new LineData();
            chartLine.setData(data);
        }

        ILineDataSet set = data.getDataSetByIndex(0);
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet();
            data.addDataSet(set);
        }

        // choose a random dataSet
        int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());
        ILineDataSet randomSet = data.getDataSetByIndex(randomDataSetIndex);
        float value = (float) (Math.random() * 50) + 50f * (randomDataSetIndex + 1);

        data.addEntry(new Entry(randomSet.getEntryCount(), value), randomDataSetIndex);
        data.notifyDataChanged();

        // let the chart know it's data has changed
        chartLine.notifyDataSetChanged();

        chartLine.setVisibleXRangeMaximum(6);
        //chart.setVisibleYRangeMaximum(15, AxisDependency.LEFT);
//
//            // this automatically refreshes the chart (calls invalidate())
        chartLine.moveViewTo(data.getEntryCount() - 7, 50f, YAxis.AxisDependency.LEFT);

    }

    private void removeLastEntry() {

        LineData data = chartLine.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);

            if (set != null) {

                Entry e = set.getEntryForXValue(set.getEntryCount() - 1, Float.NaN);

                data.removeEntry(e, 0);
                // or remove by index
                // mData.removeEntryByXValue(xIndex, dataSetIndex);
                data.notifyDataChanged();
                chartLine.notifyDataSetChanged();
                chartLine.invalidate();
            }
        }
    }

    private void addDataSet() {

        LineData data = chartLine.getData();

        if (data == null) {
            chartLine.setData(new LineData());
        } else {
            int count = (data.getDataSetCount() + 1);
            int amount = data.getDataSetByIndex(0).getEntryCount();

            ArrayList<Entry> values = new ArrayList<>();

            for (int i = 0; i < amount; i++) {
                values.add(new Entry(i, (float) (Math.random() * 50f) + 50f * count));
            }

            LineDataSet set = new LineDataSet(values, "DataSet " + count);
            set.setLineWidth(2.5f);
            set.setCircleRadius(4.5f);

            int color = colors[count % colors.length];

            set.setColor(color);
            set.setCircleColor(color);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            data.addDataSet(set);
            data.notifyDataChanged();
            chartLine.notifyDataSetChanged();
            chartLine.invalidate();
        }
    }

    private void removeDataSet() {

        LineData data = chartLine.getData();

        if (data != null) {

            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            chartLine.notifyDataSetChanged();
            chartLine.invalidate();
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet 1");
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }


}

