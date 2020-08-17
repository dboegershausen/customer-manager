package com.customermanager.customermanager.domain.service;

import com.customermanager.customermanager.domain.repository.CustomerRepository;
import com.customermanager.customermanager.exception.CustomerNotFoundException;
import com.customermanager.customermanager.domain.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    @Transactional
    public Customer create(Customer customer) {
        return repository.save(customer);
    }

    @Transactional
    public Customer update(Long id, Customer customer) {
        Customer customerToUpdate = getCustomer(id);
        if (!Objects.isNull(customer.getName())) {
            customerToUpdate.setName(customer.getName());
        }
        if (!Objects.isNull(customer.getCpf())) {
            customerToUpdate.setCpf(customer.getCpf());
        }
        if (!Objects.isNull(customer.getBirthDate())) {
            customerToUpdate.setBirthDate(customer.getBirthDate());
        }
        if (!Objects.isNull(customer.getAddress())) {
            if (!Objects.isNull(customer.getAddress().getPostalCode())) {
                customerToUpdate.getAddress().setPostalCode(customer.getAddress().getPostalCode());
            }
            if (!Objects.isNull(customer.getAddress().getStreet())) {
                customerToUpdate.getAddress().setStreet(customer.getAddress().getStreet());
            }
            customerToUpdate.getAddress().setNumber(customer.getAddress().getNumber());
            customerToUpdate.getAddress().setDetails(customer.getAddress().getDetails());
        }
        return customerToUpdate;
    }

    public List<Customer> listAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = getCustomer(id);
        repository.deleteById(customer.getId());
    }

    public Customer getCustomer(Long id) {
        return repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

}
