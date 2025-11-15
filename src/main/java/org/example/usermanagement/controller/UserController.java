package org.example.usermanagement.controller;

import org.example.usermanagement.dto.CreateUpdateUserDto;
import org.example.usermanagement.dto.UserDto;
import org.example.usermanagement.entity.User;
import org.example.usermanagement.mapper.UserMapper;
import org.example.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final UserMapper userMapper;

    public UserController(UserService service, UserMapper userMapper) {
        this.service = service;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUpdateUserDto dto) {
        User user = service.create(dto);
        return ResponseEntity
                .created(URI.create("/api/users/" + user.getId()))
                .body(userMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(userMapper.toDto(service.findById(id)));
    }

    // used -parameters in build.gradle to specify method parameter names automatically
    @GetMapping
    public ResponseEntity<Page<UserDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        return ResponseEntity.ok(
                service.listUsers(page, size, sortBy)
                        .map(userMapper::toDto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateUserDto dto
    ) {
        return ResponseEntity.ok(userMapper.toDto(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}

