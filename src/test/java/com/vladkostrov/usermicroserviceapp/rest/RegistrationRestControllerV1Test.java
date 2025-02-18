package com.vladkostrov.usermicroserviceapp.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualRequest;
import com.vladkostrov.dto.rest.userserviceapi.RegisterIndividualResponse;
import com.vladkostrov.usermicroserviceapp.enums.IndividualStatus;
import com.vladkostrov.usermicroserviceapp.service.RegistrationIndividualService;
import com.vladkostrov.usermicroserviceapp.utils.ApiDataUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest
public class RegistrationRestControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    @SuppressWarnings("unused")
    private RegistrationIndividualService registrationIndividualService;

    @Test
    @DisplayName("Test register individual success functionality")
    void givenRegisterIndividualRequest_whenRegister_thenSuccessRegisterIndividualResponse() throws Exception {
        //given
        RegisterIndividualRequest registerIndividualRequest = ApiDataUtils.getRegisterIndividualRequest();
        RegisterIndividualResponse registerIndividualResponse = ApiDataUtils.getRegisterIndividualResponse();
        BDDMockito.given(registrationIndividualService.registerIndividual(any(RegisterIndividualRequest.class))).willReturn(registerIndividualResponse);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/individual/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerIndividualRequest)));
        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(registerIndividualResponse.getStatus())));
    }

    @Test
    @DisplayName("Test rollback individual success functionality")
    void givenRegisterIndividualResponse_whenRollback_thenSuccessRollbackIndividualResponse() throws Exception {
        //given
        RegisterIndividualResponse registerIndividualResponse = ApiDataUtils.getRegisterIndividualResponse();
        registerIndividualResponse.setStatus(IndividualStatus.DELETED.toString());
        BDDMockito.given(registrationIndividualService.rollbackRegisterIndividual(any(RegisterIndividualResponse.class))).willReturn(registerIndividualResponse);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/individual/rollback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerIndividualResponse)));

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user_id", CoreMatchers.is(registerIndividualResponse.getUserId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(IndividualStatus.DELETED.toString())));

    }
}
