package com.customermanager.customermanager.api.v1.mapper;

import com.customermanager.customermanager.api.v1.dto.CustomerRequestDto;
import com.customermanager.customermanager.api.v1.dto.CustomerResponseDto;
import com.customermanager.customermanager.domain.model.Customer;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    private ModelMapper modelMapper;

    public CustomerMapper() {
        modelMapper = new ModelMapper();

        Converter<String, LocalDate> toStringDate = new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String source) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(source, format);
                return localDate;
            }
        };

        var createTypeMap = modelMapper.createTypeMap(CustomerRequestDto.class, Customer.class);
        createTypeMap.addMapping(CustomerRequestDto::getBirthDate, Customer::setBirthDate);

    }

    @Autowired
    private AddressMapper addressMapper;

    public Customer toCustomer(CustomerRequestDto customerDto) {
        Customer customer = new Customer();
        if (!Objects.isNull(customerDto.getAddress())) {
            customer.setAddress(addressMapper.toAddress(customerDto.getAddress()));
        }
        modelMapper.map(customerDto, customer);
        return customer;
    }

    public CustomerResponseDto toCustomerResponseDto(Customer customer) {
        CustomerResponseDto customerDto = new CustomerResponseDto();
        if (!Objects.isNull(customer.getAddress())) {
            customerDto.setAddress(addressMapper.toAddressResponseDto(customer.getAddress()));
        }
        modelMapper.map(customer, customerDto);
        return customerDto;
    }

    public List<CustomerResponseDto> toCustomerResponseDtoList(List<Customer> customers) {
        return customers.stream().map(customer -> {
            return toCustomerResponseDto(customer);
        }).collect(Collectors.toList());
    }



}
