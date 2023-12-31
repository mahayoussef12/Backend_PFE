package com.isima.projet.Client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Override
    List<Client> findAll();


    Client findByEmail(String email);
    
}
