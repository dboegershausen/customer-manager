package com.customermanager.customermanager.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.customermanager.customermanager.api.v1.dto.AddressRequestDto;
import com.customermanager.customermanager.api.v1.dto.CustomerRequestDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    CustomerRequestDto customer;

    private static final String DOMAIN = "v1/customers";

    @BeforeEach
    public void setUp() {
        customer = new CustomerRequestDto();
        customer.setName("João Silveira");
        customer.setCpf("044.568.245-95");
        customer.setBirthDate(LocalDate.of(1970, 5, 16));
        AddressRequestDto address = new AddressRequestDto();
        address.setPostalCode("87221-90");
        address.setStreet("Rua das Oliveiras");
        address.setNumber("165");
        address.setDetails("Apto 801 - Bloco B");
        customer.setAddress(address);
    }

    @AfterEach
    void tearDown() {
        mvc = null;
        customer = null;
    }

    @Test
    void contextLoads() {
        assertThat(mvc).isNotNull();
    }

    @Test
    public void shouldCreateCustomer() throws Exception {
        int expectedId = 1;
        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(customer)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedId)));
    }

    @Test
    public void shouldValidateEmptyFieldsOnCreateCustomer() throws Exception {
        customer.setName("");
        customer.setCpf("");
        customer.setBirthDate(null);
        customer.setAddress(null);
        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(customer)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(4)))
                .andExpect(jsonPath("$.errors[0]").value(containsString("must be informed")));
    }

    @Test
    public void shouldValidateBirthDateOnCreateCustomer() throws Exception {
        customer.setBirthDate(LocalDate.of(9999, 11, 30));
        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(customer)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]", is("BirthDate must be less than today")));
    }

    @Test
    public void shouldUpdateCustomer() throws Exception {
        int expectedId = 1;

        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(customer)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedId)));

        customer.setName("José dos Santos");
        customer.setCpf("032.121.649-12");
        customer.setBirthDate(LocalDate.of(1990, 5, 20));
        AddressRequestDto address = new AddressRequestDto();
        address.setPostalCode("89555-96");
        address.setStreet("Rua das Sombreiros");
        address.setNumber("700");
        address.setDetails("Apto 701");
        customer.setAddress(address);

        mvc.perform(MockMvcRequestBuilders.put("/" + DOMAIN + "/" + expectedId)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(customer)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(customer.getName())))
                .andExpect(jsonPath("$.cpf", is(customer.getCpf())))
                .andExpect(jsonPath("$.address.postalCode", is(customer.getAddress().getPostalCode())))
                .andExpect(jsonPath("$.address.street", is(customer.getAddress().getStreet())))
                .andExpect(jsonPath("$.address.number", is(customer.getAddress().getNumber())))
                .andExpect(jsonPath("$.address.details", is(customer.getAddress().getDetails())));
    }

    @Test
    public void canRetrieveByIdWhenDoesNotExist() throws Exception {
        int notExpectedId = 2;
        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN + "/" + notExpectedId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]", is("Could not find customer with id " + notExpectedId)));
    }

    @Test
    public void shouldListAllCustomers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    public void shouldGetCustomer() throws Exception {
        int expectedId = 1;

        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(customer)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedId)));

        mvc.perform(MockMvcRequestBuilders.get("/" + DOMAIN + "/" + expectedId))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedId)))
                .andExpect(jsonPath("$.name", is(customer.getName())));
    }

    @Test
    public void shouldDeleteCustomer() throws Exception {
        int expectedId = 1;

        mvc.perform(MockMvcRequestBuilders.post("/" + DOMAIN)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(customer)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(expectedId)));

        mvc.perform(MockMvcRequestBuilders.delete("/" + DOMAIN + "/" + expectedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(json, clazz);
    }

}
