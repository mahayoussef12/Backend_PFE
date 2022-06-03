package com.isima.projet.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
    @GetMapping("/admin")
    public String adminPage() {

        return "Hello Admin";
    }
}
