package com.customermanager.customermanager.api.v1.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public class CustomerRequestDto {

    @NotEmpty(message = "Name must be informed")
    private String name;
    @NotEmpty(message = "CPF must be informed")
    private String cpf;
    @NotNull(message = "BirthDate must be informed")
    @PastOrPresent(message = "BirthDate must be less than today")
    private LocalDate birthDate;
    @NotNull(message = "Address must be informed")
    @Valid
    private AddressRequestDto address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public AddressRequestDto getAddress() {
        return address;
    }

    public void setAddress(AddressRequestDto address) {
        this.address = address;
    }

}
