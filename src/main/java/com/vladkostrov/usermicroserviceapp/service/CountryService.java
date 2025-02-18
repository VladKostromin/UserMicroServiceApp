package com.vladkostrov.usermicroserviceapp.service;

import com.vladkostrov.usermicroserviceapp.entity.CountyEntity;
import com.vladkostrov.usermicroserviceapp.exception.CountryNotFoundException;
import com.vladkostrov.usermicroserviceapp.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryService {

    private final CountryRepository countryRepository;

    public CountyEntity getCountryByAlphaCode(String alphaCode) {
        log.info("IN getCountryByAlphaCode, alphaCode: {}", alphaCode);
        if(alphaCode.length() == 2) {
            CountyEntity country = countryRepository.findCountyEntityByAlpha2(alphaCode);
            if(country == null) {
                throw new CountryNotFoundException("No country found with alpha2 code: " + alphaCode);
            }
            return country;
        } else if(alphaCode.length() == 3) {
            CountyEntity country = countryRepository.findCountyEntityByAlpha3(alphaCode);
            if(country == null) {
                throw new CountryNotFoundException("No country found with alpha3 code: " + alphaCode);
            }
            return country;
        } else {
            throw new CountryNotFoundException("No country found wrong alpha code: " + alphaCode);
        }

    }
}
