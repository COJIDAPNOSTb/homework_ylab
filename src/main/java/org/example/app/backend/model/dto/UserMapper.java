package org.example.app.backend.model.dto;

import org.example.app.backend.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    User toEntity(UserCreateDto userCreateDto);

    UserDto toUserDto(User user);

    User updateWithNull(UserDto userDto, @MappingTarget User user);
}