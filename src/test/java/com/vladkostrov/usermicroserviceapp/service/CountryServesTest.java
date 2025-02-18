package com.vladkostrov.usermicroserviceapp.service;

import com.vladkostrov.usermicroserviceapp.entity.CountyEntity;
import com.vladkostrov.usermicroserviceapp.exception.CountryNotFoundException;
import com.vladkostrov.usermicroserviceapp.repository.CountryRepository;
import com.vladkostrov.usermicroserviceapp.utils.CountryDataUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CountryServesTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @Test
    @DisplayName("Test get country alpha2 success functionality")
    public void givenStringAlpha2_whenGetCountryAlpha2_thenCountryRetrieved() {
        //given
        BDDMockito.given(countryRepository.findCountyEntityByAlpha2(anyString())).willReturn(CountryDataUtil.getCounty());
        //when
        CountyEntity retrievedCountry = countryService.getCountryByAlphaCode(CountryDataUtil.getCounty().getAlpha2());
        //then
        assertThat(retrievedCountry).isNotNull();
        assertThat(retrievedCountry.getAlpha2()).isEqualTo(CountryDataUtil.getCounty().getAlpha2());
        verify(countryRepository, times(1)).findCountyEntityByAlpha2(anyString());
    }

    @Test
    @DisplayName("Test get country alpha2 fail functionality")
    public void givenStringAlpha2_whenGetCountryAlpha2_thenCountryNotFoundExceptionIsThrown() {
        //given
        BDDMockito.given(countryRepository.findCountyEntityByAlpha2(anyString())).willReturn(null);
        //when
        CountryNotFoundException countryNotFoundException = assertThrows(CountryNotFoundException.class,
                () -> countryService.getCountryByAlphaCode(CountryDataUtil.getCounty().getAlpha2()));
        //then
        assertThat(countryNotFoundException.getMessage()).isEqualTo("No country found with alpha2 code: " + CountryDataUtil.getCounty().getAlpha2());
        assertThat(countryNotFoundException.getErrorCode()).isEqualTo("COUNTRY_NOT_FOUND_ERROR_CODE");
        verify(countryRepository, times(1)).findCountyEntityByAlpha2(anyString());
    }

    @Test
    @DisplayName("Test get country alpha3 success functionality")
    public void givenStringAlpha2_whenGetCountryAlpha3_thenCountryRetrieved() {
        //given
        BDDMockito.given(countryRepository.findCountyEntityByAlpha3(anyString())).willReturn(CountryDataUtil.getCounty());
        //when
        CountyEntity retrievedCountry = countryService.getCountryByAlphaCode(CountryDataUtil.getCounty().getAlpha3());
        //then
        assertThat(retrievedCountry).isNotNull();
        assertThat(retrievedCountry.getAlpha3()).isEqualTo(CountryDataUtil.getCounty().getAlpha3());
        verify(countryRepository, times(1)).findCountyEntityByAlpha3(anyString());
    }

    @Test
    @DisplayName("Test get country alpha3 fail functionality")
    public void givenStringAlpha2_whenGetCountryAlpha3_thenCountryNotFoundExceptionIsThrown() {
        //given
        BDDMockito.given(countryRepository.findCountyEntityByAlpha3(anyString())).willReturn(null);
        //when
        CountryNotFoundException countryNotFoundException = assertThrows(CountryNotFoundException.class,
                () -> countryService.getCountryByAlphaCode(CountryDataUtil.getCounty().getAlpha3()));
        //then
        assertThat(countryNotFoundException.getMessage()).isEqualTo("No country found with alpha3 code: " + CountryDataUtil.getCounty().getAlpha3());
        assertThat(countryNotFoundException.getErrorCode()).isEqualTo("COUNTRY_NOT_FOUND_ERROR_CODE");
        verify(countryRepository, times(1)).findCountyEntityByAlpha3(anyString());
    }
}
