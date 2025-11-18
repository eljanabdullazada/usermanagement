package org.example.usermanagement.config;

import org.example.usermanagement.mapper.UserMapper;
import org.example.usermanagement.service.UserService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class UserServiceTestConfig {

    /**
     * Defines a Mockito mock of UserService as a Spring Bean,
     * which Spring Boot will inject into the UserController under test.
     */
    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }

    /**
     * Defines a Mockito mock of UserMapper as a Spring Bean.
     */
    @Bean
    public UserMapper userMapper() {
        return mock(UserMapper.class);
    }
}