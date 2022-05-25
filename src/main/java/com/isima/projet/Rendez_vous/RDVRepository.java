package com.isima.projet.Rendez_vous;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RDVRepository extends JpaRepository<RDV, Integer> {
   List<RDV> findAll();
   List<RDV> findAllByClientNotNullAndEntrepriseNotNull();
   @Query("select a from RDV a where a.client.id = ?1 and a.accepter=true")
   List<RDV> findByClientId(int Id);
   @Query("select a from RDV a where a.entreprise.id = ?1 and a.accepter=true")
   List<RDV> findByEntrepriseId(Long clientId);
   @Query("select a from RDV a where a.client.id = ?1 and a.accepter=false")
   List <RDV> test(long id);
   @Query("select a from RDV a where a.entreprise.id = ?1 and a.accepter=false ")
   List <RDV> testing(long id);
 //  @Query( "select u.date_rdv from RDV u where u.entreprise.id=?1 ORDER BY u.date_rdv")
  


}
