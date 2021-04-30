package com.healthcare.bosch.patientapp.fhirHelper;

import com.healthcare.bosch.patientapp.Application.CustomApplication;
//
//import org.hl7.fhir.dstu3.model.Bundle;
//import org.hl7.fhir.dstu3.model.Observation;

import java.util.List;

//import ca.uhn.fhir.util.BundleUtil;

public class ObservationFhirHelper {

    public ObservationFhirHelper() {

    }

//    //http://hapi.fhir.org/baseDstu3/Observation?subject=Patient/2645014
//    public List<Observation> getObservations(String patientId) {
//        // Invoke the client
//        Bundle bundle = CustomApplication.getInstance().getIGenericClient().search().forResource(Observation.class)
//                .where( Observation.PATIENT.hasId(patientId))
//                .returnBundle(Bundle.class)
//                .execute();
//        return BundleUtil.toListOfResourcesOfType(CustomApplication.getInstance().getFHIRContext(), bundle, Observation.class);
//    }

}