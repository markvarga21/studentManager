package com.markvarga21.usermanager.service.impl;

import com.google.gson.Gson;
import com.markvarga21.usermanager.dto.AppUserDto;
import com.markvarga21.usermanager.entity.AppUser;
import com.markvarga21.usermanager.exception.InvalidUserException;
import com.markvarga21.usermanager.exception.OperationType;
import com.markvarga21.usermanager.exception.UserNotFoundException;
import com.markvarga21.usermanager.repository.AppUserRepository;
import com.markvarga21.usermanager.service.AppUserService;
import com.markvarga21.usermanager.service.azure.FormRecognizerService;
import com.markvarga21.usermanager.util.mapping.AppUserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    /**
     * Repository for app users.
     */
    private final AppUserRepository userRepository;

    /**
     * A user mapper.
     */
    private final AppUserMapper userMapper;

    /**
     * A Form Recognizer service.
     */
    private final FormRecognizerService formRecognizerService;

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
     * Validates  and then persists the user into the database.
     *
     * @param appUserJson the user itself in a JSON string.
     * @return the updated {@code AppUserDto}.
     */
    @Override
    public AppUserDto createUser(final String appUserJson) {
        AppUserDto appUserDto = this.userMapper.mapJsonToDto(appUserJson);

        String firstName = appUserDto.getFirstName();
        String lastName = appUserDto.getLastName();
        if (!validNames(firstName, lastName)) {
            String message = String.format(
                    "'%s' first name and '%s' last name is already in use!",
                    firstName,
                    lastName
            );
            log.error(message);
            throw new InvalidUserException(message);
        }

        AppUser userToSave = this.userMapper.mapAppUserDtoToEntity(appUserDto);
        this.userRepository.save(userToSave);

        AppUserDto userDto = this.userMapper.mapAppUserEntityToDto(userToSave);
        log.info(String.format("Saving user: %s", userDto));

        return userDto;
    }

    /**
     * Checks if the users first- and last names are available.
     *
     * @param firstName the first name of the user.
     * @param lastName the last name of the user.
     * @return {@code true} if there is no other user
     * with the same name, else {@code false}.
     */
    public boolean validNames(
            final String firstName,
            final String lastName
    ) {
        Optional<AppUser> appUser = this.userRepository
                .findAppUserByFirstNameAndLastName(firstName, lastName);
        return appUser.isEmpty();
    }

    /**
     * Retrieves a user from the application using its id.
     *
     * @param id the identifier of the user we want to retrieve.
     * @return the searched user.
     * @since 1.0
     */
    @Override
    public AppUserDto getUserById(final Long id) {
        Optional<AppUser> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            String message = String.format(
                    "User cant be retrieved! Cause: user not found with id: %d",
                    id
            );
            log.error(message);
            throw new UserNotFoundException(message, OperationType.READ);
        }
        log.info(String.format("User with id %d retrieved successfully!", id));

        return this.userMapper.mapAppUserEntityToDto(userOptional.get());
    }

    /**
     * Validates and then modifies the user's information.
     *
     * @param appUserJson the user itself in a JSON string.
     * @return the updated {@code AppUserDto}.
     * @since 1.0
     */
    @Override
    public AppUserDto modifyUserById(
            final String appUserJson,
            final Long userId
    ) {
        Optional<AppUser> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            String message = String.format(
                    "User cant be modified! Cause: User not found with id: %d",
                    userId
            );
            log.error(message);
            throw new UserNotFoundException(message, OperationType.UPDATE);
        }
        AppUser userToUpdate = userOptional.get();
        AppUserDto appUserDto = this.userMapper.mapJsonToDto(appUserJson);

        userToUpdate.setGender(appUserDto.getGender());
        userToUpdate.setFirstName(appUserDto.getFirstName());
        userToUpdate.setLastName(appUserDto.getLastName());
        userToUpdate
                .setCountryOfCitizenship(appUserDto.getCountryOfCitizenship());
        userToUpdate.setPlaceOfBirth(appUserDto.getPlaceOfBirth());
        userToUpdate.setBirthDate(appUserDto.getBirthDate());
        AppUser updatedUser = this.userRepository.save(userToUpdate);

        log.info(String.format(
                "User with id %d modified successfully!", userId)
        );
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
    public AppUserDto deleteUserById(final Long id) {
        Optional<AppUser> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            String message = String.format(
                    "User cannot be deleted! Cause: user not found with id: %d",
                    id
            );
            log.error(message);
            throw new UserNotFoundException(message, OperationType.DELETE);
        }
        AppUserDto deletedUser = this.userMapper
                .mapAppUserEntityToDto(userOptional.get());
        this.userRepository.deleteById(id);
        this.formRecognizerService.deletePassportValidationByPassportNumber(
                deletedUser.getPassportNumber()
        );
        log.info(String.format("User with id %d deleted successfully!", id));

        return deletedUser;
    }
}
