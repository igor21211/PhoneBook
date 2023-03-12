package com.example.phonebook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import jakarta.persistence.*;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;


@Entity
@Data
@Table(name = "contact")
@RequiredArgsConstructor
@AllArgsConstructor
public class Contact {
    @Id
    @SequenceGenerator(
            name =  "contact_sequence",
            sequenceName = "contact_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "contact_sequence"
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
            mappedBy = "contact",
            orphanRemoval = true,
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER
    )
    private List<PhoneNumber> phoneNumberList;
    @JsonManagedReference
    @OneToMany(
            mappedBy = "contact",
            orphanRemoval = true,
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER
    )
    private List<Email> emailList;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(
            name = "date_created",
            columnDefinition = "TIMESTAMP"
    )

    private LocalDateTime dateCreated;
    @Column(
            name = "date_deleted",
            columnDefinition ="TIMESTAMP"

    )

    private LocalDateTime dateDeleted;

    @JsonBackReference
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    private User user;

}
