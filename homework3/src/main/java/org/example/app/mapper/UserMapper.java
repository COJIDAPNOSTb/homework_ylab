package org.example.app.mapper;

import org.example.app.dto.UserDTO;
import org.example.app.model.Role;
import org.example.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "role", target = "role") 
    UserDTO toDTO(User user);

    @Mapping(source = "role", target = "role")
    User toEntity(UserDTO userDTO);

    default String map(Role role) {
        return role != null ? role.name() : null;
    }

    default Role map(String roleName) {
        return roleName != null ? Role.valueOf(roleName) : null;
    }
}
