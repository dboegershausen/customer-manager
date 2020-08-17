package com.customermanager.customermanager.api.v1.mapper;

import com.customermanager.customermanager.api.v1.dto.AddressRequestDto;
import com.customermanager.customermanager.api.v1.dto.AddressResponseDto;
import com.customermanager.customermanager.domain.model.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    private ModelMapper modelMapper;

    public AddressMapper() {
        modelMapper = new ModelMapper();
    }

    public Address toAddress(AddressRequestDto addressDto) {
        return modelMapper.map(addressDto, Address.class);
    }

    public AddressResponseDto toAddressResponseDto(Address address) {
        return modelMapper.map(address, AddressResponseDto.class);
    }

}
