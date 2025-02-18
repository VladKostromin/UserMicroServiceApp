package com.vladkostrov.usermicroserviceapp.repository;

import com.vladkostrov.usermicroserviceapp.entity.IndividualEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IndividualRepository extends JpaRepository<IndividualEntity, UUID> {
}
