package com.example.phonebook.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;



@Data
public class EmailDto {
    public Long id;
    @Email(message = "Incorrect email style")
    @NotEmpty
    public String email;
}
