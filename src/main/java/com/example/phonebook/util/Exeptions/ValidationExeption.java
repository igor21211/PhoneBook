package com.example.phonebook.util.Exeptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@Data
@AllArgsConstructor
@ResponseStatus(HttpStatus.OK)
public class ValidationExeption extends RuntimeException{
    public String message;
}
