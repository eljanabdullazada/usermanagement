package org.example.usermanagement.service.impl;

import org.example.usermanagement.entity.User;
import org.example.usermanagement.exception.NotFoundException;
import org.example.usermanagement.repository.UserRepository;
import org.example.usermanagement.service.UserService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public User create(User u) {
        if (repo.existsByEmail(u.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        return repo.save(u);
    }

    @Override
    public User getById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Page<User> list(int page, int size, String sortBy) {
        Pageable p = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return repo.findAll(p);
    }

    @Override
    public User update(UUID id, String name, String email, String phone, String role) {
        User user = getById(id);

        if (email != null && !email.equals(user.getEmail()) && repo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (name != null) user.setName(name);
        if (email != null) user.setEmail(email);
        if (phone != null) user.setPhone(phone);
        if (role != null) user.setRole(role);

        return repo.save(user);
    }

    @Override
    public void delete(UUID id) {
        User u = getById(id);
        repo.delete(u);
    }
}
