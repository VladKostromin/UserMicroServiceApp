package com.vladkostrov.usermicroserviceapp.utils;

import com.vladkostrov.usermicroserviceapp.entity.UserEntity;
import com.vladkostrov.usermicroserviceapp.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDataUtils {
    private static final UUID userId = UUID.randomUUID();
    public static UserEntity getUserTransient() {
        return UserEntity.builder()
                .email("test@email.com")
                .filled(false)
                .firstName("testName")
                .lastName("testLastName")
                .address(AddressDataUtils.getAddressTransient())
                .build();
    }

    public static UserEntity getUserPersistent() {
        return UserEntity.builder()
                .id(userId)
                .email("test@email.com")
                .filled(true)
                .firstName("testName")
                .lastName("testLastName")
                .address(AddressDataUtils.getAddressPersistent())
                .status(UserStatus.ACTIVE)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
    }
}
