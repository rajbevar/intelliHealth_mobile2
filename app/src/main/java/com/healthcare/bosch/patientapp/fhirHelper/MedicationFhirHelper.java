package com.healthcare.bosch.patientapp.fhirHelper;

import com.healthcare.bosch.patientapp.Application.CustomApplication;

//import org.hl7.fhir.dstu3.model.Bundle;
//import org.hl7.fhir.dstu3.model.MedicationRequest;
//
//import java.util.List;
//
//import ca.uhn.fhir.util.BundleUtil;

public class MedicationFhirHelper {

    public MedicationFhirHelper() {

    }

   /* //http://hapi.fhir.org/baseDstu3/MedicationRequest?subject=Patient/2645014
    public List<MedicationRequest> getMedicationRequest(String patientId) {
        // Invoke the client
        Bundle bundle = CustomApplication.getInstance().getIGenericClient().search().forResource(MedicationRequest.class)
                .where(MedicationRequest.PATIENT.hasId(patientId))
                .returnBundle(Bundle.class)
                .execute();
        return BundleUtil.toListOfResourcesOfType(CustomApplication.getInstance().getFHIRContext(), bundle, MedicationRequest.class);
    }
*/
}