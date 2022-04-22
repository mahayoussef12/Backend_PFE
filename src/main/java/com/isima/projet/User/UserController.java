
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

    @PostMapping("/user/test")
    public User create(@RequestBody User User) {
        User.setMdp(encoder.encode(User.getMdp()));
         return User;

    }
    @PostMapping("/user/ajouter")
    public User createClient(@RequestBody User User) {
        User.setMdp(encoder.encode(User.getMdp()));
        return  serviceUser.save(User);

    }


}

