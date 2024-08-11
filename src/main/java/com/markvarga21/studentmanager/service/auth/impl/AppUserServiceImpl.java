package com.markvarga21.studentmanager.service.auth.impl;

import com.markvarga21.studentmanager.dto.Role;
import com.markvarga21.studentmanager.dto.StudentDto;
import com.markvarga21.studentmanager.entity.AppUser;
import com.markvarga21.studentmanager.entity.StudentAppUser;
import com.markvarga21.studentmanager.exception.InvalidUserCredentialsException;
import com.markvarga21.studentmanager.exception.UserNotFoundException;
import com.markvarga21.studentmanager.repository.AppUserRepository;
import com.markvarga21.studentmanager.repository.StudentAppUserRepository;
import com.markvarga21.studentmanager.service.StudentService;
import com.markvarga21.studentmanager.service.auth.AppUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The {@code AppUserServiceImpl} class is used to implement
 * the {@code AppUserService} interface.
 */
@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService {
    /**
     * The {@code AppUserRepository} object.
     */
    private final AppUserRepository appUserRepository;

    /**
     * The repository for the student application user.
     */
    private final StudentAppUserRepository studentAppUserRepository;

    /**
     * The {@code StudentService} object.
     */
    private final StudentService studentService;

    /**
     * This method is used to get the user by the username.
     *
     * @param username The username of the user.
     * @return The {@code AppUser} object.
     */
    @Override
    public Optional<AppUser> getUserByUsername(final String username) {
        return this.appUserRepository.findByUsername(username);
    }

    /**
     * This method is used to register a user.
     *
     * @param user The user object.
     * @return The {@code AppUser} object.
     */
    @Override
    public AppUser registerUser(final AppUser user) {
        log.info("Registering user: {}", user);
        Optional<AppUser> userOptional = this.appUserRepository
                .findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            String message = "User with username " + user.getUsername() + " already exists.";
            log.error(message);
            throw new InvalidUserCredentialsException(message);
        }

        Optional<StudentAppUser> studentAppUser = this.studentAppUserRepository
                .findByUsername(user.getUsername());
        Optional<StudentDto> studentDto = this.studentService
                .getStudentByFirstAndLastName(
                        user.getFirstName(),
                        user.getLastName()
                );
        if (studentAppUser.isEmpty() && studentDto.isPresent()) {
            StudentAppUser newStudentAppUser = new StudentAppUser();
            newStudentAppUser.setUsername(user.getUsername());
            newStudentAppUser.setStudentId(studentDto.get().getId());
            this.studentAppUserRepository.save(newStudentAppUser);
        }

        Set<Role> userRole = new HashSet<>();
        userRole.add(Role.USER);
        user.setRoles(userRole);
        return this.appUserRepository.save(user);
    }

    /**
     * This method is used to fetch all users.
     *
     * @param page The page number.
     * @param size The number of users in a single page.
     * @return A page of users.
     */
    @Override
    public Page<AppUser> getAllUsers(final Integer page, final Integer size) {
        return this.appUserRepository.findAll(PageRequest.of(page, size));
    }

    /**
     * Method for deleting a user from the database.
     *
     * @param id The id of the user.
     * @return The username of the deleted user.
     */
    @Override
    public String deleteUserById(final Long id) {
        Optional<AppUser> userOptional = this.appUserRepository.findById(id);
        if (userOptional.isEmpty()) {
            String message = "User with id " + id + " not found.";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        if (userOptional.get().getRoles().contains(Role.ADMIN)) {
            String message = "Cannot delete an admin user.";
            log.error(message);
            throw new InvalidUserCredentialsException(message);
        }
        this.appUserRepository.deleteById(id);
        return String.format(
                "User with username '%s' has been deleted.",
                userOptional.get().getUsername()
        );
    }

    /**
     * Method for fetching a user by its id.
     *
     * @param id the id of the user.
     * @return The {@code AppUser} object.
     */
    @Override
    public AppUser getUserById(final Long id) {
        Optional<AppUser> userOptional = this.appUserRepository.findById(id);
        if (userOptional.isEmpty()) {
            String message = "User with id " + id + " not found.";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        return userOptional.get();
    }
}
