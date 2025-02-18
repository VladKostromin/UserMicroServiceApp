package com.vladkostrov.usermicroserviceapp.utils;

import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualRequest;
import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualResponse;
import com.vladkostrov.usermicroserviceapp.enums.IndividualStatus;

public class ApiDataUtils {
    public static RegisterIndividualRequest getRegisterIndividualRequest() {
       return RegisterIndividualRequest.builder()
                .address(AddressDataUtils.getAddressDto())
                .email("test@email.com")
                .phoneNumber("testPhoneNumber")
               .passportNumber("testPassportNumber")
                .firstName("testFirstName")
                .lastName("testLastName")
                .build();
    }
    public static RegisterIndividualResponse getRegisterIndividualResponse() {
        return RegisterIndividualResponse.builder()
                .userId(UserDataUtils.getUserPersistent().getId())
                .status(IndividualStatus.CREATED.toString())
                .build();
    }
}
