package org.example.usermanagement.dto;

import lombok.Data;
import org.example.usermanagement.entity.UserRole;

import java.time.OffsetDateTime;
import java.util.UUID;
@Data
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private OffsetDateTime createdAt;

    public UserDto() {}

}

