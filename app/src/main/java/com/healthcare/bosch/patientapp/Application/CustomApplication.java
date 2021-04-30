package com.healthcare.bosch.patientapp.Application;

import android.app.Application;
import android.content.res.Configuration;

import com.healthcare.bosch.patientapp.utils.Components.Config;
import com.healthcare.bosch.patientapp.utils.RealmHelper.IntelliHealthRealmMigration;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;

//import ca.uhn.fhir.context.FhirContext;
//import ca.uhn.fhir.rest.client.api.IGenericClient;


public class CustomApplication extends Application {

    private static final String TAG = CustomApplication.class.getSimpleName();
    private static CustomApplication appInstance = null;

    public static CustomApplication getInstance() {
        return appInstance;
    }

//
//    private IGenericClient client;
//    private FhirContext ctx;


    static RealmConfiguration config;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        // Network Lib
        initNetworking();
        // custom font
        initCustomFont();// realm
        //  initRealm();
        initFHIR();
        JodaTimeAndroid.init(this);

// realm
        initRealm();

    }


    //For Handling the Realm
    public static Realm realm;

    public static Realm getRealmContext() {
        return realm;
    }

    public static void setRealmInstance() {
        // Get a Realm instance for this thread
        if (config != null) {
            CustomApplication.realm = Realm.getInstance(config);
        }
    }


    private void initRealm() {
        // Initialize Realm (just once per application)
        Realm.init(this);
        config = new RealmConfiguration.Builder()
                .name("bosch_doctor_intellihealth.realm")
                .schemaVersion(Config.REALM_SCHEMA_VERSION)
                .migration(new IntelliHealthRealmMigration())
                .build();
        setRealmInstance();
    }


    private void initFHIR() {
//        ctx = FhirContext.forDstu3();
//        client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseDstu3");

    }

//    public IGenericClient getIGenericClient() {
//        return client;
//    }
//
//    public FhirContext getFHIRContext() {
//        return ctx;
//    }


    private void initNetworking() {

    }

    private void initCustomFont() {
     /*  CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(Config.FONT_PATH)
                .setFontAttrId(R.attr.fontPath)
                .build()); */
    }

   /* @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }*/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }


}