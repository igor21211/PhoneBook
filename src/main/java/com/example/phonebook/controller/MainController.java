package com.example.phonebook.controller;

import com.example.phonebook.dto.ContactDto;
import com.example.phonebook.dto.EmailDto;
import com.example.phonebook.dto.PhoneNumberDto;
import com.example.phonebook.dto.UserDto;
import com.example.phonebook.model.Contact;
import com.example.phonebook.model.Email;
import com.example.phonebook.model.PhoneNumber;
import com.example.phonebook.model.User;
import com.example.phonebook.service.UserService;


import com.example.phonebook.util.mappers.ContactMapper;
import com.example.phonebook.util.mappers.EmailMapper;
import com.example.phonebook.util.mappers.PhoneNumberMapper;
import com.example.phonebook.util.mappers.UserMappers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;


import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {
    private final UserService userService;

    private final UserMappers userMapper;
    private final ContactMapper contactMapper;
    private final PhoneNumberMapper phoneNumberMapper;
    private final EmailMapper emailMapper;


    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@RequestBody @Valid UserDto userDto){
        User user = userService.create(userMapper.toUser(userDto));
        userService.setDataCreatedUsers(user.getId());
       return userMapper.toDto(user);
    }
    @PatchMapping("/users/restore/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public String restoreUserById(@PathVariable("user_id") Long user_id){
        return userService.restoreUsersById(user_id);
    }

    @PatchMapping("/users/remove/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteUserById(@PathVariable("user_id") Long user_id){
        return userService.removeUserById(user_id);
    }

    @PutMapping("/users/{user_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto updateUser(@PathVariable("user_id") Long user_id, @RequestBody @Valid UserDto userDto){
        User user = userService.updateUser(user_id, userMapper.toUser(userDto));
        return userMapper.toDto(user);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDto> getAllUsers(@RequestParam(required = false) String firstName,
                                     @RequestParam(required = false)String lastName,
                                     @RequestParam(required = false)LocalDateTime dateCreated,
                                     @RequestParam(defaultValue = "false")Boolean isDeleted,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10")int size,
                                     @RequestParam(defaultValue = "") List<String> sortList,
                                     @RequestParam(defaultValue = "DESC") String sort){
         Page<User> userPage =  userService.getAllUsers(firstName,lastName,isDeleted,dateCreated, page,size,sortList,sort);
        Page<UserDto> userDtoPageDtos = new PageImpl<>(
                userMapper.toUserListDto(userPage.getContent()),
                userPage.getPageable(),
                userPage.getTotalElements()
        );
        return userDtoPageDtos;
    }

    @GetMapping("/users/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable("user_id") Long user_id){
        return userMapper.toDto(userService.getUserById(user_id));
    }

    @GetMapping("/users/contacts/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public Page<ContactDto> getAllContactsByUser(@PathVariable("user_id") Long user_id,
                                                 @RequestParam(required = false) String firstName,
                                                 @RequestParam(required = false)String lastName,
                                                 @RequestParam(defaultValue = "false") Boolean isDeleted,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10")int size,
                                                 @RequestParam(defaultValue = "") List<String> sortList,
                                                 @RequestParam(defaultValue = "DESC") String sort){
      Page<Contact> contactPage =  userService.getAllContactsByUser(userService.getUserById(user_id),firstName,lastName,isDeleted, page,size,sortList,sort);
        Page<ContactDto> contactDtos = new PageImpl<>(
                contactMapper.toContactDtoList(contactPage.getContent()),
                contactPage.getPageable(),
                contactPage.getTotalElements()
        );
        return contactDtos;
    }

    @PostMapping("/users/contact/{user_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ContactDto saveContact(@PathVariable("user_id") Long id , @RequestBody @Valid ContactDto contact){
        Contact con =  userService.create(contactMapper.toContact(contact));
        userService.setToUser(con.getId(), userService.getUserById(id));
        userService.setDataCreatedContact(con.getId());
        return contactMapper.toDto(con);

    }

    @PutMapping("/users/contact/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public ContactDto updateContact(@PathVariable("user_id") Long id , @RequestBody @Valid ContactDto contact){
        Contact con =  userService.updateContact(id,contactMapper.toContact(contact));
        return contactMapper.toDto(con);

    }

    @GetMapping("/users/contact/{contact_id}")
    @ResponseStatus(HttpStatus.OK)
    public ContactDto getContact(@PathVariable("contact_id") Long id){
        return contactMapper.toDto(userService.getContactById(id));

    }

    @PatchMapping("/users/contact/{contact_id}")
    @ResponseStatus(HttpStatus.OK)
    public String removeContact(@PathVariable("contact_id") Long id){
        return userService.removeContactById(id);

    }

    @PatchMapping("/users/contact/restore/{contact_id}")
    @ResponseStatus(HttpStatus.OK)
    public String restoreContact(@PathVariable("contact_id") Long id){
        return userService.restoreContactById(id);

    }

    @PostMapping("/users/contact/phone/{contact_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public PhoneNumberDto savePhoneNumber(@PathVariable("contact_id") Long id, @RequestBody @Valid PhoneNumberDto phoneNumberDto){
        PhoneNumber phone = userService.create(phoneNumberMapper.toPhone(phoneNumberDto));
        userService.setToNumberContact(phone.getId(),userService.getContactById(id));
        return phoneNumberMapper.toDto(phone);
    }

    @PutMapping("/users/contact/phone/{contact_id}")
    @ResponseStatus(HttpStatus.OK)
    public PhoneNumberDto updatePhoneNumber(@PathVariable("contact_id") Long id, @RequestBody @Valid PhoneNumberDto phoneNumberDto){
        PhoneNumber phone = userService.updatePhone(id,phoneNumberMapper.toPhone(phoneNumberDto));
        return phoneNumberMapper.toDto(phone);
    }

    @PostMapping("/users/contact/email/{contact_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EmailDto saveEmail(@PathVariable("contact_id") Long id, @RequestBody  @Valid EmailDto email){
        Email emailNew = userService.create(emailMapper.toEmail(email));
        userService.setToEmailContact(emailNew.getId(),userService.getContactById(id));
        return emailMapper.toDto(emailNew);
    }

    @PatchMapping("/users/contact/email/{email_id}")
    @ResponseStatus(HttpStatus.OK)
    public String removeEmail(@PathVariable("email_id") Long id){
          return userService.removeEmailById(id);
    }

    @PutMapping("/users/contact/email/{contact_id}")
    @ResponseStatus(HttpStatus.OK)
    public EmailDto updateEmail(@PathVariable("contact_id") Long id, @RequestBody  @Valid EmailDto email){
        Email emailNew = userService.updateEmail(id,emailMapper.toEmail(email));
        return emailMapper.toDto(emailNew);
    }

    @GetMapping("users/contact/email/{contact_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<EmailDto> getAllEmails(@PathVariable("contact_id") Long id){
        return emailMapper.toEmailDtoList(userService.getAllEmailByContact(id));
    }

    @GetMapping("users/contact/phone/{contact_id}")
    @ResponseStatus(HttpStatus.OK)
    public List<PhoneNumberDto> getAllNumbers(@PathVariable("contact_id") Long id){
        return phoneNumberMapper.toPhoneDtoList(userService.getAllPhonesByContact(id));
    }

    @PatchMapping("/users/contact/phone/{phone_id}")
    @ResponseStatus(HttpStatus.OK)
    public String removePhone(@PathVariable("phone_id") Long id){
        return userService.removePhoneById(id);
    }

}
