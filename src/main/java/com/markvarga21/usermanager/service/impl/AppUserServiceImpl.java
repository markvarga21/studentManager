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
}
