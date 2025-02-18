package com.vladkostrov.usermicroserviceapp.service;

import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualRequest;
import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualResponse;
import com.vladkostrov.dto.userservice.UserDto;
import com.vladkostrov.usermicroserviceapp.entity.AddressEntity;
import com.vladkostrov.usermicroserviceapp.entity.CountyEntity;
import com.vladkostrov.usermicroserviceapp.entity.IndividualEntity;
import com.vladkostrov.usermicroserviceapp.entity.UserEntity;
import com.vladkostrov.usermicroserviceapp.enums.IndividualStatus;
import com.vladkostrov.usermicroserviceapp.mapper.AddressMapper;
import com.vladkostrov.usermicroserviceapp.mapper.IndividualMapper;
import com.vladkostrov.usermicroserviceapp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationIndividualService {
    private final UserService userService;
    private final IndividualService individualService;
    private final AddressService addressService;
    private final CountryService countryService;

    private final AddressMapper addressMapper;
    private final UserMapper userMapper;
    private final IndividualMapper individualMapper;

    @Transactional
    public RegisterIndividualResponse registerIndividual(RegisterIndividualRequest registerIndividualRequest) {
        log.info("IN registerIndividual, request: {}", registerIndividualRequest);
        log.info("Mapping objects from request");
        AddressEntity addressEntity = addressMapper.map(registerIndividualRequest.getAddress());
        UserEntity userEntity = userMapper.mapFromRequest(registerIndividualRequest);
        userEntity.setEmail(registerIndividualRequest.getEmail());
        IndividualEntity individualEntity = individualMapper.mapFromRequest(registerIndividualRequest);
        log.info("After mapping, address: {}, individual: {}, user: {}", addressEntity, individualEntity, userEntity);
        String countryCode = registerIndividualRequest.getAddress().getCountryCode();
        CountyEntity country = countryService.getCountryByAlphaCode(countryCode);

        addressEntity.setCounty(country);
        AddressEntity persistedAddress = addressService.createAddress(addressEntity);

        userEntity.setAddress(persistedAddress);
        UserEntity persistedUser = userService.createUser(userEntity);

        individualEntity.setUser(persistedUser);
        IndividualEntity persistedIndividual = individualService.createIndividual(individualEntity);
        log.info("All objects created, persistedAddress: {}, persistedIndividual: {}, persistedUser: {}", persistedAddress, persistedIndividual, persistedUser);
        return RegisterIndividualResponse.builder()
                .userId(persistedUser.getId())
                .status(IndividualStatus.CREATED.toString())
                .build();
    }
    @Transactional
    public RegisterIndividualResponse rollbackRegisterIndividual(RegisterIndividualResponse registerIndividualResponse) {
        log.info("IN rollbackRegisterIndividual, request: {}", registerIndividualResponse);
        userService.hardDeleteUserById(registerIndividualResponse.getUserId());
        log.info("IN rollbackRegisterIndividual, rollback complete, objects deleted");
        return RegisterIndividualResponse.builder()
                .userId(registerIndividualResponse.getUserId())
                .status(IndividualStatus.DELETED.toString())
                .build();
    }

    public UserDto getUserById(UUID userUUID) {
        log.info("IN getUserById, request: {}", userUUID);
        return userMapper.map(userService.getUserById(userUUID));
    }

    public List<UserDto> getAllActiveUsers( ) {
        log.info("IN getAllActiveUsers, request");
        return userService.getAllActiveUsers().stream().map(userMapper::map).toList();
    }
}
