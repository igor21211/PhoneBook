package com.example.phonebook.util.mappers;

import com.example.phonebook.dto.EmailDto;
import com.example.phonebook.dto.PhoneNumberDto;
import com.example.phonebook.model.Email;
import com.example.phonebook.model.PhoneNumber;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhoneNumberMapper {
    PhoneNumberDto toDto(PhoneNumber phoneNumber);
    PhoneNumber toPhone(PhoneNumberDto phoneNumberDto);

    List<PhoneNumberDto> toPhoneDtoList(List<PhoneNumber> phoneNumberList);
    List<PhoneNumber> toPhoneList(List<PhoneNumberDto> phoneNumberDtoList);
}
