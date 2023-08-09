package com.markvarga21.usermanager.service.impl;

import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.AppUser;
import com.markvarga21.usermanager.exception.UserNotFoundException;
import com.markvarga21.usermanager.repository.AppUserRepository;
import com.markvarga21.usermanager.service.AppUserService;
import com.markvarga21.usermanager.util.mapping.AppUserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository userRepository;
    private final AppUserMapper userMapper;

    @Override
    public List<AppUserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(this.userMapper::mapAppUserEntityToDto)
                .toList();
    }

    @Override
    public AppUserDto createUser(AppUserDto appUserDto) {
        AppUser userToSave = this.userMapper.mapAppUserDtoToEntity(appUserDto);
        this.userRepository.save(userToSave);
        return this.userMapper.mapAppUserEntityToDto(userToSave);
    }

    @Override
    public AppUserDto getUserById(Long id) {
        Optional<AppUser> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.format("User not found with id: %d", id));
        }
        return this.userMapper.mapAppUserEntityToDto(userOptional.get());
    }

    @Override
    public AppUserDto modifyUserById(AppUserDto appUserDto, Long id) {
        Optional<AppUser> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.format("User not found with id: %d", id));
        }
        AppUser userToUpdate = userOptional.get();
        userToUpdate.setAddress(appUserDto.getAddress());
        userToUpdate.setEmail(appUserDto.getEmail());
        userToUpdate.setGender(appUserDto.getGender());
        userToUpdate.setFirstName(appUserDto.getFirstName());
        userToUpdate.setLastName(appUserDto.getLastName());
        userToUpdate.setNationality(appUserDto.getNationality());
        userToUpdate.setPhoneNumber(appUserDto.getPhoneNumber());
        userToUpdate.setPlaceOfBirth(appUserDto.getPlaceOfBirth());
        userToUpdate.setBirthDate(appUserDto.getBirthDate());
        AppUser updatedUser = this.userRepository.save(userToUpdate);

        return this.userMapper.mapAppUserEntityToDto(updatedUser);
    }

    @Override
    public AppUserDto deleteUserById(Long id) {
        Optional<AppUser> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.format("User not found with id: %d", id));
        }
        AppUserDto deletedUser = this.userMapper.mapAppUserEntityToDto(userOptional.get());
        this.userRepository.deleteById(id);
        return deletedUser;
    }
}
