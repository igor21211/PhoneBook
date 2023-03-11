package com.example.phonebook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;


import lombok.Data;


import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "email")
@Data
public class Email {
    @Id
    @SequenceGenerator(
            name = "email_sequence",
            sequenceName = "email_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "email_sequence"
    )
    private Long id;


    @Column(
            name = "email",
            columnDefinition = "TEXT"
    )
    private String email;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @JsonBackReference
    @ManyToOne(
            cascade = {CascadeType.REMOVE,CascadeType.ALL,CascadeType.PERSIST}
    )
    private Contact contact;
}
