package com.healthcare.bosch.patientapp.libs.calendardayview.decoration;

import android.graphics.Rect;

import com.healthcare.bosch.patientapp.libs.calendardayview.DayView;
import com.healthcare.bosch.patientapp.libs.calendardayview.EventView;
import com.healthcare.bosch.patientapp.libs.calendardayview.PopupView;
import com.healthcare.bosch.patientapp.libs.calendardayview.data.IEvent;
import com.healthcare.bosch.patientapp.libs.calendardayview.data.IPopup;

/**
 * Created by FRAMGIA\pham.van.khac on 22/07/2016.
 */
public interface CdvDecoration {

    EventView getEventView(IEvent event, Rect eventBound, int hourHeight, int seperateHeight);

    PopupView getPopupView(IPopup popup, Rect eventBound, int hourHeight, int seperateHeight);

    DayView getDayView(int hour);
}
