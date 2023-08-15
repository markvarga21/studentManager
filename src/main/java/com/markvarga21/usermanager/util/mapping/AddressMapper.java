package com.markvarga21.usermanager.util.mapping;

import com.markvarga21.usermanager.dto.AddressDto;
import com.markvarga21.usermanager.entity.Address;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * A utility class which is used for mapping between the address entities
 * and DTOs and backwards.
 */
@Component
@RequiredArgsConstructor
public class AddressMapper {
    private final ModelMapper mapper;

    /**
     * Maps an {@code AddressDto} to an {@code Address} entity.
     *
     * @param addressDto the DTO object to be mapped to an {@code Address} entity class.
     * @return the converted {@code Address} entity.
     */
    public Address mapAddressDtoToEntity(AddressDto addressDto) {
        return this.mapper.map(addressDto, Address.class);
    }

    /**
     * Maps an {@code Address} entity to an {@code AddressDto}.
     *
     * @param address the entity object to be mapped to an {@code AddressDto} class.
     * @return the converted {@code AddressDto}.
     */
    public AddressDto mapAddressEntityToDto(Address address) {
        return this.mapper.map(address, AddressDto.class);
    }
}
