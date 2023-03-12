package com.example.phonebook.service;

import com.example.phonebook.model.Contact;
import com.example.phonebook.model.Email;
import com.example.phonebook.model.PhoneNumber;
import com.example.phonebook.model.User;
import org.springframework.data.domain.Page;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface UserService {
    User create(User user);
    String removeUserById(Long id);

    String setDataCreatedUsers(Long id);
    String restoreUsersById(Long id);
    User updateUser(Long id, User user);
    User getUserById(Long id);
    PhoneNumber create(PhoneNumber phoneNumber);
    String removeContactById(Long id);
    String setDataCreatedContact(Long id);
    String removeEmailById(Long id);
    String removePhoneById(Long id);
    PhoneNumber updatePhone(Long id,PhoneNumber phoneNumber);
    PhoneNumber setToNumberContact(Long phone_id, Contact contact);
    Email create(Email email);
    Email updateEmail(Long id,Email email);
    Email setToEmailContact(Long email_id, Contact contact);
    Contact create(Contact contact);
    Contact updateContact(Long id,Contact contact);
    String restoreContactById(Long id);
    Contact setToUser(Long contact_id, User user);
    Contact getContactById(Long id);
    Email getEmailById(Long id);
    PhoneNumber getPhoneById(Long id);
    Page<User> getAllUsers(String firstName, String lastName, Boolean isDeleted, Date dateCreated , int page, int size, List<String> sortList, String sortOrder);
    Page<Contact> getAllContactsByUser(User user,
                                       String firstName,
                                       String lastName,
                                       Boolean isDeleted,
                                       int page,
                                       int size,
                                       List<String> sortList,
                                       String sortOrder);



    List<Email> getAllEmailByContact(Long contact_id);
    List<PhoneNumber> getAllPhonesByContact(Long contact_id);


}
