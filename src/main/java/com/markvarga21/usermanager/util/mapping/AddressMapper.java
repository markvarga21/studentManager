package com.markvarga21.usermanager.util.mapping;

import com.markvarga21.usermanager.dto.AddressDto;
import com.markvarga21.usermanager.entity.Address;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {
    private final ModelMapper mapper;

    public Address mapAddressDtoToEntity(AddressDto addressDto) {
        return this.mapper.map(addressDto, Address.class);
    }

    public AddressDto mapAddressEntityToDto(Address address) {
        return this.mapper.map(address, AddressDto.class);
    }
}
