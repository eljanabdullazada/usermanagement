package org.example.usermanagement.service.impl;

import org.example.usermanagement.dto.CreateUpdateUserDto;
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
    public User create(CreateUpdateUserDto dto) {
        if (repo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User u = new User(
                dto.getName(),
                dto.getEmail(),
                dto.getPhone(),
                dto.getRole()
        );

        return repo.save(u);
    }

    @Override
    public User findById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public Page<User> listUsers(int page, int size, String sortBy) {
        Pageable p = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return repo.findAll(p);
    }

    @Override
    public User update(UUID id, CreateUpdateUserDto dto) {
        User existingUser = findById(id);

        if (!existingUser.getEmail().equals(dto.getEmail())
                && repo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        existingUser.setName(dto.getName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPhone(dto.getPhone());
        existingUser.setRole(dto.getRole());

        return repo.save(existingUser);
    }

    @Override
    public void delete(UUID id) {
        User u = findById(id);
        repo.delete(u);
    }
}
