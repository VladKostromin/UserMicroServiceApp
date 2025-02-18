package com.vladkostrov.usermicroserviceapp.utils;

import com.vladkostrov.usermicroserviceapp.entity.IndividualEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class IndividualDataUtils {

    private static final UUID individualId = UUID.randomUUID();

    public static IndividualEntity getIndividualTransient() {
        return IndividualEntity.builder()
                .email("test@email.com")
                .phoneNumber("testPhoneNumber")
                .passportNumber("testPassportNumber")
                .user(UserDataUtils.getUserTransient())
                .build();
    }

    public static IndividualEntity getIndividualPersistent() {
        return IndividualEntity.builder()
                .id(individualId)
                .email("test@email.com")
                .phoneNumber("testPhoneNumber")
                .passportNumber("testPassportNumber")
                .user(UserDataUtils.getUserPersistent())
                .verifiedAt(LocalDateTime.now())
                .build();
    }
}
