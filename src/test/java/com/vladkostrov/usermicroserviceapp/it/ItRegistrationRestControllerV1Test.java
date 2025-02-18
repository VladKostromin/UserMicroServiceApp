package com.vladkostrov.usermicroserviceapp.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualRequest;
import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualResponse;
import com.vladkostrov.usermicroserviceapp.entity.AddressEntity;
import com.vladkostrov.usermicroserviceapp.entity.CountyEntity;
import com.vladkostrov.usermicroserviceapp.entity.IndividualEntity;
import com.vladkostrov.usermicroserviceapp.entity.UserEntity;
import com.vladkostrov.usermicroserviceapp.enums.UserStatus;
import com.vladkostrov.usermicroserviceapp.repository.AddressesRepository;
import com.vladkostrov.usermicroserviceapp.repository.CountryRepository;
import com.vladkostrov.usermicroserviceapp.repository.IndividualRepository;
import com.vladkostrov.usermicroserviceapp.repository.UserRepository;
import com.vladkostrov.usermicroserviceapp.utils.*;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItRegistrationRestControllerV1Test extends AbstractRestControllerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AddressesRepository addressesRepository;
    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    public void setUp() {
        //refresh database
        userRepository.deleteAll();
        individualRepository.deleteAll();
        addressesRepository.deleteAll();
        countryRepository.deleteAll();

        //setup data
        CountyEntity county = CountryDataUtil.getCounty();
        county.setId(null);
        CountyEntity persistedCountry = countryRepository.save(county);

        AddressEntity addressTransient = AddressDataUtils.getAddressTransient();
        addressTransient.setCounty(persistedCountry);
        AddressEntity addressPersisted = addressesRepository.save(addressTransient);

        UserEntity userTransient = UserDataUtils.getUserTransient();
        userTransient.setCreated(LocalDateTime.now());
        userTransient.setUpdated(LocalDateTime.now());
        userTransient.setAddress(addressPersisted);
        userTransient.setStatus(UserStatus.ACTIVE);
        UserEntity userPersisted = userRepository.save(userTransient);


        IndividualEntity individualTransient = IndividualDataUtils.getIndividualTransient();
        individualTransient.setVerifiedAt(LocalDateTime.now());
        individualTransient.setUser(userPersisted);
        individualRepository.save(individualTransient);
    }

    @Test
    @DisplayName("Test register individual success functionality")
    void givenRegisterIndividualRequest_whenRegister_thenSuccessRegisterIndividualResponse() throws Exception {
        //given
        RegisterIndividualRequest registerIndividualRequest = ApiDataUtils.getRegisterIndividualRequest();
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/individual/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerIndividualRequest)));
        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("CREATED")));
    }

    @Test
    @DisplayName("Test rollback individual success functionality")
    void givenRegisterIndividualResponse_whenRollback_thenSuccessRegisterIndividualResponse() throws Exception {
        //given
        UUID userId = userRepository.findAll().stream().findFirst().orElseThrow().getId();
        RegisterIndividualResponse registerIndividualResponse = RegisterIndividualResponse.builder()
                .userId(userId)
                .build();
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/individual/rollback")
                .content(objectMapper.writeValueAsString(registerIndividualResponse))
                .contentType(MediaType.APPLICATION_JSON));


        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id", CoreMatchers.is(userId.toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is("DELETED")));

        await().atMost(2, TimeUnit.SECONDS).until(() -> individualRepository.findById(userId).isEmpty());
        Optional<UserEntity> userById = userRepository.findById(userId);
        assertThat(userById).isEmpty();
    }

}
