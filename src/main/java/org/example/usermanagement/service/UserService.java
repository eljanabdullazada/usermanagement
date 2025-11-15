package org.example.usermanagement.service;

import org.example.usermanagement.dto.CreateUpdateUserDto;
import org.example.usermanagement.entity.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {

    User create(CreateUpdateUserDto dto);

    User findById(UUID id);

    Page<User> listUsers(int page, int size, String sortBy);

    User update(UUID id, CreateUpdateUserDto dto);

    void delete(UUID id);
}

