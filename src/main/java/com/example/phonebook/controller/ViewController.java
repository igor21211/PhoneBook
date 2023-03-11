package com.example.phonebook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/users/contacts/{id}")
    public String contact(){
        return "contact";
    }

    @GetMapping("users/contact/email/{id}")
    public String email(){
        return "email";
    }

    @GetMapping("users/contact/phone/{id}")
    public String phone(){
        return "phone";
    }

    @GetMapping("/users/{user_id}")
    public String updateUsers(){
        return "updateUsers";
    }

    @GetMapping("/users/remove/{id}")
    public String deleteUsers(){
        return "deleteUsers";
    }
    @GetMapping("/users/contact/{id}")
    public String deleteContact(){
        return "deleteUsers";
    }

    @GetMapping("/users/contact/{id}/")
    public String updateContact(){
        return "updateContact";
    }

    @GetMapping("/users/restore/{id}")
    public String restoreUser(){
        return "restore";
    }

    @GetMapping("/users/contact/restore/{id}")
    public String restoreContact(){
        return "restore";
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
