package com.vladkostrov.usermicroserviceapp.repository;

import com.vladkostrov.usermicroserviceapp.entity.CountyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CountryRepository extends JpaRepository<CountyEntity, Integer> {

    CountyEntity findCountyEntityByAlpha2(String alpha2);

    CountyEntity findCountyEntityByAlpha3(String alpha3);
}
