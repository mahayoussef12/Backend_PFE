package com.isima.projet.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
  @Autowired
  private ServiceUser serviceUser;
@Autowired
private PasswordEncoder encoder;

    @PostMapping("/user/ajouter")
    public utilisateur createClient(@RequestBody utilisateur utilisateur) {

         return serviceUser.save(utilisateur);

    }}