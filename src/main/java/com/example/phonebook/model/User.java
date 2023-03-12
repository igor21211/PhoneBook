package com.example.phonebook.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;



import lombok.Data;



import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;


@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @SequenceGenerator(
            name = "users_sequence",
            sequenceName = "users_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "users_sequence"
    )
    private Long id;


    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastName;
    @JsonManagedReference
    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<Contact> contactList;
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(
            name = "date_created",
            columnDefinition = "DATE"
    )
    private Date dateCreated;
    @Column(
            name = "date_deleted",
            columnDefinition ="DATE"
    )
    private Date dateDeleted;

}
