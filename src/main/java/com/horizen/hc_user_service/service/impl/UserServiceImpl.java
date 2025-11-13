package com.horizen.hc_user_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.horizen.hc_user_service.dto.CreateUserRequest;
import com.horizen.hc_user_service.dto.UpdateUserRequest;
import com.horizen.hc_user_service.dto.UserDto;
import com.horizen.hc_user_service.model.User;
import com.horizen.hc_user_service.repository.UserRepository;
import com.horizen.hc_user_service.service.UserService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        log.info("Creating new user with username: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .skillSet(request.getSkillSet())
                .build();

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        log.info("Fetching users with pageable: {}", pageable);
        return userRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID id) {
        log.info("Fetching user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        log.info("Fetching user by username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUsernameOrEmail(String username, String email) {
        log.info("Fetching user by username: {} or email: {}", username, email);
        User user = userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with username: " + username + " or email: " + email));
        return mapToDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UUID id, UpdateUserRequest request) {
        log.info("Updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setSkillSet(request.getSkillSet());

        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    @Override
    @Transactional
    public UserDto partialUpdateUser(UUID id, UpdateUserRequest request) {
        log.info("Partially updating user with id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail());
        }

        if (request.getSkillSet() != null) {
            user.setSkillSet(request.getSkillSet());
        }

        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID id) {
        log.info("Deleting user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .skillSet(user.getSkillSet())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}