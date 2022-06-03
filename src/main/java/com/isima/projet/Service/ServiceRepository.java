package com.isima.projet.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<service,Integer> {
    @Query("select a from service a where a.entreprise.id = ?1 and a.afficher=true")
    List<service> findByEntrepriseId(Long id);
    @Query("select a from service a where a.entreprise.id = ?1 and a.afficher=false")
    List<service> findByEntrepriseIddsc(Long id);
}
