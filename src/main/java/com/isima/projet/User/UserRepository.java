package com.isima.projet.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);

    @Transactional
    Long deleteByUsername(String username);
}
