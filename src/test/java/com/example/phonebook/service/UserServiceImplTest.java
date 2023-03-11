package com.example.phonebook.service;

import com.example.phonebook.model.Contact;
import com.example.phonebook.model.Email;
import com.example.phonebook.model.PhoneNumber;
import com.example.phonebook.model.User;
import com.example.phonebook.repository.ContactRepository;
import com.example.phonebook.repository.EmailRepository;
import com.example.phonebook.repository.PhoneNumberRepository;
import com.example.phonebook.repository.UserRepository;

import com.example.phonebook.util.Exeptions.ResourceNotFoundException;
import com.example.phonebook.util.Exeptions.ResourceWasDeletedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private PhoneNumberRepository phoneNumberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, contactRepository, emailRepository, phoneNumberRepository);
    }



    @Test
    void whenWeCreatedUserShouldBeReturn() {
        User user = new User();
        user.setFirstName("Artur");
        user.setLastName("Pirozkov");

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        User created = userService.create(user);
        assertThat(created.getFirstName()).isSameAs(user.getFirstName());
        assertThat(created.getLastName()).isSameAs(user.getLastName());
        verify(userRepository).save(user);
    }

    @Test
    void updateUser() {


    }

    @Test
    void whenGivenId_shouldReturnUsers_ifFound() {
        User user = new User();
        user.setId(88L);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User expected = userService.getUserById(user.getId());

        assertThat(expected).isSameAs(user);
        verify(userRepository).findById(user.getId());

    }

    @Test
    void whenWeCreatedPhoneNumberShouldBeReturn() {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneOfNumber("+380982468583");

        when(phoneNumberRepository.save(ArgumentMatchers.any(PhoneNumber.class))).thenReturn(phoneNumber);

        PhoneNumber created = userService.create(phoneNumber);
        assertThat(created.getPhoneOfNumber()).isSameAs(phoneNumber.getPhoneOfNumber());
        verify(phoneNumberRepository).save(phoneNumber);
    }

    @Test
    void getUserById_ShouldThrowExceptionWhenUserIsDeleted() {

        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setIsDeleted(true);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));


        assertThrows(ResourceWasDeletedException.class, () -> userService.getUserById(id));
        verify(userRepository).findById(id);
    }

    @Test
    public void testSetToNumberContact() {
        // создаем тестовые данные
        Long phoneId = 1L;
        Contact contact = new Contact();
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setId(phoneId);
        phoneNumber.setPhoneOfNumber("123456789");
        phoneNumber.setContact(null);

        // настройка макета репозитория
        when(phoneNumberRepository.findById(phoneId)).thenReturn(Optional.of(phoneNumber));
        when(phoneNumberRepository.save(phoneNumber)).thenReturn(phoneNumber);

        // выполнение метода и проверка результатов
        PhoneNumber result = userService.setToNumberContact(phoneId, contact);
        assertEquals(contact, result.getContact());
    }

    @Test
    void whenWeCreatedEmailShouldBeReturn() {
        Email email = new Email();
        email.setEmail("test@gmail.com");

        when(emailRepository.save(ArgumentMatchers.any(Email.class))).thenReturn(email);

        Email created = userService.create(email);
        assertThat(created.getEmail()).isSameAs(email.getEmail());
        verify(emailRepository).save(email);
    }

    @Test
    void updateEmail() {
        Long emailId = 1L;
        Email email = new Email();
        email.setId(emailId);
        email.setEmail("test@gmail.com");
        email.setContact(null);

        Email email2 = new Email();
        email.setId(emailId);
        email.setEmail("test2@gmail.com");
        email.setContact(null);

        when(emailRepository.findById(emailId)).thenReturn(Optional.of(email));
        when(emailRepository.save(email)).thenReturn(email);

        Email expected = userService.updateEmail(emailId,email2);
        assertThat(expected.getEmail()).isEqualTo(email2.getEmail());
        assertThat(expected.getId()).isEqualTo(emailId);
        assertNotEquals(email,email2);
    }

    @Test
    void setToEmailContact() {
        Long emailId = 1L;
        Email email = new Email();
        Contact contact = new Contact();
        email.setId(emailId);
        email.setEmail("test@gmail.com");
        email.setContact(null);

        when(emailRepository.findById(emailId)).thenReturn(Optional.of(email));
        when(emailRepository.save(email)).thenReturn(email);

        Email expected = userService.setToEmailContact(emailId,contact);
        assertEquals(contact,expected.getContact());
    }

    @Test
    void whenWeCreatedContactShouldBeReturn() {
        Contact contact = new Contact();
        contact.setFirstName("Igor");
        contact.setLastName("Shpura");

        when(contactRepository.save(ArgumentMatchers.any(Contact.class))).thenReturn(contact);

        Contact created = userService.create(contact);
        assertThat(created.getFirstName()).isSameAs(contact.getFirstName());
        assertThat(created.getLastName()).isSameAs(contact.getLastName());
        verify(contactRepository).save(contact);
    }

    @Test
    void setToUser() {
        Long contactId = 1L;
        Contact contact = new Contact();
        User user = new User();
        contact.setId(contactId);
        contact.setFirstName("Test");
        contact.setLastName("testLastName");

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));
        when(contactRepository.save(contact)).thenReturn(contact);

        Contact result = userService.setToUser(contactId,user);
        assertEquals(user,result.getUser());


    }

    @Test
    void whenGivenId_shouldReturnContact_ifFound() {
        Contact contact = new Contact();
        contact.setId(88L);

        when(contactRepository.findById(contact.getId())).thenReturn(Optional.of(contact));
        Contact expected = userService.getContactById(contact.getId());

        assertThat(expected).isSameAs(contact);
        verify(contactRepository).findById(contact.getId());
    }

    @Test
    void whenGivenId_shouldReturnEmail_ifFound() {
        Email email = new Email();
        email.setId(88L);

        when(emailRepository.findById(email.getId())).thenReturn(Optional.of(email));
        Email expected = userService.getEmailById(email.getId());

        assertThat(expected).isSameAs(expected);
        verify(emailRepository).findById(email.getId());
    }

    @Test
    void whenGivenId_shouldReturnPhoneNumber_ifFound() {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setId(88L);

        when(phoneNumberRepository.findById(phoneNumber.getId())).thenReturn(Optional.of(phoneNumber));
        PhoneNumber expected = userService.getPhoneById(phoneNumber.getId());

        assertThat(expected).isSameAs(phoneNumber);
        verify(phoneNumberRepository).findById(phoneNumber.getId());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.create(user);
        assertEquals(user, result);
    }

    @Test
    void testRemoveUserById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setIsDeleted(false);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        String result = userService.removeUserById(id);
        assertEquals("result : successful", result);
        assertTrue(user.getIsDeleted());
    }

    @Test
    void testRemoveUserByIdNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.removeUserById(id);
        });
    }

    @Test
    void testRestoreUsersById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setIsDeleted(true);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        String result = userService.restoreUsersById(id);
        assertEquals("result : successful", result);
        assertFalse(user.getIsDeleted());
    }

    @Test
    void testRestoreUsersByIdNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.restoreUsersById(id);
        });
    }

    @Test
    void testUpdateUser() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setFirstName("John");
        user.setLastName("Doe");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User updatedUser = new User();
        updatedUser.setId(id);
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Doe");

        User result = userService.updateUser(id, updatedUser);
        assertEquals(updatedUser.getFirstName(), result.getFirstName());
        assertEquals(updatedUser.getLastName(), result.getLastName());
    }

    @Test
    void testUpdateUserNotFound() {
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(id, new User());
        });
    }


    @Test
    public void testGetAllEmailByContact() {

        Long contactId = 1L;
        Contact contact = new Contact();
        contact.setEmailList(new ArrayList<>());
        Email email1 = new Email();
        email1.setEmail("test1@example.com");
        email1.setIsDeleted(false);
        Email email2 = new Email();
        email2.setEmail("test2@example.com");
        email2.setIsDeleted(true);
        Email email3 = new Email();
        email3.setEmail("test3@example.com");
        email3.setIsDeleted(false);
        contact.getEmailList().add(email1);
        contact.getEmailList().add(email2);
        contact.getEmailList().add(email3);

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));

        List<Email> result = userService.getAllEmailByContact(contactId);
        List<Email> expected = new ArrayList<>();
        expected.add(email1);
        expected.add(email3);
        assertEquals(expected, result);
    }

    @Test
    void getAllPhonesByContact() {
        Long contactId = 1L;
        Contact contact = new Contact();
        contact.setPhoneNumberList(new ArrayList<>());
        PhoneNumber phoneNumber1 = new PhoneNumber();
        phoneNumber1.setPhoneOfNumber("+380982468583");
        phoneNumber1.setIsDeleted(false);
        PhoneNumber phoneNumber2 = new PhoneNumber();
        phoneNumber2.setPhoneOfNumber("+380982468574");
        phoneNumber2.setIsDeleted(true);
        PhoneNumber phoneNumber3 = new PhoneNumber();
        phoneNumber3.setPhoneOfNumber("380982468555");
        phoneNumber3.setIsDeleted(false);
        contact.getPhoneNumberList().add(phoneNumber1);
        contact.getPhoneNumberList().add(phoneNumber2);
        contact.getPhoneNumberList().add(phoneNumber3);

        when(contactRepository.findById(contactId)).thenReturn(Optional.of(contact));

        List<PhoneNumber> result = userService.getAllPhonesByContact(contactId);
        List<PhoneNumber> expected = new ArrayList<>();
        expected.add(phoneNumber1);
        expected.add(phoneNumber3);
        assertEquals(expected,result);
    }
}