package org.example.usermanagement.service.impl;

import org.example.usermanagement.dto.CreateUpdateUserDto;
import org.example.usermanagement.entity.User;
import org.example.usermanagement.exception.EmailAlreadyExistsException;
import org.example.usermanagement.exception.NotFoundException;
import org.example.usermanagement.mapper.UserMapper;
import org.example.usermanagement.repository.UserRepository;
import org.example.usermanagement.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repo;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository repo, UserMapper userMapper) {
        this.repo = repo;
        this.userMapper = userMapper;
    }

    @Override
    public User create(CreateUpdateUserDto dto) {
        log.info("Creating user with email={}", dto.getEmail());

        if (repo.existsByEmail(dto.getEmail())) {
            log.warn("Failed to create user: email already exists={}", dto.getEmail());
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User u = userMapper.toEntity(dto);
        User saved = repo.save(u);

        log.info("User created successfully id={}", saved.getId());
        return saved;
    }

    @Override
    public User findById(UUID id) {
        log.debug("Fetching user id={}", id);

        return repo.findById(id)
                .map(user -> {
                    log.info("User found id={}", id);
                    return user;
                })
                .orElseThrow(() -> {
                    log.error("User not found id={}", id);
                    return new NotFoundException("User not found");
                });
    }

    @Override
    public Page<User> listUsers(int page, int size, String sortBy) {
        log.debug("Listing users page={}, size={}, sortBy={}", page, size, sortBy);

        Pageable p = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return repo.findAll(p);
    }

    @Override
    public User update(UUID id, CreateUpdateUserDto dto) {
        log.info("Updating user id={}", id);

        User existingUser = findById(id);

        if (!existingUser.getEmail().equals(dto.getEmail())
                && repo.existsByEmail(dto.getEmail())) {
            log.warn("Failed to update user id={}, email already exists={}", id, dto.getEmail());
            throw new EmailAlreadyExistsException("Email already exists");
        }

        userMapper.updateUserFromDto(dto, existingUser);
        User updated = repo.save(existingUser);

        log.info("User updated successfully id={}", id);
        return updated;
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting user id={}", id);

        User u = findById(id);
        repo.delete(u);

        log.info("User deleted successfully id={}", id);
    }
}
