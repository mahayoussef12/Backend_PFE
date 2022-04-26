package com.isima.projet.Rendez_vous;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RDVRepository extends JpaRepository<RDV, Integer> {
   List<RDV> findAll();
   List<RDV> findAllByClientNotNullAndEntrepriseNotNull();
   List<RDV> findByClientId(int Id);

   List<RDV> findByEntrepriseId(Long clientId);
 //  @Query( "select u.date_rdv from RDV u where u.entreprise.id=?1 ORDER BY u.date_rdv")



}
