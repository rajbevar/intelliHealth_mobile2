package com.healthcare.bosch.patientapp.utils.Components;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.utils.Interface.DialogResponse;
import com.healthcare.bosch.patientapp.utils.model.MedicationRealmModel;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DialogMedication {


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
    Button false_button, true_button;

    // MARK : Lifecycle
    public DialogMedication(Context context, String title, int patientId, MedicationRealmModel medicationDetails, DialogResponse dialogResponse) {

        mainContext = context;
        customTitle = title;
        customMedicationDetails = medicationDetails;
        customPatientId = patientId;
        customDialogResponse = dialogResponse;

        view = LayoutInflater.from(context).inflate(
                R.layout.activity_notification_intent, null);
        initialize();
    }

    public void show() {
        showCurrentViewAsAlert(customDialogResponse);
    }

    public void dismiss() {
        customDialog.dismiss();
    }

    public View getUI() {
        return view;
    }

    // MARK : Instance Methods
    private void initialize() {
        txtMedicineName = getUI().findViewById(R.id.txtMedicineName);
        txtMedicineDate = getUI().findViewById(R.id.txtMedicineDate);
        txtMedicineTime = getUI().findViewById(R.id.txtMedicineTime);
        flowLayout = getUI().findViewById(R.id.flow2);
        reasonSpinner = getUI().findViewById(R.id.spinner);
        lnrFlow = getUI().findViewById(R.id.flowLayout);
        lnrFlow.setVisibility(View.GONE);
        txtMedicineName.setText(customMedicationDetails.getMedicineName());
        txtMedicineDate.setText(customMedicationDetails.getStartDate());
        txtMedicineTime.setText(getWhen(customMedicationDetails.getWhen()));
        true_button = getUI().findViewById(R.id.true_button);
        false_button = getUI().findViewById(R.id.false_button);

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
    }


    private void showCurrentViewAsAlert(final DialogResponse customDialogResponse) {

        customDialog = DialogPlus.newDialog(mainContext)
                .setContentHolder(new ViewHolder(getUI()))
                .setGravity(Gravity.CENTER)
                .setMargin(20, 20, 20, 20)
                .setPadding(20, 20, 20, 20).setCancelable(false)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        Log.d("TAG", "'");

                    }
                })
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(DialogPlus dialog, View v) {
                        // handle number button click
                        if (v.getTag() != null && "number_button".equals(v.getTag())) {

                            return;
                        }
                        switch (v.getId()) {
                            case R.id.true_button: {
                                customDialogResponse.okayPressed();
                                customDialog.dismiss();
                            }
                            break;
                            case R.id.false_button: {
                                reason = reasonSpinner.getSelectedItem().toString();
                                if (reason.equalsIgnoreCase("-- Choose Reason --")) {
                                    lnrFlow.setVisibility(View.VISIBLE);
                                    true_button.setVisibility(View.GONE);
                                    false_button.setText("Confirm");
                                    Utilities.showCookieBar((Activity) mainContext, "Kindly choose reason", "Choose reason", false).show();
                                } else {
                                    customDialogResponse.dismissPressed();
                                    customDialog.dismiss();

                                }
                            }


                        }
                    }

                }).

                        create();

        customDialog.show();

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

