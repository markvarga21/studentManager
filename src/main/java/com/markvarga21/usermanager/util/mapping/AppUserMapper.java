package com.markvarga21.usermanager.util.mapping;

import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.AppUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppUserMapper {
    private final ModelMapper mapper;

    public AppUser mapAppUserDtoToEntity(AppUserDto appUserDto) {
        return this.mapper.map(appUserDto, AppUser.class);
    }

    public AppUserDto mapAppUserEntityToDto(AppUser appUser) {
        return this.mapper.map(appUser, AppUserDto.class);
    }
}
