package com.example.phonebook.controller;

import com.example.phonebook.dto.UserDto;
import com.example.phonebook.model.User;
import com.example.phonebook.service.UserService;
import com.example.phonebook.util.Exeptions.ResourceNotFoundException;
import com.example.phonebook.util.mappers.ContactMapper;
import com.example.phonebook.util.mappers.EmailMapper;
import com.example.phonebook.util.mappers.PhoneNumberMapper;
import com.example.phonebook.util.mappers.UserMappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class MainControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMappers userMapper;

    @Mock
    private ContactMapper contactMapper;

    @Mock
    private PhoneNumberMapper phoneNumberMapper;

    @Mock
    private EmailMapper emailMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        MainController mainController = new MainController(userService, userMapper, contactMapper, phoneNumberMapper, emailMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    @Test
    public void saveUser_ShouldReturnCreatedStatus() throws Exception {

        UserDto userDto = new UserDto();
        User user = new User();
        when(userMapper.toUser(eq(userDto))).thenReturn(user);
        when(userService.create(eq(user))).thenReturn(user);
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
    public void getAllUsers_ShouldReturnPageOfUserDtos() throws Exception {

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
        // Arrange
        Long userId = 1L;
        when(userService.getUserById(userId)).thenThrow(new ResourceNotFoundException("User not found with id = " + userId));

        // Act and Assert
        mockMvc.perform(get("/api/users/{user_id}", userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllContactsByUser_ShouldReturnPageOfUserContactsDtos() throws Exception {

    }

    @Test
    void saveContact() {
    }

    @Test
    void updateContact() {
    }

    @Test
    void getContact() {
    }

    @Test
    void removeContact() {
    }

    @Test
    void restoreContact() {
    }

    @Test
    void savePhoneNumber() {
    }

    @Test
    void updatePhoneNumber() {
    }

    @Test
    void saveEmail() {
    }

    @Test
    void removeEmail() {
    }

    @Test
    void updateEmail() {
    }

    @Test
    void getAllEmails() {
    }

    @Test
    void getAllNumbers() {
    }

    @Test
    void removePhone() {
    }
}