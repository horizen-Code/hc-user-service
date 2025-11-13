package com.horizen.hc_user_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.horizen.hc_user_service.dto.CreateUserRequest;
import com.horizen.hc_user_service.dto.UpdateUserRequest;
import com.horizen.hc_user_service.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto createUser(CreateUserRequest request);

    List<UserDto> getAllUsers();

    Page<UserDto> getAllUsers(Pageable pageable);

    UserDto getUserById(UUID id);

    UserDto getUserByUsername(String username);

    UserDto getUserByEmail(String email);

    UserDto getUserByUsernameOrEmail(String username, String email);

    UserDto updateUser(UUID id, UpdateUserRequest request);

    UserDto partialUpdateUser(UUID id, UpdateUserRequest request);

    void deleteUser(UUID id);
}