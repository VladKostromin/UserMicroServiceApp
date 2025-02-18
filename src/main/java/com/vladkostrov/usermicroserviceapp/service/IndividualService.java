package com.vladkostrov.usermicroserviceapp.service;

import com.vladkostrov.usermicroserviceapp.entity.IndividualEntity;
import com.vladkostrov.usermicroserviceapp.enums.IndividualStatus;
import com.vladkostrov.usermicroserviceapp.exception.IndividualNotFoundException;
import com.vladkostrov.usermicroserviceapp.repository.IndividualRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndividualService {
    private final IndividualRepository individualRepository;

    public IndividualEntity createIndividual(IndividualEntity individual) {
        log.info("IN createIndividual, individual: {}", individual);
        individual.setStatus(IndividualStatus.CREATED);
        individual.setVerifiedAt(LocalDateTime.now());
        return individualRepository.save(individual);
    }

    public IndividualEntity getIndividualById(UUID id) {
        log.info("IN getIndividualById, id: {}", id);
        return individualRepository.findById(id).orElseThrow(() -> new IndividualNotFoundException("No individual found with id: " + id));
    }

    public IndividualEntity updateIndividual(IndividualEntity individual) {
        log.info("IN updateIndividual, individual: {}", individual);
        if(!individualRepository.existsById(individual.getId())) {
            throw new IndividualNotFoundException("No individual found to update with id: " + individual.getId());
        }
        return individualRepository.save(individual);
    }

    public List<IndividualEntity> getAllCreatedIndividuals() {
        log.info("IN getAllCreatedIndividuals");
        return individualRepository.findAll()
                .stream()
                .filter(i -> i.getStatus().equals(IndividualStatus.CREATED)).collect(Collectors.toList());
    }
    public List<IndividualEntity> getAllDeletedIndividuals() {
        log.info("IN getAllDeletedIndividuals");
        return individualRepository.findAll()
                .stream()
                .filter(i -> i.getStatus().equals(IndividualStatus.DELETED)).collect(Collectors.toList());
    }

    public void softDeleteIndividualById(UUID id) {
        log.info("IN softDeleteIndividualById, id: {}", id);
        IndividualEntity individualToDelete = individualRepository
                .findById(id).orElseThrow(() -> new IndividualNotFoundException("No individual found to soft delete with id: " + id));
        individualToDelete.setStatus(IndividualStatus.DELETED);
        individualRepository.save(individualToDelete);
    }
    public void hardDeleteIndividualById(UUID id) {
        log.info("IN hardDeleteIndividualById, id: {}", id);
        if(!individualRepository.existsById(id)) {
            throw new IndividualNotFoundException("No individual found to hard delete with id: " + id);
        }
        individualRepository.deleteById(id);
    }
}
