package com.healthcare.bosch.patientapp.utils.Components;

public interface Config {
    String FONT_PATH = "fonts/Montserrat-Regular.ttf";
    String APP_ID = "com.innov.doctor";
    String AUTH = "Authorization";
    String CONTENT_TYPE = "Content-Type";
    String ACCEPT = "Accept";
    String APP_CLUSTER = "ap2";
    String SUB_SPEECH_KEY = "SUB_SPEECH_KEY";
    //String SUB_SPEAKER_KEY = "d07dae75d4494f7da2e918a614a61a04";
    String SUB_SPEAKER_KEY = "27f90eb20a714477b8282bdf5fb1adab";
    String ENV = "PROD"; // DEV,PROD

    String AppName = "innovation";
    String STARTS_AT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String DATE_DD_MM_YYYY = "dd/MM/yyyy";
    String TIME_HH_MM_A = "hh:mm a";

    String EMAIL = "healthcare@bosch.com";
    String PASSWORD = "Bosch@123";

    int REALM_SCHEMA_VERSION = 1;

    // frequency time for alaram
    int MORN = 8;
    int AFTN = 12;
    int EVE = 17;
    int NIGHT = 20;

    // NONE=0,
    // TAKEN=1,
    // NOTTAKEN = 2,
    // IGNORED = 3,


    String patientAPI = "http://intellihealthapi.azurewebsites.net/api/Patient/%s";
    String patientMedicationAPI = "http://intellihealthapi.azurewebsites.net/api/Medications?externalId=%s";

}

