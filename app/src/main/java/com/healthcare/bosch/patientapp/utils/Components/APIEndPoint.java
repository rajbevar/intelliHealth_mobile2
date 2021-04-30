package com.healthcare.bosch.patientapp.utils.Components;

public interface APIEndPoint {


    String BASE_URL = "https://westus.api.cognitive.microsoft.com/spid/v1.0/";
    String DEV_BASE_URL = "https://westus.api.cognitive.microsoft.com/spid/v1.0/";

    String CREATE_PROFILE = "identificationProfiles";
    String CREATE_ENROLL = "identificationProfiles/%s/enroll?shortAudio=true";
    String IDENTIFY_PROFILE = "identify?identificationProfileIds=%s&shortAudio=true";

    String CREATE_VERIFY_PROFILE = "verificationProfiles";
    String CREATE_VERIFY_ENROLL = "verificationProfiles/%s/enroll";
    String VERIFY_PROFILE = "verify?verificationProfileId=%s";

    String CREATE_USER = "https://patientengtranscriptionapi.azurewebsites.net/api/User";
    String CREATE_NOTES = "https://patientengtranscriptionapi.azurewebsites.net/api/Note";

}

