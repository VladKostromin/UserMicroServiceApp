package com.vladkostrov.usermicroserviceapp.utils;

import com.vladkostrov.usermicroserviceapp.entity.CountyEntity;

import java.time.LocalDateTime;

public class CountryDataUtil {
    public static CountyEntity getCounty(){
        return CountyEntity.builder()
                .id(1)
                .countryName("United States")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .alpha2("US")
                .alpha3("USA")
                .build();
    }

}
