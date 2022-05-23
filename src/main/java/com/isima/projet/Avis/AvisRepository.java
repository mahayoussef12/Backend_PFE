package com.isima.projet.Avis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Integer> {
       // Entreprise findById(long id);

   @Query
           ( "select AVG (u.start),u.provider.id from Avis u where u.provider.id=?1 ")

   double testing(long id);

    @Query("select a from Avis a where a.provider.id = ?1 and a.afficher=true")
   List <Avis> test(long id);
    @Query("select count (a.provider.id) from Avis a where a.provider.id = ?1 and a.afficher=true")
    double count(long id);

}
