package com.vladkostrov.usermicroserviceapp.utils;

import com.vladkostrov.dto.userservice.AddressDto;
import com.vladkostrov.usermicroserviceapp.entity.AddressEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class AddressDataUtils {

    private static final UUID addressId = UUID.randomUUID();

    public static AddressEntity getAddressTransient() {
        return AddressEntity.builder()
                .address("testAddress")
                .city("testCity")
                .zipCode("testZipCode")
                .build();
    }

    public static AddressEntity getAddressPersistent() {
        return AddressEntity.builder()
                .id(addressId)
                .address("testAddress")
                .city("testCity")
                .zipCode("testZipCode")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .archived(LocalDateTime.now())
                .build();
    }

    public static AddressDto getAddressDto() {
        return AddressDto.builder()
                .address("testAddress")
                .city("testCity")
                .zipCode("testZipCode")
                .countryCode("USA")
                .countryName("testCountry")
                .build();
    }
}
