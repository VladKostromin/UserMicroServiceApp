package com.vladkostrov.usermicroserviceapp.controller;


import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualRequest;
import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualResponse;
import com.vladkostrov.dto.userservice.UserDto;
import com.vladkostrov.usermicroserviceapp.service.RegistrationIndividualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/individual")
@RequiredArgsConstructor
@Slf4j
public class RegistrationIndividualController {

    private final RegistrationIndividualService registrationIndividualServiceService;

    @PostMapping("/register")
    public ResponseEntity<RegisterIndividualResponse> register(@RequestBody RegisterIndividualRequest registerIndividualRequest) {
        log.info("IN register, request: {}", registerIndividualRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationIndividualServiceService.registerIndividual(registerIndividualRequest));
    }

    @PostMapping("/rollback")
    public ResponseEntity<RegisterIndividualResponse> rollback(@RequestBody RegisterIndividualResponse registerIndividualResponse) {
        log.info("IN rollback, request: {}", registerIndividualResponse);
        return ResponseEntity.status(HttpStatus.OK).body(registrationIndividualServiceService.rollbackRegisterIndividual(registerIndividualResponse));
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<UserDto> get(@PathVariable UUID id) {
        log.info("IN get, request: {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(registrationIndividualServiceService.getUserById(id));
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDto>> getAllUsersWithActiveStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(registrationIndividualServiceService.getAllActiveUsers());
    }

}
