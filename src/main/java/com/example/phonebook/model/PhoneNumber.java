package com.example.phonebook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import lombok.Data;


import static jakarta.persistence.GenerationType.SEQUENCE;


@Entity
@Table(name = "numbers_of_phone")
@Data
public class PhoneNumber {
    @Id
    @SequenceGenerator(
            name = "phone_number_sequence",
            sequenceName = "phone_number_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "phone_number_sequence"
    )
    private Long id;
    @Size(min = 13, max = 13, message = "Incorrect number ? number must have 13 digits ")
    @Column(
            name = "phone_number",
            nullable = false,
            columnDefinition = "VARCHAR(50)"
    )
    private String phoneOfNumber;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
    @JsonBackReference
    @ManyToOne
    private Contact contact;
}
