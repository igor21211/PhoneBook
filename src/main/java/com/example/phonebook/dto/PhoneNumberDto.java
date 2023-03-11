package com.example.phonebook.dto;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;




@Data
public class PhoneNumberDto {
    public Long id;

    @Size(min = 13, max = 13, message = "Incorrect number, number must have 13 digits ")
    public String phoneOfNumber;
}
