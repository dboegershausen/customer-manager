package com.customermanager.customermanager.service;

import com.customermanager.customermanager.domain.model.Address;
import com.customermanager.customermanager.domain.model.Customer;
import com.customermanager.customermanager.domain.repository.CustomerRepository;
import com.customermanager.customermanager.domain.service.CustomerService;
import com.customermanager.customermanager.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {

    @Autowired
    private CustomerService service;

    @MockBean
    private CustomerRepository repository;

    Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setName("João Silveira");
        customer.setCpf("044.568.245-95");
        customer.setBirthDate(LocalDate.of(1970, 5, 16));
        Address address = new Address();
        address.setPostalCode("87221-90");
        address.setStreet("Rua das Oliveiras");
        address.setNumber("165");
        address.setDetails("Apto 801 - Bloco B");
        customer.setAddress(address);
    }

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(repository).isNotNull();
    }

    @Test
    public void shouldCreateCustomer() {
        when(repository.save(any(Customer.class))).thenReturn(customer);
        Customer newCustomer = service.create(customer);
        assertThat(newCustomer).isNotNull();
        assertThat(newCustomer).isEqualTo(customer);
    }

    @Test
    public void shouldUpdateCustomer() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(customer));
        when(repository.save(any(Customer.class))).thenReturn(customer);

        Customer customerToUpdate = new Customer();
        customerToUpdate.setName("Novo Nome");
        customerToUpdate.setCpf("222.333.444-77");
        customerToUpdate.setBirthDate(LocalDate.of(1990, 5, 6));
        Address address = new Address();
        address.setPostalCode("88555-748");
        address.setStreet("Rua das Palmeiras");
        address.setNumber("132");
        address.setDetails("Apto 901");
        customerToUpdate.setAddress(address);

        Customer updatedCustomer = service.update(1L, customerToUpdate);

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getName()).isEqualTo(customerToUpdate.getName());
        assertThat(updatedCustomer.getCpf()).isEqualTo(customerToUpdate.getCpf());
        assertThat(updatedCustomer.getBirthDate()).isEqualTo(customerToUpdate.getBirthDate());
    }

    @Test
    public void shouldDeleteCustomer() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(customer));
        when(repository.save(any(Customer.class))).thenReturn(customer);

        service.delete(1L);

        assertThat(!repository.findById(1L).isPresent());
    }

    @Test
    public void shouldThrowCustomerNotFoundException() {
        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            service.getCustomer(1000L);
        });
    }

    @Test
    public void shouldGetCustomer() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(customer));

        Customer expectedCustomer = service.getCustomer(1L);

        assertThat(expectedCustomer).isNotNull();
        assertThat(expectedCustomer.getName()).isEqualTo(customer.getName());
    }

    @Test
    public void shouldListAllCustomers() {
        when(repository.findAll()).thenReturn(Collections.singletonList(customer));

        assertThat(service.listAll()).isNotEmpty();
    }

}
