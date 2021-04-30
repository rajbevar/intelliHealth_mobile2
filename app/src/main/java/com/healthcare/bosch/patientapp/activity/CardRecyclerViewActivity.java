package com.healthcare.bosch.patientapp.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcare.bosch.patientapp.Application.CustomApplication;
import com.healthcare.bosch.patientapp.R;
import com.healthcare.bosch.patientapp.utils.Components.DialogMedication;
import com.healthcare.bosch.patientapp.utils.Interface.DialogResponse;
import com.healthcare.bosch.patientapp.utils.RealmHelper.RealmManager;
import com.healthcare.bosch.patientapp.utils.adapter.CardAdapter;
import com.healthcare.bosch.patientapp.utils.model.MedicationRealmModel;
import com.healthcare.bosch.patientapp.utils.model.Patient;

import java.util.ArrayList;

//
//import org.hl7.fhir.dstu3.model.Observation;
//
//import ca.uhn.fhir.context.FhirContext;
//import ca.uhn.fhir.rest.api.MethodOutcome;
//import ca.uhn.fhir.rest.client.api.IGenericClient;
//import ca.uhn.fhir.util.BundleUtil;


public class CardRecyclerViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private ArrayList<Patient> patientArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getBundleData();
        initView();

    }

    private void getBundleData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int patientId = (int) extras.get("patientId");
            int id = (int) extras.get("id");
            // show popup view
            RealmManager.getInstance().beginTrans();
            MedicationRealmModel model = RealmManager.getInstance().getMedicationReqById(id);
            RealmManager.getInstance().commitTrans();

            if (model != null) {
               DialogMedication dialogMedication = new DialogMedication(this, "Time to action", patientId, model, new DialogResponse() {
                    @Override
                    public void okayPressed() {

                        MedicationRealmModel medicationRealmModel = new MedicationRealmModel();
                        medicationRealmModel.setFrequency(model.getFrequency());
                        medicationRealmModel.setMedicineDesc(model.getMedicineDesc());
                        medicationRealmModel.setMedicineName(model.getMedicineName());
                        medicationRealmModel.setStatus("taken");
                        medicationRealmModel.setWhen(model.getWhen());
                        medicationRealmModel.setPendingIntentId(model.getPendingIntentId());
                        medicationRealmModel.setStartDate(model.getStartDate());
                        medicationRealmModel.setEndDate(model.getEndDate());
                        medicationRealmModel.setId(model.getId());

                        RealmManager.getInstance().beginTrans();
                        CustomApplication.getRealmContext().copyToRealmOrUpdate(medicationRealmModel);
                        RealmManager.getInstance().commitTrans();

                    }

                    @Override
                    public void dismissPressed() {
                        MedicationRealmModel medicationRealmModel = new MedicationRealmModel();
                        medicationRealmModel.setFrequency(model.getFrequency());
                        medicationRealmModel.setMedicineDesc(model.getMedicineDesc());
                        medicationRealmModel.setMedicineName(model.getMedicineName());
                        medicationRealmModel.setStatus("not taken");
                        medicationRealmModel.setWhen(model.getWhen());
                        medicationRealmModel.setPendingIntentId(model.getPendingIntentId());
                        medicationRealmModel.setStartDate(model.getStartDate());
                        medicationRealmModel.setEndDate(model.getEndDate());
                        medicationRealmModel.setId(model.getId());

                        RealmManager.getInstance().beginTrans();
                        CustomApplication.getRealmContext().copyToRealmOrUpdate(medicationRealmModel);
                        RealmManager.getInstance().commitTrans();

                    }
                });
                dialogMedication.show();
             /*   MyAlertDialogFragment dialog = new MyAlertDialogFragment(this, "Time to action", patientId, model, new DialogResponse() {
                    @Override
                    public void okayPressed() {

                        MedicationRealmModel medicationRealmModel = new MedicationRealmModel();
                        medicationRealmModel.setFrequency(model.getFrequency());
                        medicationRealmModel.setMedicineDesc(model.getMedicineDesc());
                        medicationRealmModel.setMedicineName(model.getMedicineName());
                        medicationRealmModel.setStatus("taken");
                        medicationRealmModel.setWhen(model.getWhen());
                        medicationRealmModel.setPendingIntentId(model.getPendingIntentId());
                        medicationRealmModel.setStartDate(model.getStartDate());
                        medicationRealmModel.setEndDate(model.getEndDate());
                        medicationRealmModel.setId(model.getId());

                        RealmManager.getInstance().beginTrans();
                        CustomApplication.getRealmContext().copyToRealmOrUpdate(medicationRealmModel);
                        RealmManager.getInstance().commitTrans();

                    }

                    @Override
                    public void dismissPressed() {

                    }
                });
                dialog.show(getSupportFragmentManager(), "");*/
            }
        }
    }


    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientArrayList = new ArrayList<>();
        adapter = new CardAdapter(this, patientArrayList);
        recyclerView.setAdapter(adapter);
        //  recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        createListData();


    }


    private void createListData() {
        Patient patient = new Patient(2645014, "Oliver Emma", "543545345656", "male", "Diabetes", true);
        patientArrayList.add(patient);

        patient = new Patient(2645015, "David Smith", "543545345656", "male", "Obesity | Anxiety", false);
        patientArrayList.add(patient);

        patient = new Patient(2645016, "Maria Martine", "543545345656", "female", "Respiratory problems", true);
        patientArrayList.add(patient);

        patient = new Patient(2645017, "James Johnson", "54354534311156", "male", "Back pain | Allergic rhinitis", true);
        patientArrayList.add(patient);

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
