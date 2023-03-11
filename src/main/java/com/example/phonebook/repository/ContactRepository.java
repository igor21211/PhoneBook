package com.example.phonebook.repository;

import com.example.phonebook.model.Contact;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact,Long>, JpaSpecificationExecutor<Contact> {

    Optional<Contact> findById(Long user_id);







}
