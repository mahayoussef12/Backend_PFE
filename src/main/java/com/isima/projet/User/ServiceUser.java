package com.isima.projet.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ServiceUser {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository repository;

    public User save(User user)
    {
        user.setMdp(encoder.encode(user.getMdp()));
       return repository.save(user);

    } @Autowired
    private UserRepository userDao;


    public User connect(User user) throws UsernameAlreadyUsedException {
        User dbUser = userDao.findByUsername(user.getUsername());

        if (dbUser != null) {

            if (dbUser.getConnected()) {
                throw new UsernameAlreadyUsedException("This user is already connected: " + dbUser.getUsername());
            }

            dbUser.setConnected(true);
            return userDao.save(dbUser);
        }

        user.setConnected(true);
        return userDao.save(user);
    }


    public User disconnect(User user) {
        if (user == null) {
            return null;
        }

        User dbUser = userDao.findByUsername(user.getUsername());
        if (dbUser == null) {
            return user;
        }

        dbUser.setConnected(false);
        return userDao.save(dbUser);
    }
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user =repository.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return (UserDetails) new User(user);
    }
}
