package com.healthcare.bosch.patientapp.utils.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.healthcare.bosch.patientapp.Application.CustomApplication;
import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.activity.MainActivity;
import com.healthcare.bosch.patientapp.activity.SummaryActivity;
import com.healthcare.bosch.patientapp.utils.Components.Utilities;
import com.healthcare.bosch.patientapp.utils.model.Patient;
import com.healthcare.bosch.patientapp.utils.model.Planet;

//import org.hl7.fhir.dstu3.model.MedicationRequest;
//import org.hl7.fhir.dstu3.model.Observation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import ca.uhn.fhir.util.BundleUtil;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.PlanetHolder> {

    private Context context;
    private ArrayList<Patient> patients;

    public CardAdapter(Context context, ArrayList<Patient> patients) {
        this.context = context;
        this.patients = patients;
    }

    @NonNull
    @Override
    public PlanetHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new PlanetHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanetHolder holder, int position) {
        Patient patient = patients.get(position);
        holder.setDetails(patient, position);
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    class PlanetHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtPatientId, txtMobile, txtGender, txtMedical;
        ImageView imgPatientStatus;
        CardView cardView;

        PlanetHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtPatientName);
            txtPatientId = itemView.findViewById(R.id.txtPatientId);
            txtMobile = itemView.findViewById(R.id.txtPatientMobile);
            txtGender = itemView.findViewById(R.id.txtGender);
            txtMedical = itemView.findViewById(R.id.txtMedicalRecord);
            imgPatientStatus = itemView.findViewById(R.id.imgPatientStatus);
            cardView = itemView.findViewById(R.id.cardView);


        }

        void setDetails(final Patient patient, final int position) {
            txtName.setText(patient.getPatientName());
            txtPatientId.setText("ID: " + String.valueOf(patient.getId()));
            txtMobile.setText("Phone: " + patient.getPhoneNumber());
            txtGender.setText("Gender: " + patient.getGender());
            txtMedical.setText(patient.getMedicalCondition());
            if (patient.isStatus()) {
                imgPatientStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.active));
            } else {
                imgPatientStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.inactive));
            }

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == 1) {
                        Utilities.showCookieBar((Activity) context, "User Status is Inactive", "Inactive", false).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }).start();

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        bundle.putString("patientId", String.valueOf(patient.getId()));
                        bundle.putString("patientName", patient.getPatientName());
                        bundle.putString("patientMedicalRecord", patient.getMedicalCondition());
                        Utilities.showActivityNotFinished((Activity) context, SummaryActivity.class, bundle);
                    }
                }
            });
        }
    }
}