package com.isima.projet.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    @Transactional
    Long deleteByUsername(String username);
    @Query("select  (a.client.id) from User a where a.email=?1")
   long test(String email);
    @Query("select  (a.entreprise.id) from User a where a.email=?1")
    long findtest(String email);
    User findByEmail(String email);


    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);
}
