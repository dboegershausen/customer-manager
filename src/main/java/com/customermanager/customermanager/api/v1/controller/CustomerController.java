package com.customermanager.customermanager.api.v1.controller;

import com.customermanager.customermanager.api.v1.dto.CustomerResponseDto;
import com.customermanager.customermanager.api.v1.mapper.CustomerMapper;
import com.customermanager.customermanager.api.v1.dto.CustomerRequestDto;
import com.customermanager.customermanager.domain.model.Customer;
import com.customermanager.customermanager.domain.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController("CategoryControllerV2")
@RequestMapping("/v1/customers")
@Api(tags = "Customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @Autowired
    private CustomerMapper mapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all customers")
    public List<CustomerResponseDto> list() {
        return mapper.toCustomerResponseDtoList(service.listAll());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Find a customer by id")
    public ResponseEntity<CustomerResponseDto> find(@ApiParam(value = "Customer Id", required = true, example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toCustomerResponseDto(service.getCustomer(id)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create a customer")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomerResponseDto> create(@RequestBody @Valid CustomerRequestDto customerDto, UriComponentsBuilder uriBuilder) {

        Customer customer = mapper.toCustomer(customerDto);
        customer = service.create(customer);

        URI location = uriBuilder.path("/v1/customers/{id}").buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(location).body(mapper.toCustomerResponseDto(customer));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update a customer")
    public ResponseEntity<CustomerResponseDto> update(@ApiParam(value = "Customer Id", required = true, example = "1") @PathVariable Long id,
                                                      @RequestBody @Valid CustomerRequestDto customerDto) {

        Customer customer =  mapper.toCustomer(customerDto);
        customer = service.update(id, customer);

        return ResponseEntity.ok(mapper.toCustomerResponseDto(customer));
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete a customer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@ApiParam(value = "Customer Id", required = true, example = "1") @PathVariable Long id) {
        service.delete(id);
    }

}
