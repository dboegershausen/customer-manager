package com.customermanager.customermanager.api.v1.dto;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponseDto {

    private List<String> errorMessages = new ArrayList<>();

    public void addError(String message) {
        errorMessages.add(message);
    }

    public List<String> getErrors() {
        return errorMessages;
    }

}
