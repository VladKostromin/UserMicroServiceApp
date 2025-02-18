package com.vladkostrov.usermicroserviceapp.service;

import com.vladkostrov.usermicroserviceapp.entity.IndividualEntity;
import com.vladkostrov.usermicroserviceapp.enums.IndividualStatus;
import com.vladkostrov.usermicroserviceapp.exception.IndividualNotFoundException;
import com.vladkostrov.usermicroserviceapp.repository.IndividualRepository;
import com.vladkostrov.usermicroserviceapp.utils.IndividualDataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IndividualServiceTest {

    @Mock
    private IndividualRepository individualRepository;
    @InjectMocks
    private IndividualService individualServiceUnderTest;

    @Test
    @DisplayName("Test create individual success functionality")
    public void givenIndividualEntity_whenCreateIndividual_thenIndividualIsCreated() {
        //given
        IndividualEntity individualToCreate = IndividualDataUtils.getIndividualTransient();
        BDDMockito.given(individualRepository.save(individualToCreate)).willReturn(IndividualDataUtils.getIndividualPersistent());
        //when
        IndividualEntity createdIndividual = individualServiceUnderTest.createIndividual(individualToCreate);
        //then
        assertThat(createdIndividual).isNotNull();
        assertThat(createdIndividual.getId()).isNotNull();
        verify(individualRepository, times(1)).save(individualToCreate);
    }

    @Test
    @DisplayName("Test get individual by id success functionality")
    public void givenIndividualId_whenGetIndividualById_thenIndividualIsRetrieved() {
        //given
        UUID individualId = IndividualDataUtils.getIndividualPersistent().getId();
        BDDMockito.given(individualRepository.findById(any(UUID.class))).willReturn(Optional.of(IndividualDataUtils.getIndividualPersistent()));
        //when
        IndividualEntity retrievedIndividual = individualServiceUnderTest.getIndividualById(individualId);
        //then
        assertThat(retrievedIndividual).isNotNull();
        assertThat(retrievedIndividual.getId()).isEqualTo(individualId);
        verify(individualRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Test get individual by id fail functionality")
    public void givenIndividualId_whenGetIndividualById_thenIndividualNotFoundExceptionIsThrown() {
        //given
        UUID individualId = IndividualDataUtils.getIndividualPersistent().getId();
        BDDMockito.given(individualRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        //when
        IndividualNotFoundException exception = assertThrows(IndividualNotFoundException.class, () -> individualServiceUnderTest.getIndividualById(individualId));
        //then
        assertThat(exception.getMessage()).isEqualTo("No individual found with id: " + individualId);
        assertThat(exception.getErrorCode()).isEqualTo("INDIVIDUAL_NOT_FOUND_ERROR_CODE");
        verify(individualRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    @DisplayName("Test update individual success functionality")
    public void givenIndividualToUpdate_whenUpdateIndividual_thenIndividualIsUpdated() {
        //given
        IndividualEntity individualToUpdate = IndividualDataUtils.getIndividualPersistent();
        individualToUpdate.setEmail("updateTest@email.com");
        BDDMockito.given(individualRepository.existsById(any(UUID.class))).willReturn(true);
        BDDMockito.given(individualRepository.save(any(IndividualEntity.class))).willReturn(individualToUpdate);
        //when
        IndividualEntity updatedIndividual = individualServiceUnderTest.updateIndividual(individualToUpdate);
        //then
        assertThat(updatedIndividual).isNotNull();
        assertThat(updatedIndividual.getEmail()).isNotEqualTo(IndividualDataUtils.getIndividualPersistent().getEmail());
        verify(individualRepository, times(1)).existsById(any(UUID.class));
        verify(individualRepository, times(1)).save(individualToUpdate);
    }

    @Test
    @DisplayName("Test update individual fail functionality")
    public void givenIndividualToUpdate_whenUpdateIndividual_thenIndividualNotFoundExceptionIsThrown() {
        //given
        IndividualEntity individualToUpdate = IndividualDataUtils.getIndividualPersistent();
        BDDMockito.given(individualRepository.existsById(any(UUID.class))).willReturn(false);
        //when
        IndividualNotFoundException exception = assertThrows(IndividualNotFoundException.class, () -> individualServiceUnderTest.updateIndividual(individualToUpdate));
        //then
        assertThat(exception.getMessage()).isEqualTo("No individual found to update with id: " + individualToUpdate.getId());
        assertThat(exception.getErrorCode()).isEqualTo("INDIVIDUAL_NOT_FOUND_ERROR_CODE");
        verify(individualRepository, times(1)).existsById(any(UUID.class));
        verify(individualRepository, never()).save(individualToUpdate);
    }

    @Test
    @DisplayName("Test get all created individual success functionality")
    public void givenListOfIndividuals_whenGetAllCreatedIndividuals_thenListOfCreatedIndividualsIsRetrieved() {
        //given
        IndividualEntity individual1 = IndividualDataUtils.getIndividualPersistent();
        individual1.setStatus(IndividualStatus.CREATED);
        IndividualEntity individual2 = IndividualDataUtils.getIndividualPersistent();
        individual2.setStatus(IndividualStatus.CREATED);
        IndividualEntity individual3 = IndividualDataUtils.getIndividualPersistent();
        individual3.setStatus(IndividualStatus.CREATED);
        IndividualEntity individual4 = IndividualDataUtils.getIndividualPersistent();
        individual4.setStatus(IndividualStatus.DELETED);
        List<IndividualEntity> individuals = List.of(individual1, individual2, individual3, individual4);
        BDDMockito.given(individualRepository.findAll()).willReturn(individuals);
        //when
        List<IndividualEntity> allCreatedIndividuals = individualServiceUnderTest.getAllCreatedIndividuals();
        //then
        assertThat(CollectionUtils.isEmpty(allCreatedIndividuals)).isFalse();
        assertThat(allCreatedIndividuals.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test get all deleted individual success functionality")
    public void givenListOfIndividuals_whenGetAllDeletedIndividuals_thenListOfDeletedIndividualsIsRetrieved() {
        //given
        IndividualEntity individual1 = IndividualDataUtils.getIndividualPersistent();
        individual1.setStatus(IndividualStatus.CREATED);
        IndividualEntity individual2 = IndividualDataUtils.getIndividualPersistent();
        individual2.setStatus(IndividualStatus.CREATED);
        IndividualEntity individual3 = IndividualDataUtils.getIndividualPersistent();
        individual3.setStatus(IndividualStatus.DELETED);
        IndividualEntity individual4 = IndividualDataUtils.getIndividualPersistent();
        individual4.setStatus(IndividualStatus.DELETED);
        List<IndividualEntity> individuals = List.of(individual1, individual2, individual3, individual4);
        BDDMockito.given(individualRepository.findAll()).willReturn(individuals);
        //when
        List<IndividualEntity> allDeletedIndividuals = individualServiceUnderTest.getAllDeletedIndividuals();
        //then
        assertThat(CollectionUtils.isEmpty(allDeletedIndividuals)).isFalse();
        assertThat(allDeletedIndividuals.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test soft delete individual success functionality")
    public void givenIndividualId_whenSoftDeleteIndividualById_thenStatusIsDeleted() {
        //given
        IndividualEntity individualToDelete = IndividualDataUtils.getIndividualPersistent();
        BDDMockito.given(individualRepository.findById(any(UUID.class))).willReturn(Optional.of(individualToDelete));
        //when
        individualServiceUnderTest.softDeleteIndividualById(individualToDelete.getId());
        //then
        assertThat(individualToDelete.getStatus()).isEqualTo(IndividualStatus.DELETED);
        verify(individualRepository, times(1)).save(any(IndividualEntity.class));
        verify(individualRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("Test soft delete individual fail functionality")
    public void givenIndividualId_whenSoftDeleteIndividualById_thenIndividualNotFoundExceptionIsThrown() {
        //given
        BDDMockito.given(individualRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        //when
        IndividualNotFoundException exception = assertThrows(IndividualNotFoundException.class,
                () -> individualServiceUnderTest.softDeleteIndividualById(IndividualDataUtils.getIndividualPersistent().getId()));
        //then
        assertThat(exception.getMessage())
                .isEqualTo("No individual found to soft delete with id: " + IndividualDataUtils.getIndividualPersistent().getId());
        assertThat(exception.getErrorCode()).isEqualTo("INDIVIDUAL_NOT_FOUND_ERROR_CODE");
        verify(individualRepository, never()).save(any(IndividualEntity.class));

    }


    @Test
    @DisplayName("Test hard delete individual success functionality")
    public void givenIndividualId_whenHardDeleteIndividualById_thenIndividualIsDeleted() {
        //given
        BDDMockito.given(individualRepository.existsById(any(UUID.class))).willReturn(true);
        //when
        individualServiceUnderTest.hardDeleteIndividualById(IndividualDataUtils.getIndividualPersistent().getId());
        //then
        verify(individualRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("Test hard delete individual fail functionality")
    public void givenIndividualId_whenHardDeleteIndividual_thenIndividualNotFoundExceptionIsThrown() {
        //given
        BDDMockito.given(individualRepository.existsById(any(UUID.class))).willReturn(false);
        //when
        IndividualNotFoundException exception = assertThrows(IndividualNotFoundException.class,
                () -> individualServiceUnderTest.hardDeleteIndividualById(IndividualDataUtils.getIndividualPersistent().getId()));
        //then
        assertThat(exception.getMessage())
                .isEqualTo("No individual found to hard delete with id: " + IndividualDataUtils.getIndividualPersistent().getId());
        assertThat(exception.getErrorCode()).isEqualTo("INDIVIDUAL_NOT_FOUND_ERROR_CODE");
        verify(individualRepository, never()).deleteById(any(UUID.class));
    }
}
