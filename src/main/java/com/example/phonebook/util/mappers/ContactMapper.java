package com.example.phonebook.util.mappers;

import com.example.phonebook.dto.ContactDto;
import com.example.phonebook.model.Contact;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    ContactDto toDto(Contact contact);
    Contact toContact(ContactDto contactDto);
    List<ContactDto> toContactDtoList(List<Contact> contactList);
    List<Contact> toContactList(List<ContactDto> contactDtoList);
}
