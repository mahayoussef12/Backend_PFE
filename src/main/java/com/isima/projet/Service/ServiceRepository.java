package com.isima.projet.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<service,Integer> {

    List<service> findByEntrepriseId(Long clientId);
}
