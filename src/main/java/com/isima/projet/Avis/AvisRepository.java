package com.isima.projet.Avis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AvisRepository extends JpaRepository<Avis, Integer> {
       // Entreprise findById(long id);

   @Query
           ( "select AVG (u.start),u.entreprise.id from Avis u where u.entreprise.id=?1 ")

   double testing(long id);
}
