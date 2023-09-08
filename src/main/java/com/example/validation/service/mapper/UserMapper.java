package com.example.validation.service.mapper;

import com.example.validation.dto.UserDto;
import com.example.validation.model.User;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(userDto.getPassword()))")
    @Mapping(target = "enabled", expression = "java(true)")
    @Mapping(target = "code", expression = "java(0000)")
    public abstract User toEntity(UserDto userDto);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    public abstract UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void update(UserDto userDto, @MappingTarget User user);

}
