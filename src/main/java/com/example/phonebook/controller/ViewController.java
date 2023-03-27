package com.example.phonebook.controller;

import com.example.phonebook.model.User;
import com.example.phonebook.service.JwtService;
import com.example.phonebook.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class ViewController {

    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model){
        model.addAttribute("error", error !=null);
        model.addAttribute("logout", logout !=null);
        return "login";
    }
    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/users/contacts")
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
