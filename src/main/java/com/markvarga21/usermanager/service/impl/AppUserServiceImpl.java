package com.markvarga21.usermanager.service.impl;

import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.AppUser;
import com.markvarga21.usermanager.exception.OperationType;
import com.markvarga21.usermanager.exception.UserNotFoundException;
import com.markvarga21.usermanager.repository.AppUserRepository;
import com.markvarga21.usermanager.service.AppUserService;
import com.markvarga21.usermanager.util.mapping.AddressMapper;
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
    private final AddressMapper addressMapper;

    @Override
    public List<AppUserDto> getAllUsers() {
        List<AppUserDto> userDtoList = userRepository
                .findAll()
                .stream()
                .map(this.userMapper::mapAppUserEntityToDto)
                .toList();
        log.info(String.format("Listing %d users.", userDtoList.size()));

        return userDtoList;
    }

    @Override
    public AppUserDto createUser(AppUserDto appUserDto) {
        AppUser userToSave = this.userMapper.mapAppUserDtoToEntity(appUserDto);
        this.userRepository.save(userToSave);

        AppUserDto userDto = this.userMapper.mapAppUserEntityToDto(userToSave);
        log.info(String.format("Saving user: %s", userDto));

        return userDto;
    }

    @Override
    public AppUserDto getUserById(Long id) {
        Optional<AppUser> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            String message = String.format("User cant be retrieved! Cause: user not found with id: %d", id);
            log.error(message);
            throw new UserNotFoundException(message, OperationType.READ);
        }
        log.info(String.format("User with id %d retrieved successfully!", id));

        return this.userMapper.mapAppUserEntityToDto(userOptional.get());
    }

    @Override
    public AppUserDto modifyUserById(AppUserDto appUserDto, Long id) {
        Optional<AppUser> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            String message = String.format("User cant be modified! Cause: User not found with id: %d", id);
            log.error(message);
            throw new UserNotFoundException(message, OperationType.UPDATE);
        }
        AppUser userToUpdate = userOptional.get();
        userToUpdate.setAddress(this.addressMapper.mapAddressDtoToEntity(appUserDto.getAddress()));
        userToUpdate.setEmail(appUserDto.getEmail());
        userToUpdate.setGender(appUserDto.getGender());
        userToUpdate.setFirstName(appUserDto.getFirstName());
        userToUpdate.setLastName(appUserDto.getLastName());
        userToUpdate.setNationality(appUserDto.getNationality());
        userToUpdate.setPhoneNumber(appUserDto.getPhoneNumber());
        userToUpdate.setPlaceOfBirth(this.addressMapper.mapAddressDtoToEntity(appUserDto.getPlaceOfBirth()));
        userToUpdate.setBirthDate(appUserDto.getBirthDate());
        AppUser updatedUser = this.userRepository.save(userToUpdate);

        log.info(String.format("User with id %d modified successfully!", id));
        return this.userMapper.mapAppUserEntityToDto(updatedUser);
    }

    @Override
    public AppUserDto deleteUserById(Long id) {
        Optional<AppUser> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            String message = String.format("User cannot be deleted! Cause: user not found with id: %d", id);
            log.error(message);
            throw new UserNotFoundException(message, OperationType.DELETE);
        }
        AppUserDto deletedUser = this.userMapper.mapAppUserEntityToDto(userOptional.get());
        this.userRepository.deleteById(id);
        log.info(String.format("User with id %d deleted successfully!", id));

        return deletedUser;
    }
}
