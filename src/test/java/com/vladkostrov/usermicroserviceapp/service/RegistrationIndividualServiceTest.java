package com.vladkostrov.usermicroserviceapp.service;

import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualRequest;
import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualResponse;
import com.vladkostrov.usermicroserviceapp.entity.AddressEntity;
import com.vladkostrov.usermicroserviceapp.entity.IndividualEntity;
import com.vladkostrov.usermicroserviceapp.entity.UserEntity;
import com.vladkostrov.usermicroserviceapp.enums.IndividualStatus;
import com.vladkostrov.usermicroserviceapp.mapper.AddressMapper;
import com.vladkostrov.usermicroserviceapp.mapper.IndividualMapper;
import com.vladkostrov.usermicroserviceapp.mapper.UserMapper;
import com.vladkostrov.usermicroserviceapp.utils.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class RegistrationIndividualServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private AddressService addressService;
    @Mock
    private IndividualService individualService;
    @Mock
    private CountryService countryService;

    @Mock
    private UserMapper userMapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private IndividualMapper individualMapper;

    @InjectMocks
    private RegistrationIndividualService registrationServiceUnderTest;

    @Test
    @DisplayName("test register individual success functionality")
    public void givenRegisterIndividualRequest_whenRegisterIndividual_thenIndividualIsCreated() {
        //given
        RegisterIndividualRequest registerIndividualRequest = ApiDataUtils.getRegisterIndividualRequest();
        //Mapping objects:
        BDDMockito.given(addressMapper.map(registerIndividualRequest.getAddress())).willReturn(AddressDataUtils.getAddressTransient());
        BDDMockito.given(userMapper.mapFromRequest(registerIndividualRequest)).willReturn(UserDataUtils.getUserTransient());
        BDDMockito.given(individualMapper.mapFromRequest(registerIndividualRequest)).willReturn(IndividualDataUtils.getIndividualTransient());
        //Services:
        BDDMockito.given(userService.createUser(any(UserEntity.class))).willReturn(UserDataUtils.getUserPersistent());
        BDDMockito.given(addressService.createAddress(any(AddressEntity.class))).willReturn(AddressDataUtils.getAddressPersistent());
        BDDMockito.given(individualService.createIndividual(any(IndividualEntity.class))).willReturn(IndividualDataUtils.getIndividualPersistent());
        BDDMockito.given(countryService.getCountryByAlphaCode(anyString())).willReturn(CountryDataUtil.getCounty());

        //when
        RegisterIndividualResponse registerIndividualResponse = registrationServiceUnderTest.registerIndividual(registerIndividualRequest);

        //then

        //mapping:
        verify(addressMapper).map(registerIndividualRequest.getAddress());
        verify(userMapper).mapFromRequest(registerIndividualRequest);
        verify(individualMapper).mapFromRequest(registerIndividualRequest);

        //services:
        verify(userService, times(1)).createUser(any(UserEntity.class));
        verify(addressService, times(1)).createAddress(any(AddressEntity.class));
        verify(individualService, times(1)).createIndividual(any(IndividualEntity.class));

        //response verify:
        assertThat(registerIndividualResponse).isNotNull();
        assertThat(registerIndividualResponse.getUserId()).isEqualTo(UserDataUtils.getUserPersistent().getId());
        assertThat(registerIndividualResponse.getStatus()).isEqualTo("CREATED");

    }

    @Test
    @DisplayName("Test rollback individual success functionality")
    public void givenRegisterIndividualResponse_whenRollbackRegisterIndividual_thenIndividualIsRolledBack() {
        //given
        RegisterIndividualResponse registerIndividualResponse = ApiDataUtils.getRegisterIndividualResponse();

        //when
        RegisterIndividualResponse resultResponse = registrationServiceUnderTest.rollbackRegisterIndividual(registerIndividualResponse);

        //then

        verify(userService, times(1)).hardDeleteUserById(any(UUID.class));

        //response verify:
        assertThat(resultResponse).isNotNull();
        assertThat(resultResponse.getUserId()).isEqualTo(registerIndividualResponse.getUserId());
        assertThat(resultResponse.getStatus()).isEqualTo(IndividualStatus.DELETED.toString());


    }
}
