package com.vladkostrov.usermicroserviceapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "addresses")
@Audited
public class AddressEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column
    private LocalDateTime created;

    @Column
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountyEntity county;

    @Column(length = 128)
    private String address;

    @Column(name = "zip_code", length = 32)
    private String zipCode;

    @Column
    private LocalDateTime archived;

    @Column(length = 32)
    private String city;

}
