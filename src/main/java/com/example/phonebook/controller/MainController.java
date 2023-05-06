package com.example.phonebook.controller;

import com.example.phonebook.dto.ContactDto;
import com.example.phonebook.dto.EmailDto;
import com.example.phonebook.dto.PhoneNumberDto;
import com.example.phonebook.dto.UserDto;
import com.example.phonebook.model.*;
import com.example.phonebook.service.JwtService;
import com.example.phonebook.service.JwtTokenService;
import com.example.phonebook.service.UserService;


import com.example.phonebook.util.mappers.ContactMapper;
import com.example.phonebook.util.mappers.EmailMapper;
import com.example.phonebook.util.mappers.PhoneNumberMapper;
import com.example.phonebook.util.mappers.UserMappers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;


import java.util.Date;
import java.util.List;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {
    private final UserService userService;

    private final JwtService jwtService;
    private final UserMappers userMapper;
    private final ContactMapper contactMapper;
    private final PhoneNumberMapper phoneNumberMapper;
    private final EmailMapper emailMapper;
    private final JwtTokenService jwtTokenService;


    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findUser(loginRequest.getLogin()).orElseThrow();
        String token = jwtTokenService.createToken(user);
        Authentication authentication = jwtTokenService.createAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return Response.ok(token);
    }
    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.OK)
    public Response registry(@RequestBody User user){
        userService.register(user);
        return Response.ok(null);
    }

    @PatchMapping("/users/restore/")
    @ResponseStatus(HttpStatus.OK)
    public Response restoreUserById(@RequestHeader("Authorization") String token){
        User userToken = jwtService.extractUser(token.substring("Bearer ".length()));
        userService.restoreUsersById(userToken.getId());
        return Response.ok("successful");
    }

    @PatchMapping("/users/remove/")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteUserById(@RequestHeader("Authorization") String token){
        User userToken = jwtService.extractUser(token.substring("Bearer ".length()));
        userService.removeUserById(userToken.getId());
        return Response.ok("successful");
    }

    @PutMapping("/users/")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto updateUser(@RequestHeader("Authorization") String token, @RequestBody @Valid UserDto userDto){
        User userToken = jwtService.extractUser(token.substring("Bearer ".length()));
        User userNew = userService.updateUser(userToken.getId(), userMapper.toUser(userDto));
        return userMapper.toDto(userNew);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDto> getAllUsers(@RequestParam(required = false) String firstName,
                                     @RequestParam(required = false)String lastName,
                                     @RequestParam(required = false) Date dateCreated,
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

    @GetMapping("/users/")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@RequestHeader("Authorization") String token){
        User user = jwtService.extractUser(token.substring("Bearer ".length()));
        return userMapper.toDto(userService.getUserById(user.getId()));
    }

    @GetMapping("/users/contacts")
    @ResponseStatus(HttpStatus.OK)
    public Response getAllContactsByUser(@RequestHeader("Authorization") String token,
                                                 @RequestParam(required = false) String firstName,
                                                 @RequestParam(required = false)String lastName,
                                                 @RequestParam(defaultValue = "false") Boolean isDeleted,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10")int size,
                                                 @RequestParam(defaultValue = "") List<String> sortList,
                                                 @RequestParam(defaultValue = "DESC") String sort){
        User user = jwtService.extractUser(token.substring("Bearer ".length()));
      Page<Contact> contactPage =  userService.getAllContactsByUser(userService.getUserById(user.getId()),firstName,lastName,isDeleted, page,size,sortList,sort);
        Page<ContactDto> contactDtos = new PageImpl<>(
                contactMapper.toContactDtoList(contactPage.getContent()),
                contactPage.getPageable(),
                contactPage.getTotalElements()
        );
        return Response.ok(contactDtos);
    }

    @PostMapping("/users/contact/{user_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Response saveContact(@RequestHeader("Authorization") String token , @RequestBody @Valid ContactDto contact){
        User user = jwtService.extractUser(token.substring("Bearer ".length()));
        Contact con =  userService.create(contactMapper.toContact(contact));
        userService.setToUser(con.getId(), userService.getUserById(user.getId()));
        userService.setDataCreatedContact(con.getId());
        return Response.ok(contactMapper.toDto(con));
    }

    @PutMapping("/users/contact/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public Response updateContact(@RequestHeader("Authorization") String token, @RequestBody @Valid ContactDto contact){
        User user = jwtService.extractUser(token.substring("Bearer ".length()));
        Contact con =  userService.updateContact(user.getId(),contactMapper.toContact(contact));
        return Response.ok(contactMapper.toDto(con));
    }

    @GetMapping("/users/contact/{contact_id}")
    @ResponseStatus(HttpStatus.OK)
    public Response getContact(@PathVariable("contact_id") Long id){
        return Response.ok(contactMapper.toDto(userService.getContactById(id)));
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
