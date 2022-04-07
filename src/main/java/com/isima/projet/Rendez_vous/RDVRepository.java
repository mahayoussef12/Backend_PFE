package com.isima.projet.Rendez_vous;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RDVRepository extends JpaRepository<RDV, Integer> {
   List<RDV> findAll();
   List<RDV> findByClientId(int Id);

   List<RDV> findByEntrepriseId(Long clientId);


}