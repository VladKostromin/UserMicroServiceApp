package com.vladkostrov.usermicroserviceapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "countries")
@Audited
public class CountyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime created;

    @Column
    private LocalDateTime updated;

    @Column(name = "country_name", length = 32)
    private String countryName;

    @Column(length = 2)
    private String alpha2;

    @Column(length = 3)
    private String alpha3;
}
