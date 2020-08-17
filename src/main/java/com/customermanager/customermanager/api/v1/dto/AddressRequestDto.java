package com.customermanager.customermanager.api.v1.dto;

import javax.validation.constraints.NotEmpty;

public class AddressRequestDto {

    @NotEmpty(message = "PostalCode must be informed")
    private String postalCode;
    @NotEmpty(message = "Street must be informed")
    private String street;
    private String number;
    private String details;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
