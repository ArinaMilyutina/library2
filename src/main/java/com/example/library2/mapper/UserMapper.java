package com.example.library2.mapper;

import com.example.library2.dto.AuthUserDto;
import com.example.library2.dto.RegUserDto;
import com.example.library2.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    RegUserDto regUserToRegUserDto(User user);

    User regUserDtoToUser(RegUserDto regUserDto);

    User loginUserDtoToUser(AuthUserDto authUserDto);
}
