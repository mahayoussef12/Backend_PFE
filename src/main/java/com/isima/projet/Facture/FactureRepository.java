package com.isima.projet.Facture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureRepository extends JpaRepository<Facture,Long> {




   List<Facture> findByEntrepriseId(Long clientID);

   List<Facture> findByClientId(int clID);

    Facture findByCode(String code);
}
