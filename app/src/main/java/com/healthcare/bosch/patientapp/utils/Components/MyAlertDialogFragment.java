package com.healthcare.bosch.patientapp.utils.Components;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.utils.Interface.DialogResponse;
import com.healthcare.bosch.patientapp.utils.model.MedicationRealmModel;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.ArrayList;
import java.util.List;

public class MyAlertDialogFragment extends DialogFragment {
    private static final String TAG = "CustomDialogF";
    Context mainContext;
    View view;
    String customTitle;
    String reason = "";
    int customPatientId;
    MedicationRealmModel customMedicationDetails;
    DialogPlus customDialog;
    private TextView txtMedicineName, txtMedicineDate, txtMedicineTime;
    DialogResponse customDialogResponse;
    SingleSelectToggleGroup flowLayout;
    LinearLayout lnrFlow;
    List<String> categories;
    Spinner reasonSpinner;
    View customView;
    Button positiveButton, negativeButton;
    Button false_button, true_button;

    public MyAlertDialogFragment(Context context, String title, int patientId, MedicationRealmModel medicationDetails, DialogResponse dialogResponse) {
        // Required empty public constructor
        mainContext = context;
        customTitle = title;
        customMedicationDetails = medicationDetails;
        customPatientId = patientId;
        customDialogResponse = dialogResponse;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (negativeButton != null) {

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reason = reasonSpinner.getSelectedItem().toString();
                    if (reason.equalsIgnoreCase("-- Choose Reason --")) {
                        lnrFlow.setVisibility(View.VISIBLE);
                        //  Utilities.showCookieBar((Activity) mainContext, "Kindly choose reason", "Choose reason", false).show();
                        //  Toast.makeText(mainContext,"Kindly select reason",Toast.LENGTH_SHORT).show();
                    } else {
                        customDialogResponse.dismissPressed();
                        dismiss();
                    }
                }
            });
        }
        if (positiveButton != null) {
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialogResponse.okayPressed();
                    dismiss();
                }
            });
        }
    }

    public View getUI() {
        return customView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        customView = inflater.inflate(R.layout.activity_notification_intent, null);
        builder.setView(customView);
        setCancelable(false);
        builder.setPositiveButton("Taken", null);
        builder.setNegativeButton("Not taken", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {                    //
                positiveButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_POSITIVE);

                negativeButton = ((AlertDialog) dialog)
                        .getButton(AlertDialog.BUTTON_NEGATIVE);
            }
        });


        initialize();


        return alertDialog;
    }


    private void initialize() {
        txtMedicineName = getUI().findViewById(R.id.txtMedicineName);
        txtMedicineDate = getUI().findViewById(R.id.txtMedicineDate);
        txtMedicineTime = getUI().findViewById(R.id.txtMedicineTime);
        true_button = getUI().findViewById(R.id.true_button);
        false_button = getUI().findViewById(R.id.false_button);
        flowLayout = getUI().findViewById(R.id.flow2);
        reasonSpinner = getUI().findViewById(R.id.spinner);
        lnrFlow = getUI().findViewById(R.id.flowLayout);
        lnrFlow.setVisibility(View.GONE);
        txtMedicineName.setText(customMedicationDetails.getMedicineName());
        txtMedicineDate.setText(customMedicationDetails.getStartDate());
        txtMedicineTime.setText(getWhen(customMedicationDetails.getWhen()));

        categories = new ArrayList<String>();
        categories.add("-- Choose Reason --");
        categories.add("Forget");
        categories.add("Could not take");
        categories.add("Did not feel like it");
        categories.add("Feel uncomfortable");
        categories.add("Ran out of medication");
        categories.add("Do not need");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mainContext, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        reasonSpinner.setAdapter(dataAdapter);

        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reason = categories.get(position);
                dismiss();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public String getWhen(String usagewhen) {
        String when = "8 AM";

        if (usagewhen.equalsIgnoreCase("MORN")) {
            return Config.MORN + " AM";
        } else if (usagewhen.equalsIgnoreCase("AFTN")) {
            return "12 PM";
        } else if (usagewhen.equalsIgnoreCase("EVE")) {
            return " 5 PM";
        } else if (usagewhen.equalsIgnoreCase("NIGHT")) {
            return "8 PM";
        }

        return when;
    }


}
