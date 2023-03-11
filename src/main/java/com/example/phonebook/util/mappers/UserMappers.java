package com.example.phonebook.util.mappers;

import com.example.phonebook.dto.UserDto;
import com.example.phonebook.model.User;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UserMappers {
    UserDto toDto(User User);
    User toUser(UserDto userDto);
    List<UserDto> toUserListDto(List<User> userList);
    List<User> toUserList(List<UserDto> userDtoList);

}
