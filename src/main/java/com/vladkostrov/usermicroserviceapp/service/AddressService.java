package com.vladkostrov.usermicroserviceapp.service;

import com.vladkostrov.usermicroserviceapp.entity.AddressEntity;
import com.vladkostrov.usermicroserviceapp.exception.AddressNotFoundException;
import com.vladkostrov.usermicroserviceapp.repository.AddressesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {
    private final AddressesRepository addressesRepository;

    public AddressEntity createAddress(AddressEntity address) {
        log.info("IN createAddress, address: {}", address);
        address.setCreated(LocalDateTime.now());
        address.setUpdated(LocalDateTime.now());
        address.setArchived(LocalDateTime.now());
        return addressesRepository.save(address);
    }

    public AddressEntity getAddressById(UUID id) {
        log.info("IN getAddressById, id: {}", id);
        return addressesRepository.findById(id).orElseThrow(() -> new AddressNotFoundException("No address found with id: " + id));
    }

    public List<AddressEntity> getAllAddresses() {
        log.info("IN getAllAddresses");
        return addressesRepository.findAll();
    }

    public AddressEntity updateAddress(AddressEntity address) {
        log.info("IN updateAddress, address: {}", address);
        if(!addressesRepository.existsById(address.getId())) {
            throw new AddressNotFoundException("No address found to update with id: " + address.getId());
        }
        return addressesRepository.save(address);
    }
    public void deleteAddress(UUID id) {
        log.info("IN deleteAddress, id: {}", id);
        if(!addressesRepository.existsById(id)) {
            throw new AddressNotFoundException("No address found to delete with id: " + id);
        }
        addressesRepository.deleteById(id);
    }

}
