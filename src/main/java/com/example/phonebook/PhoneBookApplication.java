package com.example.phonebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan("com.example.phonebook.model")
@EnableJpaRepositories("com.example.phonebook.repository")
@SpringBootApplication
public class PhoneBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhoneBookApplication.class, args);
    }

}
