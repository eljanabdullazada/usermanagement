package org.example.usermanagement.util;

import org.example.usermanagement.dto.UserDto;
import org.example.usermanagement.entity.User;

public class Mapper {
    public static UserDto toDto(User u) {
        if (u == null) return null;
        UserDto d = new UserDto();
        d.setId(u.getId());
        d.setName(u.getName());
        d.setEmail(u.getEmail());
        d.setPhone(u.getPhone());
        d.setRole(u.getRole());
        d.setCreatedAt(u.getCreatedAt());
        return d;
    }

    public static void updateFromDto(User user, String name, String email, String phone, String role) {
        if (name != null) user.setName(name);
        if (email != null) user.setEmail(email);
        if (phone != null) user.setPhone(phone);
        if (role != null) user.setRole(role);
    }
}
