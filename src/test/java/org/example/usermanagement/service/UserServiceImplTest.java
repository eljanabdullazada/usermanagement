package org.example.usermanagement.service;

import org.example.usermanagement.dto.CreateUpdateUserDto;
import org.example.usermanagement.entity.User;
import org.example.usermanagement.entity.UserRole;
import org.example.usermanagement.exception.EmailAlreadyExistsException;
import org.example.usermanagement.exception.NotFoundException;
import org.example.usermanagement.mapper.UserMapper;
import org.example.usermanagement.repository.UserRepository;
import org.example.usermanagement.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testCreateUser() {
        CreateUpdateUserDto dto = new CreateUpdateUserDto();
        dto.setEmail("test@test.com");
        dto.setName("Test User");

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@test.com");
        user.setName("Test User");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(dto);

        assertNotNull(createdUser);
        assertEquals(dto.getEmail(), createdUser.getEmail());
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_EmailExists() {
        CreateUpdateUserDto dto = new CreateUpdateUserDto();
        dto.setEmail("test@test.com");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.create(dto));

        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindById() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindById_NotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(userId));

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testListUsers() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<User> userPage = new PageImpl<>(Collections.singletonList(new User()));

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<User> result = userService.listUsers(0, 10, "name");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void testUpdateUser() {
        UUID userId = UUID.randomUUID();
        CreateUpdateUserDto dto = new CreateUpdateUserDto();
        dto.setEmail("new@test.com");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@test.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = userService.update(userId, dto);

        assertNotNull(updatedUser);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_EmailExists() {
        UUID userId = UUID.randomUUID();
        CreateUpdateUserDto dto = new CreateUpdateUserDto();
        dto.setEmail("new@test.com");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@test.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.update(userId, dto));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.delete(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_NotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.delete(userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }
}
