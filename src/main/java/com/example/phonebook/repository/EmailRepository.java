package com.example.phonebook.repository;

import com.example.phonebook.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PatchMapping;

public interface EmailRepository extends JpaRepository<Email, Long> {

}