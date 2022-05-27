package com.isima.projet.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    @Transactional
    Long deleteByUsername(String username);
    @Query("select  (a.client.id) from User a where a.email=?1")
    String test(String email);

    User findByEmail(String email);

    User getUserByUsername(String username);
}
