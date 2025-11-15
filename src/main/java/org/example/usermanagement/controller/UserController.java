package org.example.usermanagement.controller;

import org.example.usermanagement.dto.CreateUpdateUserDto;
import org.example.usermanagement.dto.UserDto;
import org.example.usermanagement.entity.User;
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

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUpdateUserDto dto) {
        User user = service.create(dto);
        return ResponseEntity
                .created(URI.create("/api/users/" + user.getId()))
                .body(toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(toDto(service.findById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy
    ) {
        return ResponseEntity.ok(
                service.listUsers(page, size, sortBy)
                        .map(this::toDto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUpdateUserDto dto
    ) {
        return ResponseEntity.ok(toDto(service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private UserDto toDto(User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setEmail(u.getEmail());
        dto.setPhone(u.getPhone());
        dto.setRole(u.getRole());
        dto.setCreatedAt(u.getCreatedAt());
        return dto;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}

