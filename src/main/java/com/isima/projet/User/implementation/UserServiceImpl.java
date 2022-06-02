package com.isima.projet.User.implementation;


import com.isima.projet.User.MyUserDetails;
import com.isima.projet.User.User;
import com.isima.projet.User.UserRepository;
import com.isima.projet.User.UsernameAlreadyUsedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserDetailsService {

    @Autowired
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

   @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return  new MyUserDetails(user);
    }
    }

