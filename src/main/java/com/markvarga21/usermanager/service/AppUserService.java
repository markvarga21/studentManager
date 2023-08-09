package com.markvarga21.usermanager.service;

import com.markvarga21.usermanager.dto.AppUserDto;

import java.util.List;

public interface AppUserService {
    List<AppUserDto> getAllUsers();
    AppUserDto createUser(AppUserDto appUserDto);
    AppUserDto getUserById(Long id);
    AppUserDto modifyUserById(AppUserDto appUserDto, Long id);
    AppUserDto deleteUserById(Long id);
}
