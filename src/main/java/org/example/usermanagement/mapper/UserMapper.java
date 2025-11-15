package org.example.usermanagement.mapper;

import org.example.usermanagement.dto.CreateUpdateUserDto;
import org.example.usermanagement.dto.UserDto;
import org.example.usermanagement.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(CreateUpdateUserDto dto);

    void updateUserFromDto(CreateUpdateUserDto dto, @MappingTarget User user);
}
