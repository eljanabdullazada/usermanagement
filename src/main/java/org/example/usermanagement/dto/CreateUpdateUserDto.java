package org.example.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUpdateUserDto {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private String phone;
    private String role;

    public CreateUpdateUserDto() {}

}

