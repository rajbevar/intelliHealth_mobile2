package com.healthcare.bosch.patientapp.utils.Interface;

import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class UtilitiesInterfaces {

    /**
     * To Receive Alert dialog click events
     */
    public interface AlertCallback {
        public void onOptionClick(DialogInterface dialog, int buttonIndex);
    }

    // Interface
    public interface PermissionCallback {
        void receivePermissionStatus(Boolean isGranted);
    }

    // Interface
    public interface SpinnerCallback {
        void receiveSelectedItem(HashMap<String, String> selectedItem);
    }

    // To receive Multiple selection of items from spinner
    public interface MultiSelectSpinnerCallback {
        void receiveMultiSelectedItems(ArrayList<HashMap<String, String>> onlySelectedItems, ArrayList<HashMap<String, String>> allItems);
    }


}
