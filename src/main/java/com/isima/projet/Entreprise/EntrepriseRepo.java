package com.isima.projet.Entreprise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrepriseRepo extends JpaRepository<Entreprise, Long>{
    List<Entreprise> findByEmail(String email);

    List<Entreprise> findByCategorie(String categorie);

    List<Entreprise> findByVille(String ville);

    List<Entreprise> findByCategorieAndVille(String categorie, String ville);
    // Entreprise findByNom(String nomSociete);
}
