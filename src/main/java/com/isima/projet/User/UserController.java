
package com.isima.projet.User;


import com.isima.projet.Client.ClientRepository;
import com.isima.projet.Entreprise.EntrepriseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
  @Autowired
  private ServiceUser serviceUser;
  @Autowired
  private EntrepriseRepo repo;
  @Autowired
  private ClientRepository clientRepository;
  @Autowired
  private UserRepository userRepository;
@Autowired
private PasswordEncoder encoder;

    @PostMapping("/user/test/{EntrepriseId}")
    public User createClient(@PathVariable (value = "EntrepriseId") Long EntrepriseId,@Valid @RequestBody User User) {
        return repo.findById((long) Math.toIntExact(EntrepriseId)).map(entreprise -> {
            User.setEntreprise(entreprise);
            User.setMdp(encoder.encode(User.getMdp()));
            return serviceUser.save(User);
                }).orElseThrow(() -> new IllegalArgumentException("EntrepriseId " + EntrepriseId + " not found"));



    }
    @PostMapping("/user/create/{ClientId}")
    public User create(@PathVariable (value = "ClientId") int ClientId,@Valid @RequestBody User User) {
        return clientRepository.findById(ClientId).map(client -> {
            User.setClient(client);
            User.setMdp(encoder.encode(User.getMdp()));
            return serviceUser.save(User);
        }).orElseThrow(() -> new IllegalArgumentException("EntrepriseId " + ClientId + " not found"));



    }
    @GetMapping("/user/maha/{email}")
    public User testing (@PathVariable String email ){
        return userRepository.findByEmail(email);
    }
    @GetMapping("/user/maaha/{email}")
    public String test (@PathVariable String email ){
        return userRepository.test(email);
    }

 /*   @PostMapping("/user/ajouter")
    public User createClient(@RequestBody User User) {
        User.setMdp(encoder.encode(User.getMdp()));
        return  serviceUser.save(User);

    }*/
 @PostMapping("/registration")
 @ResponseStatus(code = HttpStatus.CREATED)
 public void register(@RequestBody User userCredentialsDto) {
    /*// User user =userCredentialsDto.Build()
          //   .enabled(true)
             .username(userCredentialsDto.getUsername())
             .password(encoder.encode(userCredentialsDto.getMdp()))
             .roles(Set.of("USER"))
             .build();
     userRepository.save(user);*/
 }

}

