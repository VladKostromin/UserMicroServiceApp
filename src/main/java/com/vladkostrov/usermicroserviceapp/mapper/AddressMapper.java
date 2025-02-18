package com.vladkostrov.usermicroserviceapp.mapper;

import com.vladkostrov.dto.userservice.AddressDto;
import com.vladkostrov.usermicroserviceapp.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    AddressEntity map(AddressDto addressDto);

}
