package com.example.phonebook.util.mappers;

import com.example.phonebook.dto.EmailDto;
import com.example.phonebook.model.Email;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    EmailDto toDto(Email email);
    Email toEmail(EmailDto emailDto);
    List<EmailDto> toEmailDtoList(List<Email> emailList);
    List<Email> toEmailList(List<EmailDto> emailDtoList);
}
