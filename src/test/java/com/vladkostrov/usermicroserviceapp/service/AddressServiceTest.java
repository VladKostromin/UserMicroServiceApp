package com.vladkostrov.usermicroserviceapp.service;

import com.vladkostrov.usermicroserviceapp.entity.AddressEntity;
import com.vladkostrov.usermicroserviceapp.exception.AddressNotFoundException;
import com.vladkostrov.usermicroserviceapp.repository.AddressesRepository;
import com.vladkostrov.usermicroserviceapp.utils.AddressDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressesRepository addressesRepository;
    @InjectMocks
    private AddressService addressServiceUnderTest;


    @Test
    @DisplayName("Test create address functionality")
    public void givenAddressToCreate_whenCreateAddress_thenAddressIsCreated() {
        //given
        AddressEntity addressToCreate = AddressDataUtils.getAddressTransient();
        BDDMockito.given(addressesRepository.save(addressToCreate)).willReturn(AddressDataUtils.getAddressPersistent());
        //when
        AddressEntity createdAddress = addressServiceUnderTest.createAddress(addressToCreate);
        //then
        assertThat(createdAddress).isNotNull();
        assertThat(createdAddress.getId()).isNotNull();
        verify(addressesRepository, times(1)).save(addressToCreate);
    }

    @Test
    @DisplayName("Test get address by id success functionality")
    public void givenAddressId_whenGetAddressById_thenAddressIsRetrieved() {
        //given
        UUID addressId = AddressDataUtils.getAddressPersistent().getId();
        BDDMockito.given(addressesRepository.findById(any(UUID.class))).willReturn(Optional.of(AddressDataUtils.getAddressPersistent()));
        //when
        AddressEntity retrievedAddress = addressServiceUnderTest.getAddressById(addressId);
        //then
        assertThat(retrievedAddress).isNotNull();
        assertThat(retrievedAddress.getId()).isEqualTo(addressId);
        verify(addressesRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Test get address by id fail functionality")
    public void givenAddressId_whenGetAddressById_thenAddressNotFoundExceptionIsThrown() {
        //given
        UUID addressId = AddressDataUtils.getAddressPersistent().getId();
        BDDMockito.given(addressesRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        //when
        AddressNotFoundException addressNotFoundException = assertThrows(AddressNotFoundException.class, () -> addressServiceUnderTest.getAddressById(addressId));
        //then
        assertThat(addressNotFoundException.getMessage()).isEqualTo("No address found with id: " + addressId);
        verify(addressesRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Test get all addresses success functionality")
    public void givenListAddresses_whenGetAllAddresses_thenAllAddressesAreRetrieved() {
        //given
        List<AddressEntity> addresses = List.of(AddressDataUtils.getAddressPersistent(), AddressDataUtils.getAddressPersistent(), AddressDataUtils.getAddressPersistent());
        BDDMockito.given(addressesRepository.findAll()).willReturn(addresses);
        //when
        List<AddressEntity> retrievedAddresses = addressServiceUnderTest.getAllAddresses();
        //then
        assertThat(retrievedAddresses).isNotNull();
        assertThat(retrievedAddresses.size()).isEqualTo(addresses.size());
        verify(addressesRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test update address success functionality")
    public void givenAddressToUpdate_whenUpdateAddress_thenAddressIsUpdated() {
        //given
        AddressEntity addressToUpdate = AddressDataUtils.getAddressPersistent();
        addressToUpdate.setCity("updateTestCity");
        BDDMockito.given(addressesRepository.existsById(any(UUID.class))).willReturn(true);
        BDDMockito.given(addressesRepository.save(any(AddressEntity.class))).willReturn(addressToUpdate);
        //when
        AddressEntity updatedAddress = addressServiceUnderTest.updateAddress(addressToUpdate);
        //then
        assertThat(updatedAddress).isNotNull();
        assertThat(updatedAddress.getCity()).isNotEqualTo(AddressDataUtils.getAddressPersistent().getCity());
        verify(addressesRepository, times(1)).existsById(any(UUID.class));
        verify(addressesRepository, times(1)).save(any(AddressEntity.class));
    }

    @Test
    @DisplayName("Test update address fail functionality")
    public void givenAddressToUpdate_whenUpdateAddress_thenAddressNotFoundExceptionIsThrown() {
        //given
        AddressEntity addressToUpdate = AddressDataUtils.getAddressPersistent();
        BDDMockito.given(addressesRepository.existsById(any(UUID.class))).willReturn(false);
        //when
        AddressNotFoundException addressNotFoundException = assertThrows(AddressNotFoundException.class, () -> addressServiceUnderTest.updateAddress(addressToUpdate));
        //then
        assertThat(addressNotFoundException.getMessage()).isEqualTo("No address found to update with id: " + addressToUpdate.getId());
        assertThat(addressNotFoundException.getErrorCode()).isEqualTo("ADDRESS_NOT_FOUND_ERROR_CODE");
        verify(addressesRepository, times(1)).existsById(any(UUID.class));
        verify(addressesRepository, never()).save(any(AddressEntity.class));
    }

    @Test
    @DisplayName("Test delete address success functionality")
    public void givenAddressToDeleteId_whenDeleteAddressById_thenAddressIsDeleted() {
        //given
        UUID addressId = AddressDataUtils.getAddressPersistent().getId();
        BDDMockito.given(addressesRepository.existsById(any(UUID.class))).willReturn(true);
        //when
        addressServiceUnderTest.deleteAddress(addressId);
        //then
        verify(addressesRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("Test delete address fail functionality")
    public void givenAddressToDelete_whenDeleteAddressById_thenAddressNotFoundExceptionIsThrown() {
        //given
        UUID addressId = AddressDataUtils.getAddressPersistent().getId();
        BDDMockito.given(addressesRepository.existsById(any(UUID.class))).willReturn(false);
        //when
        AddressNotFoundException addressNotFoundException = assertThrows(AddressNotFoundException.class, () -> addressServiceUnderTest.deleteAddress(addressId));
        //then
        assertThat(addressNotFoundException.getMessage()).isEqualTo("No address found to delete with id: " + addressId);
        assertThat(addressNotFoundException.getErrorCode()).isEqualTo("ADDRESS_NOT_FOUND_ERROR_CODE");
        verify(addressesRepository, never()).deleteById(any(UUID.class));

    }


}
