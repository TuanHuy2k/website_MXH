package com.hust.webchat.mapper;

import com.hust.webchat.domain.User;
import com.hust.webchat.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {
    User toEntity(UserDTO dto);

    UserDTO toDto(User entity);
}
