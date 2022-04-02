package com.isima.projet.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServiceUser {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository repository;
    public utilisateur save(utilisateur user)
    {
        user.setMdp(encoder.encode(user.getMdp()));
       return repository.save(user);

    }
}
