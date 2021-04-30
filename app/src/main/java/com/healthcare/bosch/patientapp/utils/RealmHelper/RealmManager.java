package com.healthcare.bosch.patientapp.utils.RealmHelper;

import com.healthcare.bosch.patientapp.Application.CustomApplication;
import com.healthcare.bosch.patientapp.utils.model.MedicationRealmModel;

import java.util.ArrayList;

import io.realm.RealmResults;

public class RealmManager {
    private static final RealmManager ourInstance = new RealmManager();

    public static RealmManager getInstance() {
        return ourInstance;
    }

    private RealmManager() {

    }

    public void beginTrans() {
        CustomApplication.getRealmContext().beginTransaction();
    }

    public void commitTrans() {
        CustomApplication.getRealmContext().commitTransaction();
    }

    public void clearRealmData() {
        beginTrans();
        CustomApplication.getRealmContext().deleteAll();
        commitTrans();
    }

    public ArrayList<MedicationRealmModel> getMedicationReqByDate(int id, String searchDate) {
        ArrayList<MedicationRealmModel> medicationRealmModelList = new ArrayList<>();
        RealmResults<MedicationRealmModel> results = CustomApplication.getRealmContext().where(MedicationRealmModel.class).equalTo("pendingIntentId", id).and().equalTo("startDate", searchDate).findAll();
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                medicationRealmModelList.add(results.get(i));
            }
        }
        return medicationRealmModelList;
    }

    public MedicationRealmModel getMedicationReqById(int id) {
        MedicationRealmModel results = CustomApplication.getRealmContext().where(MedicationRealmModel.class).equalTo("id", id).findFirst();
        return results;
    }
}
