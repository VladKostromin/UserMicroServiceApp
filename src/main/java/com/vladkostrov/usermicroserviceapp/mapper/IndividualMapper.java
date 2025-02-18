package com.vladkostrov.usermicroserviceapp.mapper;

import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualRequest;
import com.vladkostrov.usermicroserviceapp.entity.IndividualEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndividualMapper {
    IndividualEntity mapFromRequest(RegisterIndividualRequest request);
}
