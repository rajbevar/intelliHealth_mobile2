package com.healthcare.bosch.patientapp.utils.ChartHelper;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CustomChartMarkerView extends MarkerView {

    public static final String MARKER_TYPE_TIMESTAMP = "timestamp";
    public static final String MARKER_TYPE_MINUTES = "minutes";
    public static final String MARKER_TYPE_SECONDS = "seconds";
    public static final String MARKER_TYPE_HOURS = "hours";
    public static final String MARKER_TYPE_WEEK_MONTH_YEAR = "week_month_year";
    public static final String MARKER_TYPE_MINUTES_FROM_EPOCH = "minutes_from_epoch";

    private TextView topLabel;
    private TextView bottomLabel;
    private String topUnit;
    private String bottomUnit;
    private boolean useAxisXFormatter = false;
    private boolean useAxisYFormatter = false;
    private boolean hasBringToFront = false;
    private long extraTimestampOffset = 0; //Hack for meditations activity charts
    private String extraTopUnitTextInfo = ""; //Hack workout details charts
    private String extraBottomUnitTextInfo = ""; //Hack workout details charts
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private final SimpleDateFormat DATE_FORMAT_WEEK_MONTH_YEAR = new SimpleDateFormat("MMM d", Locale.getDefault());

    public CustomChartMarkerView(Context context, @LayoutRes int layoutResource,
                                 @DrawableRes int backgroundRes, @ColorRes int textColor,
                                 String topUnit, String bottomUnit, Chart chart, boolean useAxisXFormatter,
                                 boolean useAxisYFormatter, boolean hasBringToFront) {

        super(context, layoutResource);

        this.useAxisXFormatter = useAxisXFormatter;
        this.useAxisYFormatter = useAxisYFormatter;
        this.hasBringToFront = hasBringToFront;

        this.topUnit = topUnit;
        this.bottomUnit = bottomUnit;

        setChartView(chart);
        setBackgroundResource(backgroundRes);

        //   topLabel = findViewById(R.id.chart_marker_view_top_label);
        //  bottomLabel = findViewById(R.id.chart_marker_view_bottom_label);

        topLabel.setTextColor(ContextCompat.getColor(context, textColor));
        bottomLabel.setTextColor(ContextCompat.getColor(context, textColor));
    }

    public CustomChartMarkerView(Context context, @LayoutRes int layoutResource,
                                 @DrawableRes int backgroundRes, @ColorRes int textColor,
                                 String topUnit, String bottomUnit, Chart chart) {
        this(context, layoutResource, backgroundRes, textColor, topUnit, bottomUnit, chart, false, false, false);
    }

    public CustomChartMarkerView(Context context, @LayoutRes int layoutResource,
                                 @DrawableRes int backgroundRes, @ColorRes int textColor, Chart chart, boolean useAxisXFormatter,
                                 boolean useAxisYFormatter, boolean hasBringToFront) {
        this(context, layoutResource, backgroundRes, textColor, "", "", chart, useAxisXFormatter, useAxisYFormatter, hasBringToFront);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        switch (topUnit.toLowerCase()) {
            case MARKER_TYPE_WEEK_MONTH_YEAR:
                //  topLabel.setText(getFinalTextLeftUnit(DATE_FORMAT_WEEK_MONTH_YEAR.format(TimeUtils.fromDaysBetweenEpoch((int) e.getX()).toDate())));
                break;
            case MARKER_TYPE_TIMESTAMP:
                //  topLabel.setText(getFinalTextLeftUnit(DATE_FORMAT.format(new DateTime((int) e.getX() + extraTimestampOffset).toDate())));
                break;
            case MARKER_TYPE_MINUTES:
                // topLabel.setText(getFinalTextLeftUnit(TimeUtils.formatSeconds((int) (e.getX() * 60))));
                break;
            case MARKER_TYPE_SECONDS:
                //  topLabel.setText(getFinalTextLeftUnit(TimeUtils.formatSeconds((int) (e.getX()))));
                break;
            case MARKER_TYPE_HOURS:
                //  topLabel.setText(getFinalTextLeftUnit(TimeUtils.formatSeconds((int) (e.getX() * 60 * 60))));
                break;
            case MARKER_TYPE_MINUTES_FROM_EPOCH:
                //   topLabel.setText(getFinalTextLeftUnit(DATE_FORMAT.format(TimeUtils.fromMinutesBetweenEpoch((int) e.getX()).toDate())));
                break;
            default:
                if (useAxisXFormatter && getChartView().getXAxis().getValueFormatter() != null) {
                    String label = getChartView().getXAxis().getValueFormatter().getFormattedValue(e.getX(), getChartView().getXAxis()).replaceAll("\n", " ");
                    topLabel.setText(getFinalTextLeftUnit(label));
                } else {
                    //  topLabel.setText(getFinalTextLeftUnit(Constants.DECIMAL_FORMAT_1.format(e.getX())));
                }
                break;
        }

        switch (bottomUnit.toLowerCase()) {
            case MARKER_TYPE_WEEK_MONTH_YEAR:
                // bottomLabel.setText(getFinalTextRightUnit(DATE_FORMAT_WEEK_MONTH_YEAR.format(TimeUtils.fromDaysBetweenEpoch((int) e.getY()).toDate())));
                break;
            case MARKER_TYPE_TIMESTAMP:
                //  bottomLabel.setText(getFinalTextRightUnit(DATE_FORMAT.format(new DateTime((int) e.getY() + extraTimestampOffset).toDate())));
                break;
            case MARKER_TYPE_MINUTES:
                //  bottomLabel.setText(getFinalTextRightUnit(TimeUtils.formatSeconds((int) (e.getY() * 60))));
                break;
            case MARKER_TYPE_SECONDS:
                //  bottomLabel.setText(getFinalTextRightUnit(TimeUtils.formatSeconds((int) (e.getY()))));
                break;
            case MARKER_TYPE_HOURS:
                //   bottomLabel.setText(getFinalTextRightUnit(TimeUtils.formatSeconds((int) (e.getY() * 60 * 60))));
                break;
            case MARKER_TYPE_MINUTES_FROM_EPOCH:
                //bottomLabel.setText(getFinalTextRightUnit(DATE_FORMAT.format(TimeUtils.fromMinutesBetweenEpoch((int) e.getY()).toDate())));
            default:
                if (useAxisYFormatter) {
                    if (getChartView() instanceof BarChart && ((BarChart) getChartView()).getAxisLeft().getValueFormatter() != null) {
                        AxisBase axis = ((BarChart) getChartView()).getAxisLeft();
                        String label = axis.getValueFormatter().getFormattedValue(e.getY(), axis);
                        bottomLabel.setText(getFinalTextRightUnit(label));
                    } else {
                        //bottomLabel.setText(getFinalTextRightUnit(Constants.DECIMAL_FORMAT_1.format(e.getY())));
                    }
                } else {
                    // bottomLabel.setText(getFinalTextRightUnit(Constants.DECIMAL_FORMAT_1.format(e.getY())));
                }
                break;
        }

        super.refreshContent(e, highlight);

        /*if (getChartView() instanceof BarChart) {
            setOffset(-getWidth() / 2, -getHeight() + getContext().getResources().getDimensionPixelSize(R.dimen.extra_offset_y_marker_bar));
        } else {
            setOffset(-getWidth() / 2, -highlight.getYPx());
        }*/

        setOffset(-getWidth() / 2, -highlight.getYPx());

        if (hasBringToFront) {
            getChartView().bringToFront();
        }
    }

    private String getFinalTextLeftUnit(String text) {
        StringBuilder builder = new StringBuilder(text.trim());


        return builder.toString();
    }

    private boolean isCustomMarkerUnit(String unit) {
        return MARKER_TYPE_TIMESTAMP.equals(unit) ||
                MARKER_TYPE_MINUTES.equals(unit) ||
                MARKER_TYPE_SECONDS.equals(unit) ||
                MARKER_TYPE_HOURS.equals(unit) ||
                MARKER_TYPE_WEEK_MONTH_YEAR.equals(unit) ||
                MARKER_TYPE_MINUTES_FROM_EPOCH.equals(unit);
    }

    private String getFinalTextRightUnit(String text) {
        StringBuilder builder = new StringBuilder(text.trim());


        return builder.toString();
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        super.draw(canvas, posX, posY);
        getOffsetForDrawingAtPoint(posX, posY);
    }
}