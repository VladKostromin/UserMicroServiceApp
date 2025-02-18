package com.vladkostrov.usermicroserviceapp.repository;


import com.vladkostrov.usermicroserviceapp.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressesRepository extends JpaRepository<AddressEntity, UUID> {

}
