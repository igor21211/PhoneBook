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
import com.example.phonebook.util.Exeptions.ResourseNotRemoveTheLastWrite;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.swing.text.StyledEditorKit;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final EmailRepository emailRepository;
    private final PhoneNumberRepository phoneNumberRepository;



    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public String removeUserById(Long id) {
        userRepository.findById(id).map(
                user -> {
                    user.setDateDeleted(LocalDateTime.now());
                    user.setIsDeleted(true);
                    return userRepository.save(user);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("User not found with id = " + id));
        return "result : successful";
    }

    @Override
    public String setDataCreatedUsers(Long id) {
         userRepository.findById(id).map(
                user -> {
                    user.setDateCreated(LocalDateTime.now());
                    return userRepository.save(user);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("User not found with id = " + id));
        return "result : successful";
    }

    @Override
    public String restoreUsersById(Long id) {
        userRepository.findById(id).map(
                user -> {
                    user.setDateDeleted(null);
                    user.setIsDeleted(false);
                    return userRepository.save(user);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("User not found with id = " + id));
        return "result : successful";
    }
    @Override
    public User updateUser(Long id, User user) {
        return userRepository.findById(id).map(
                user1 -> {
                    user1.setFirstName(user.getFirstName());
                    user1.setLastName(user.getLastName());
                    return userRepository.save(user1);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("User not found with id = " + id));
    }
    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id = " + id));
        if (user.getIsDeleted()) {
            throw new ResourceWasDeletedException("User was deleted with id = " + id);
        }
        return user;
    }
    @Override
    public PhoneNumber create(PhoneNumber phoneNumber) {
        return phoneNumberRepository.save(phoneNumber);
    }

    @Override
    public String removeContactById(Long id) {
        contactRepository.findById(id).map(
                contact -> {
                    contact.setDateDeleted(LocalDateTime.now());
                    contact.setIsDeleted(true);
                    return contactRepository.save(contact);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id = " + id));
        return "result : successful";
    }

    @Override
    public String setDataCreatedContact(Long id) {
        contactRepository.findById(id).map(
                contact -> {
                    contact.setDateCreated(LocalDateTime.now());
                    return contactRepository.save(contact);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id = " + id));
        return "result : successful";
    }

    @Override
    public String removeEmailById(Long id) {
        List<Email> emailList = emailRepository.findById(id)
                .get()
                .getContact()
                .getEmailList()
                .stream()
                .filter(email -> email.getIsDeleted()!=true)
                .collect(Collectors.toList());
        if (emailList.size()>1) {
            emailRepository.findById(id).map(
                    email -> {
                        email.setIsDeleted(true);
                        return emailRepository.save(email);
                    }
            ).orElseThrow(() -> new ResourceNotFoundException("Email not found with id = " + id));
            return "result : successful";
        }else {
            throw new ResourseNotRemoveTheLastWrite("This is the last note, can't deleted");
        }
    }
    @Override
    public String removePhoneById(Long id) {
        List<PhoneNumber> phoneNumberList = phoneNumberRepository.findById(id)
                .get()
                .getContact()
                .getPhoneNumberList()
                .stream()
                .filter(phoneNumber -> phoneNumber.getIsDeleted()!=true)
                .collect(Collectors.toList());
        if (phoneNumberList.size()>1) {
            phoneNumberRepository.findById(id).map(
                    phoneNumber -> {
                        phoneNumber.setIsDeleted(true);
                        return phoneNumberRepository.save(phoneNumber);
                    }
            ).orElseThrow(() -> new ResourceNotFoundException("PhoneNumber not found with id = " + id));
            return "result : successful";
        }else {
            throw new ResourseNotRemoveTheLastWrite("This is the last note, can't deleted");
        }
    }
    @Override
    public PhoneNumber updatePhone(Long id, PhoneNumber phoneNumber) {
        return phoneNumberRepository.findById(id).map(
                phoneNumber1 -> {
                    phoneNumber1.setPhoneOfNumber(phoneNumber.getPhoneOfNumber());
                    return phoneNumberRepository.save(phoneNumber1);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Number of phone not found with id = " + id));
    }
    @Override
    public PhoneNumber setToNumberContact(Long phone_id, Contact contact) {
        return phoneNumberRepository.findById(phone_id).map(
                        phoneNumber -> {
                            phoneNumber.setContact(contact);
                            return phoneNumberRepository.save(phoneNumber);
                        })
                .orElseThrow(() -> new ResourceNotFoundException("Number of phone not found with id = " + phone_id));
    }

    @Override
    public Email create(Email email) {
        return emailRepository.save(email);
    }
    @Override
    public Email updateEmail(Long id, Email email) {
        return emailRepository.findById(id).map(
                email1 -> {
                    email1.setEmail(email.getEmail());
                    return emailRepository.save(email1);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Email not found with id = " + id));
    }

    @Override
    public Email setToEmailContact(Long email_id, Contact contact) {
        return emailRepository.findById(email_id).map(
                        email -> {
                            email.setContact(contact);
                            return emailRepository.save(email);
                        })
                .orElseThrow(() -> new ResourceNotFoundException("Email not found with id = " + email_id));
    }

    @Override
    public Contact create(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(Long id, Contact contact) {
        return contactRepository.findById(id).map(
                contact1 -> {
                    contact1.setFirstName(contact.getFirstName());
                    contact1.setLastName(contact.getLastName());
                    return contactRepository.save(contact1);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id = " + id));
    }

    @Override
    public String restoreContactById(Long id) {
        contactRepository.findById(id).map(
                user -> {
                    user.setIsDeleted(false);
                    return contactRepository.save(user);
                }
        ).orElseThrow(() -> new ResourceNotFoundException("Contact not found with id = " + id));
        return "result : successful";
    }
    @Override
    public Contact setToUser(Long contact_id, User user) {
        return contactRepository.findById(contact_id).map(
                        entity -> {
                            entity.setUser(user);
                            return contactRepository.save(entity);
                        })
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id = " + contact_id));
    }
    @Override
    public Contact getContactById(Long id) {
       Contact contact =  contactRepository.findById(id).orElseThrow(() ->
               new ResourceNotFoundException("Contact not found with id = " + id));
       if(contact.getIsDeleted()){
           throw new ResourceWasDeletedException("Contact was delete with id = " +id);
       }else
       return contact;
    }
    @Override
    public Email getEmailById(Long id) {
        return emailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Email not found with id = " + id));
    }
    @Override
    public PhoneNumber getPhoneById(Long id) {
        PhoneNumber phoneNumber =  phoneNumberRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Phone not found with id = " + id));
        if(phoneNumber.getIsDeleted()){
            throw new ResourceWasDeletedException("PhoneNumber was deleted with this id " + id);
        }
        return phoneNumber;
    }
    @Override
    public Page<User> getAllUsers(String firstName, String lastName, Boolean isDeleted, LocalDateTime dateCreated, int page, int size, List<String> sortList, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        return userRepository.findAll(getUserSpecificator(firstName,lastName, isDeleted, dateCreated), pageable);
    }
    private Specification<User> getUserSpecificator(final String firstName, final String lastName, final Boolean isDeleted, final LocalDateTime dateCreated){
        return ((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.and();
            if(dateCreated!=null) predicate=criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("dateCreated"), dateCreated));
            if(isDeleted!= null) predicate=criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("isDeleted"),isDeleted));
            if(firstName!=null) predicate = criteriaBuilder.and(predicate,criteriaBuilder.like(root.get("firstName"),"%"+firstName+"%"));
            if(lastName!=null) predicate = criteriaBuilder.and(predicate,criteriaBuilder.like(root.get("lastName"),"%"+lastName+"%"));
            return predicate;
        });
    }
    @Override
    public Page<Contact> getAllContactsByUser(User user, String firstName, String lastName, Boolean isDeleted, int page, int size, List<String> sortList, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        return contactRepository.findAll(getContactSpecification(user, firstName, lastName,isDeleted), pageable);
    }
    private Specification<Contact> getContactSpecification(final User user, final String firstName, final String lastName, final Boolean isDeleted) {
        return ((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.and();
            if(isDeleted!= null) predicate=criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("isDeleted"),isDeleted));
            if (user != null) predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("user"), user));
            if (firstName != null)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"));
            if (lastName != null)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%"));
            return predicate;
        });
    }
    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }
    @Override
    public List<Email> getAllEmailByContact(Long contact_id) {
        return contactRepository.findById(contact_id)
                .get()
                .getEmailList()
                .stream()
                .filter(email -> email.getIsDeleted()!=true)
                .collect(Collectors.toList());
    }
    @Override
    public List<PhoneNumber> getAllPhonesByContact(Long contact_id) {
        return contactRepository.findById(contact_id)
                .get()
                .getPhoneNumberList()
                .stream()
                .filter(phoneNumber -> phoneNumber.getIsDeleted()!=true)
                .collect(Collectors.toList());
    }
}
