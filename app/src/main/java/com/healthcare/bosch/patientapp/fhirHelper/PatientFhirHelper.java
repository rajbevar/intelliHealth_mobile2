package com.healthcare.bosch.patientapp.fhirHelper;

import com.healthcare.bosch.patientapp.Application.CustomApplication;
//
//import org.hl7.fhir.dstu3.model.Bundle;
//import org.hl7.fhir.dstu3.model.Patient;
//
//import java.util.List;
//
//import ca.uhn.fhir.util.BundleUtil;

public class PatientFhirHelper {


    public PatientFhirHelper() {

    }
//
//    public List<Patient> getPatients() {
//        // Invoke the client
//        Bundle bundle = CustomApplication.getInstance().getIGenericClient().search().forResource(Patient.class)
//                .where(Patient.FAMILY.matches().value("Ichha"))
//                .returnBundle(Bundle.class)
//                .execute();
//        return BundleUtil.toListOfResourcesOfType(CustomApplication.getInstance().getFHIRContext(), bundle, Patient.class);
//    }

}