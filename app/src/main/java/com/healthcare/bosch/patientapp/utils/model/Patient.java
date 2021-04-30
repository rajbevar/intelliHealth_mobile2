package com.healthcare.bosch.patientapp.utils.model;

public class Patient {

    private String patientName;
    private String phoneNumber;
    private String gender;
    private String medicalCondition;
    private boolean status;
    private int id;


    public Patient(int id, String patientName, String phoneNumber, String gender, String medicalCondition, boolean status) {
        this.patientName = patientName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.medicalCondition = medicalCondition;
        this.status = status;
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}