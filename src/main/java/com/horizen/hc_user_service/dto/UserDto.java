package com.horizen.hc_user_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserDto {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String skillSet;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}