package com.isima.projet.Messagerie;

import com.isima.projet.Entreprise.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface messagerieRepository extends JpaRepository<messagerie,Long> {
    List<messagerie> findByEntreprise(Entreprise entreprise);
}
