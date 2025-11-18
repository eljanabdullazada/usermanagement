package org.example.usermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.usermanagement.dto.CreateUpdateUserDto;
import org.example.usermanagement.dto.UserDto;
import org.example.usermanagement.entity.User;
import org.example.usermanagement.mapper.UserMapper;
import org.example.usermanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.context.annotation.Import;
import org.example.usermanagement.config.UserServiceTestConfig;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(UserServiceTestConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;


    @Test
    void testCreateUser() throws Exception {
        CreateUpdateUserDto dto = new CreateUpdateUserDto();
        dto.setEmail("test@test.com");
        dto.setName("Test User");

        User user = new User();
        user.setId(UUID.randomUUID());

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(dto.getEmail());
        userDto.setName(dto.getName());

        when(userService.create(any(CreateUpdateUserDto.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId().toString()));
    }

    @Test
    void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        UserDto userDto = new UserDto();
        userDto.setId(userId);

        when(userService.findById(userId)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()));
    }

    @Test
    void testListUsers() throws Exception {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(new User()));
        // Note: userDtoPage is not needed here as we only mock the return value of toDto(any(User.class))
        // Page<UserDto> userDtoPage = new PageImpl<>(Collections.singletonList(new UserDto()));

        when(userService.listUsers(0, 10, "createdAt")).thenReturn(userPage);
        when(userMapper.toDto(any(User.class))).thenReturn(new UserDto());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testUpdateUser() throws Exception {
        UUID userId = UUID.randomUUID();
        CreateUpdateUserDto dto = new CreateUpdateUserDto();
        dto.setEmail("new@test.com");
        dto.setName("New Name");

        User updatedUser = new User();
        updatedUser.setId(userId);

        UserDto userDto = new UserDto();
        userDto.setId(userId);

        when(userService.update(any(UUID.class), any(CreateUpdateUserDto.class))).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(userDto);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()));
    }

    @Test
    void testDeleteUser() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testHealth() throws Exception {
        mockMvc.perform(get("/api/users/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

}