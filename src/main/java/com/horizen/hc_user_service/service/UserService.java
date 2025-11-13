package com.horizen.hc_user_service.service;

import java.util.List;

import com.horizen.hc_user_service.dto.UserRequest;
import com.horizen.hc_user_service.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse getUserByUsername(String username);

    UserResponse updateUser(Long id, UserRequest userRequest);

    void deleteUser(Long id);
}