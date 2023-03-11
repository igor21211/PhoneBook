package com.example.phonebook.dto;


import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;



@Data
public class EmailDto {
    public Long id;
    @Email(message = "Incorrect email style")
    public String email;
}
