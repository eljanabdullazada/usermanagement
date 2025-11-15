package org.example.usermanagement.service.impl;

import org.example.usermanagement.dto.CreateUpdateUserDto;
import org.example.usermanagement.entity.User;
import org.example.usermanagement.exception.EmailAlreadyExistsException;
import org.example.usermanagement.exception.NotFoundException;
import org.example.usermanagement.mapper.UserMapper;
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
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository repo, UserMapper userMapper) {
        this.repo = repo;
        this.userMapper = userMapper;
    }

    @Override
    public User create(CreateUpdateUserDto dto) {
        if (repo.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User u = userMapper.toEntity(dto);

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
            throw new EmailAlreadyExistsException("Email already exists");
        }

        userMapper.updateUserFromDto(dto, existingUser);

        return repo.save(existingUser);
    }

    @Override
    public void delete(UUID id) {
        User u = findById(id);
        repo.delete(u);
    }
}
