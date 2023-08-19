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
    /**
     * A model mapper for mapping entities to DTOs
     * and backwards.
     */
    private final ModelMapper mapper;

    /**
     * Maps an {@code AddressDto} to an {@code Address} entity.
     *
     * @param addressDto the DTO object to be mapped to an entity class.
     * @return the converted {@code Address} entity.
     */
    public Address mapAddressDtoToEntity(final AddressDto addressDto) {
        return this.mapper.map(addressDto, Address.class);
    }
}
