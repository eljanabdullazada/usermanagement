package org.example.usermanagement.service;

import org.example.usermanagement.entity.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {

    User create(User u);

    User getById(UUID id);

    Page<User> list(int page, int size, String sortBy);

    User update(UUID id, String name, String email, String phone, String role);

    void delete(UUID id);
}

