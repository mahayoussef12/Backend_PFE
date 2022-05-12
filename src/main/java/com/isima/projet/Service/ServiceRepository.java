package com.isima.projet.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<service,Integer> {
    service findByRdv(int id);
}
