package com.vladkostrov.usermicroserviceapp.mapper;

import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualRequest;
import com.vladkostrov.dto.userservice.UserDto;
import com.vladkostrov.usermicroserviceapp.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserEntity mapFromRequest(RegisterIndividualRequest user);

    UserDto map(UserEntity user);

    @InheritInverseConfiguration
    UserEntity map(UserDto userDto);
}
