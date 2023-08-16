package com.markvarga21.usermanager.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.AppUser;
import com.markvarga21.usermanager.exception.OperationType;
import com.markvarga21.usermanager.exception.UserNotFoundException;
import com.markvarga21.usermanager.repository.AppUserRepository;
import com.markvarga21.usermanager.service.AppUserService;
import com.markvarga21.usermanager.service.FormRecognizerService;
import com.markvarga21.usermanager.util.mapping.AddressMapper;
import com.markvarga21.usermanager.util.mapping.AppUserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The service class which contains the core logic of the application.
 */
@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository userRepository;
    private final AppUserMapper userMapper;
    private final AddressMapper addressMapper;
    private final FormRecognizerService formRecognizerService;
    private final Gson gson;

    /**
     * Retrieves all the users in the application.
     *
     * @return all to users stored in a {@code List}.
     * @since 1.0
     */
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

    /**
     * Saves a user in the application.
     * /TODO
     */
    @Override
    public AppUserDto createUser(MultipartFile idDocument, MultipartFile selfiePhoto, String appUserJson) {
        AppUserDto appUserDto = this.gson.fromJson(appUserJson, AppUserDto.class);
        System.out.println(appUserDto);
//        this.formRecognizerService.validateUser(appUserDto, idDocument);
//        AppUser userToSave = this.userMapper.mapAppUserDtoToEntity(appUserDto);
//        this.userRepository.save(userToSave);
//
//        AppUserDto userDto = this.userMapper.mapAppUserEntityToDto(userToSave);
//        log.info(String.format("Saving user: %s", userDto));

        return null;
    }

    /**
     * Retrieves a user from the application using its id.
     *
     * @param id the identifier of the user we want to retrieve.
     * @return the searched user.
     * @since 1.0
     */
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

    /**
     * Modifies the user's information.
     *
     * @param appUserDto the modified user information.
     * @param id the identifier of the user you want to modify.
     * @return the newly modified user's dto.
     * @since 1.0
     */
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

    /**
     * Deletes a user by its id.
     *
     * @param id the identifier used for deleting a user.
     * @return the recently deleted user's dto.
     * @since 1.0
     */
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
