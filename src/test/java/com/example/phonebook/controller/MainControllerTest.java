package com.example.phonebook.controller;

import com.example.phonebook.dto.ContactDto;

import com.example.phonebook.dto.UserDto;
import com.example.phonebook.model.Contact;

import com.example.phonebook.model.User;
import com.example.phonebook.service.JwtService;
import com.example.phonebook.service.JwtTokenService;
import com.example.phonebook.service.UserService;
import com.example.phonebook.util.Exeptions.ResourceNotFoundException;
import com.example.phonebook.util.mappers.ContactMapper;
import com.example.phonebook.util.mappers.EmailMapper;
import com.example.phonebook.util.mappers.PhoneNumberMapper;
import com.example.phonebook.util.mappers.UserMappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;


import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;




import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
@AllArgsConstructor
@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class MainControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMappers userMapper;

    @MockBean
    private ContactMapper contactMapper;

    @MockBean
    private PhoneNumberMapper phoneNumberMapper;

    @MockBean
    private EmailMapper emailMapper;
    @MockBean
    private final JwtService jwtService;
    @MockBean
    private final JwtTokenService jwtTokenService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MainController mainController = new MainController(userService, jwtService,userMapper, contactMapper, phoneNumberMapper, emailMapper,jwtTokenService);
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    @Test
    public void saveUser_ShouldReturnCreatedStatus() throws Exception {

        UserDto userDto = new UserDto();
        User user = new User();
        when(userMapper.toUser(eq(userDto))).thenReturn(user);
        when(userService.register(user)).thenReturn(user);
        when(userMapper.toDto(eq(user))).thenReturn(userDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(userDto)));
    }

    @Test
    public void testRestoreUserById() throws Exception {
        Long userId = 1L;

        when(userService.restoreUsersById(userId)).thenReturn("User with id " + userId + " has been restored.");

        mockMvc.perform(patch("/api/users/restore/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("User with id " + userId + " has been restored."));
    }

    @Test
    public void testDeleteUserById() throws Exception {
        Long userId = 1L;

        when(userService.removeUserById(userId)).thenReturn("User with id " + userId + " has been removed.");

        mockMvc.perform(patch("/api/users/remove/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("User with id " + userId + " has been removed."));
    }
    @Test
    public void testUpdateUserReturnsExpectedUserDto() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Dou");

        User user = new User();
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Dou");

        when(userService.updateUser(userId, userMapper.toUser(userDto))).thenReturn(user);

        User result = userService.updateUser(userId, userMapper.toUser(userDto));

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Dou",result.getLastName());
    }

    @Test
    public void testUpdateUserCallsUpdateUserWithCorrectArguments() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Dou");

        User user = userMapper.toUser(userDto);

        userService.updateUser(userId, userMapper.toUser(userDto));

        verify(userService).updateUser(userId, user);
    }

    @Test
    public void testGetUserById_ShouldReturnOkStatusAndCorrectUser() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");
        when(userService.getUserById(userId)).thenReturn(user);

        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setId(userId);
        expectedUserDto.setFirstName("John");
        expectedUserDto.setLastName("Doe");
        when(userMapper.toDto(user)).thenReturn(expectedUserDto);

        mockMvc.perform(get("/api/users/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.intValue()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testGetUserById_ShouldReturnNotFoundStatus_WhenUserNotFound() throws Exception {
        Long userId = 1L;
        when(userService.getUserById(userId)).thenThrow(new ResourceNotFoundException("User not found with id = " + userId));

        mockMvc.perform(get("/api/users/{user_id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getContact() throws Exception {
        Long contactId = 1L;
        Contact contact = new Contact();
        ContactDto contactDto = new ContactDto();
        contactDto.setId(contactId);
        when(userService.getContactById(contactId)).thenReturn(contact);
        when(contactMapper.toDto(contact)).thenReturn(contactDto);

        mockMvc.perform(get("/api/users/contact/" + contactId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveContact() throws Exception {
        long contactId = 1L;
        when(userService.removeContactById(contactId)).thenReturn("Contact removed successfully");
        mockMvc.perform(patch("/api/users/contact/{contact_id}", contactId))
                .andExpect(status().isOk());
    }

    @Test
    public void restoreContactTest() throws Exception {
        Long id = 1L;
        String expectedMessage = "Contact with id " + id + " has been restored";

        when(userService.restoreContactById(id)).thenReturn(expectedMessage);

        mockMvc.perform(patch("/api/users/contact/restore/{contact_id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));

        verify(userService, times(1)).restoreContactById(id);
    }


    @Test
    public void testRemoveEmail() throws Exception {
        Long emailId = 1L;
        String expectedMessage = "result : successful";
        Mockito.when(userService.removeEmailById(emailId)).thenReturn(expectedMessage);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/contact/email/{email_id}", emailId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    public void testRemoveEmailThrowsException() throws Exception {
        Long emailId = 1L;
        Mockito.when(userService.removeEmailById(emailId)).thenThrow(new ResourceNotFoundException("Email not found with id = " + emailId));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/contact/email/{email_id}", emailId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testRemovePhoneThrowsException() throws Exception {
        Long phoneId = 1L;
        Mockito.when(userService.removePhoneById(phoneId)).thenThrow(new ResourceNotFoundException("PhoneNumber not found with id = " + phoneId));
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/contact/phone/{phone_id}", phoneId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testRemovePhone() throws Exception {
        Long phoneId = 1L;
        String expectedMessage = "result : successful";
        Mockito.when(userService.removePhoneById(phoneId)).thenReturn(expectedMessage);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/contact/phone/{phone_id}", phoneId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

}